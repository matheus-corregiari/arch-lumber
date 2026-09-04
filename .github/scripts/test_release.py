"""Run with python -m unittest discover -s .github/scripts -p 'test_*.py'."""

import unittest
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
                    "CodeQL", "CI Gate", "Create Release Tag"}
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
                patch("release.pages", return_value=[{"name": "CodeQL", "conclusion": "failure"}]), \
                patch("release.time.sleep") as sleep:
            with self.assertRaisesRegex(ValueError, "CodeQL"):
                release.approved("sha")
            sleep.assert_not_called()

    def test_release_requires_unique_merged_pr(self):
        with patch.dict("os.environ", {"GITHUB_REPOSITORY": "owner/repo"}), \
                patch("release.pages", return_value=[]):
            with self.assertRaises(ValueError):
                release.merged_pr("sha")


if __name__ == "__main__":
    unittest.main()
