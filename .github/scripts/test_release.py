"""Run with python -m unittest discover -s .github/scripts -p 'test_*.py'."""

import base64
import re
import unittest
from urllib.error import HTTPError
from unittest.mock import patch

import release


class ReleasePolicyTest(unittest.TestCase):
    def test_release_increments(self):
        for branch in ("release/1.4.0", "release/2.0.0"):
            self.assertEqual(branch.split("/")[1], release.validate(branch, ["1.3.4"]))

    def test_hotfix_increments(self):
        self.assertEqual("1.3.5", release.validate("hotfix/1.3.5", ["1.3.4"]))

    def test_invalid_branches_and_increments(self):
        for branch in ("feature/x", "release/1.4", "release/01.4.0", "release/1.3.5",
                       "release/1.5.0", "hotfix/1.4.0", "hotfix/1.3.6", "release/2.0.1"):
            with self.subTest(branch=branch), self.assertRaises(ValueError):
                release.validate(branch, ["1.3.4"])

    def test_duplicate_and_historical_versions(self):
        for tags in (["1.3.4", "1.4.0"], ["1.3.4", "2.0.0"]):
            with self.assertRaises(ValueError):
                release.validate("release/1.4.0", tags)

    def test_numeric_order(self):
        self.assertEqual("1.10.0", release.validate("release/1.10.0", ["1.9.0"]))

    def test_rc_progression_and_promotion(self):
        tags = ["1.3.4", "1.4.0-rc9"]
        self.assertEqual("1.4.0-rc10", release.validate("release/1.4.0-rc10", tags))
        self.assertEqual("1.4.0", release.validate("release/1.4.0", tags))
        with self.assertRaises(ValueError):
            release.validate("release/1.4.0-rc10", ["1.4.0"])

    def test_initial_release(self):
        self.assertEqual("1.0.0", release.validate("release/1.0.0", []))
        with self.assertRaises(ValueError):
            release.validate("hotfix/1.0.1", [])

    def test_remote_failure_is_not_empty_history(self):
        with patch("release.git", side_effect=RuntimeError("remote unavailable")):
            with self.assertRaises(RuntimeError):
                release.remote_tags()

    def test_master_retry_requires_exact_annotated_tag(self):
        with patch("release.git", side_effect=["", "tag", "approved-sha"]):
            self.assertEqual("1.4.0", release.retry_version("release/1.4.0", {"1.4.0": "tag-object"}, "approved-sha"))
        with patch("release.git", side_effect=["", "tag", "different-sha"]):
            with self.assertRaises(ValueError):
                release.retry_version("release/1.4.0", {"1.4.0": "tag-object"}, "approved-sha")

    def test_job_pagination_uses_jobs_envelope(self):
        with patch("release.api", return_value={"jobs": [{"name": "CI Gate"}]}):
            self.assertEqual([{"name": "CI Gate"}], release.pages("jobs", "jobs"))

    def test_publication_requires_successful_named_gates(self):
        run = {"id": 1, "head_branch": "master", "status": "in_progress", "conclusion": None}
        with patch.dict("os.environ", {"GITHUB_REPOSITORY": "owner/repo"}), \
                patch("release.api", return_value={"workflow_runs": [run]}), \
                patch("release.pages", return_value=[{"name": "CI Gate", "conclusion": "skipped"}]):
            with self.assertRaises(ValueError):
                release.approved("sha")

    def test_publication_ignores_non_gate_job_failure(self):
        run = {"id": 1, "head_branch": "master", "status": "in_progress", "conclusion": None}
        required = {"Release Policy", "Coverage Gate", "Static Analysis", "Docs Gate",
                    "CodeQL (actions)", "CodeQL (java-kotlin)", "CodeQL (python)",
                    "CodeQL Policy", "CI Gate", "Create Release Tag"}
        jobs = [{"name": name, "conclusion": "success"} for name in required]
        jobs.append({"name": "Deploy Docs", "conclusion": "failure"})
        with patch.dict("os.environ", {"GITHUB_REPOSITORY": "owner/repo"}), \
                patch("release.api", return_value={"workflow_runs": [run]}), \
                patch("release.pages", return_value=jobs):
            release.approved("sha")

    def test_publication_rejects_failed_gate_immediately(self):
        run = {"id": 1, "head_branch": "master", "status": "in_progress", "conclusion": None}
        with patch.dict("os.environ", {"GITHUB_REPOSITORY": "owner/repo"}), \
                patch("release.api", return_value={"workflow_runs": [run]}), \
                patch("release.pages", return_value=[{"name": "CodeQL (java-kotlin)", "conclusion": "failure"}]), \
                patch("release.time.sleep") as sleep:
            with self.assertRaisesRegex(ValueError, "CodeQL"):
                release.approved("sha")
            sleep.assert_not_called()

    def test_publication_waits_for_every_codeql_gate(self):
        run = {"id": 1, "head_branch": "master"}
        names = {"Release Policy", "Coverage Gate", "Static Analysis", "Docs Gate",
                 "CodeQL (actions)", "CodeQL (java-kotlin)", "CodeQL (python)",
                 "CodeQL Policy", "CI Gate", "Create Release Tag"}
        complete = [{"name": name, "conclusion": "success"} for name in names]
        for gate in ("CodeQL (actions)", "CodeQL (java-kotlin)", "CodeQL (python)", "CodeQL Policy"):
            for conclusion in (None, "missing"):
                pending = [dict(job, conclusion=None) if job["name"] == gate else job
                           for job in complete if conclusion != "missing" or job["name"] != gate]
                with self.subTest(gate=gate, conclusion=conclusion), \
                        patch.dict("os.environ", {"GITHUB_REPOSITORY": "owner/repo"}), \
                        patch("release.api", return_value={"workflow_runs": [run]}), \
                        patch("release.pages", side_effect=[pending, complete]), \
                        patch("release.time.sleep") as sleep:
                    release.approved("sha")
                    sleep.assert_called_once_with(10)

    def test_publication_rejects_unsuccessful_codeql_gates(self):
        run = {"id": 1, "head_branch": "master"}
        for gate in ("CodeQL (actions)", "CodeQL (java-kotlin)", "CodeQL (python)", "CodeQL Policy"):
            for conclusion in ("failure", "cancelled", "timed_out", "action_required", "skipped"):
                with self.subTest(gate=gate, conclusion=conclusion), \
                        patch.dict("os.environ", {"GITHUB_REPOSITORY": "owner/repo"}), \
                        patch("release.api", return_value={"workflow_runs": [run]}), \
                        patch("release.pages", return_value=[{"name": gate, "conclusion": conclusion}]), \
                        patch("release.time.sleep") as sleep:
                    with self.assertRaisesRegex(ValueError, re.escape(gate)):
                        release.approved("sha")
                    sleep.assert_not_called()

    def test_release_requires_unique_merged_pr(self):
        with patch.dict("os.environ", {"GITHUB_REPOSITORY": "owner/repo"}), \
                patch("release.pages", return_value=[]):
            with self.assertRaises(ValueError):
                release.merged_pr("sha")


class RecoveryPublicationTest(unittest.TestCase):
    def setUp(self):
        environment = patch.dict("os.environ", {
            "GITHUB_REPOSITORY": "owner/repo", "GITHUB_ACTOR": "publisher", "GH_TOKEN": "test-token",
        })
        environment.start()
        self.addCleanup(environment.stop)
        manifest = patch("release.Path.read_text", return_value="io.example\tlibrary\t1.4.4\nio.example\tlibrary-jvm\t1.4.4")
        self.manifest = manifest.start()
        self.addCleanup(manifest.stop)
        http = patch("urllib.request.urlopen")
        self.http = http.start()
        self.addCleanup(http.stop)

    def test_both_does_not_probe_destinations_that_will_be_uploaded(self):
        release.publications("both")
        self.http.assert_not_called()

    def test_recovery_checks_every_coordinate_only_in_omitted_destinations(self):
        central = "https://repo.maven.apache.org/maven2/"
        github = "https://maven.pkg.github.com/owner/repo/"
        paths = ["io/example/library/1.4.4/library-1.4.4.pom",
                 "io/example/library-jvm/1.4.4/library-jvm-1.4.4.pom"]
        for destination, registries in (("central", [github]), ("github", [central]),
                                        ("release-only", [central, github])):
            with self.subTest(destination=destination):
                self.http.reset_mock()
                release.publications(destination)
                requests = [call.args[0] for call in self.http.call_args_list]
                self.assertEqual([registry + path for registry in registries for path in paths],
                                 [request.full_url for request in requests])
                for request in requests:
                    if request.full_url.startswith(github):
                        expected = "Basic " + base64.b64encode(b"publisher:test-token").decode()
                        self.assertEqual(expected, request.get_header("Authorization"))
                    else:
                        self.assertIsNone(request.get_header("Authorization"))

    def test_missing_or_forbidden_publication_blocks_recovery_without_polling(self):
        for code in (404, 403):
            with self.subTest(code=code), patch("release.time.sleep") as sleep:
                self.http.reset_mock()
                self.http.side_effect = HTTPError("https://example.invalid", code, "unavailable", {}, None)
                with self.assertRaisesRegex(ValueError, "central io.example:library:1.4.4"):
                    release.publications("release-only")
                self.assertEqual(1, self.http.call_count)
                sleep.assert_not_called()

    def test_missing_github_publication_blocks_central_only_recovery(self):
        self.http.side_effect = HTTPError("https://example.invalid", 404, "missing", {}, None)
        with self.assertRaisesRegex(ValueError, "github io.example:library:1.4.4"):
            release.publications("central")

    def test_recovery_rejects_empty_manifest(self):
        self.manifest.return_value = ""
        with self.assertRaisesRegex(ValueError, "Empty publication manifest"):
            release.publications("release-only")
        self.http.assert_not_called()

    def test_recovery_rejects_invalid_version_before_requesting_a_pom(self):
        self.manifest.return_value = "io.example\tlibrary\tinvalid"
        with self.assertRaisesRegex(ValueError, "Invalid version"):
            release.publications("release-only")
        self.http.assert_not_called()

    def test_unknown_destination_and_removed_polling_mode_are_rejected(self):
        for destination in ("complete", "invalid", None):
            with self.subTest(destination=destination), self.assertRaisesRegex(ValueError, "Unknown recovery destination"):
                release.publications(destination)
        self.http.assert_not_called()


if __name__ == "__main__":
    unittest.main()
