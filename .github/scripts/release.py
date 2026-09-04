"""Release policy and provenance checks shared by all Arch repositories."""

import argparse
import json
import os
from pathlib import Path
import re
import subprocess
import time

VERSION = re.compile(r"(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-rc([1-9]\d*))?")


def git(*args):
    return subprocess.check_output(["git", *args], text=True).strip()


def api(path):
    environment = os.environ.copy()
    if token := environment.get("CI_READ_TOKEN"):
        environment["GH_TOKEN"] = token
    return json.loads(subprocess.check_output(["gh", "api", path], text=True, env=environment))


def pages(path, key=None):
    result = []
    for page in range(1, 1001):
        response = api(f"{path}{'&' if '?' in path else '?'}per_page=100&page={page}")
        batch = response[key] if key else response
        result.extend(batch)
        if len(batch) < 100:
            return result
    raise ValueError("API pagination limit reached")


def version(value):
    match = VERSION.fullmatch(value)
    if not match:
        raise ValueError(f"Invalid version: {value}; expected X.Y.Z or X.Y.Z-rcN")
    major, minor, patch, rc = match.groups()
    return int(major), int(minor), int(patch), int(rc) if rc else float("inf")


def validate(branch, tags):
    kind, separator, value = branch.partition("/")
    if not separator or kind not in ("release", "hotfix"):
        raise ValueError("master accepts only release/X.Y.Z or hotfix/X.Y.Z")
    candidate = version(value)
    releases = {tag: version(tag) for tag in tags if VERSION.fullmatch(tag)}
    if value in tags:
        raise ValueError(f"Tag {value} already exists")
    if releases and candidate <= max(releases.values()):
        raise ValueError(f"Version {value} must exceed every remote release tag")
    stable = [v[:3] for v in releases.values() if v[3] == float("inf")]
    if stable:
        major, minor, patch = max(stable)
        allowed = {(major + 1, 0, 0), (major, minor + 1, 0)} if kind == "release" else {(major, minor, patch + 1)}
        if candidate[:3] not in allowed:
            raise ValueError(f"Invalid {kind} increment from {major}.{minor}.{patch}")
    elif kind != "release" or candidate[:3] != (1, 0, 0):
        raise ValueError("The first stable release must start at release/1.0.0")
    return value


def remote_tags():
    return {line.split("\t", 1)[1].removeprefix("refs/tags/"): line.split("\t", 1)[0]
            for line in git("ls-remote", "--tags", "--refs", "origin").splitlines()}


def retry_version(branch, tags, sha):
    """Only master retries may reuse an immutable tag pointing at this exact commit."""
    kind, _, value = branch.partition("/")
    if kind not in ("release", "hotfix"):
        raise ValueError("Invalid release branch")
    version(value)
    if value not in tags:
        return validate(branch, tags)
    git("fetch", "origin", f"refs/tags/{value}")
    if git("cat-file", "-t", "FETCH_HEAD") != "tag" or git("rev-parse", "FETCH_HEAD^{}") != sha:
        raise ValueError("Existing tag does not annotate this master commit")
    return value


def output(**values):
    for name, value in values.items():
        print(f"{name}={value}")
    if path := os.environ.get("GITHUB_OUTPUT"):
        with open(path, "a", encoding="utf-8") as stream:
            for name, value in values.items():
                stream.write(f"{name}={value}\n")


def merged_pr(sha):
    repository = os.environ["GITHUB_REPOSITORY"]
    matches = [pr for pr in pages(f"repos/{repository}/commits/{sha}/pulls")
               if pr.get("merged_at") and pr["base"]["ref"] == "master" and pr["merge_commit_sha"] == sha]
    if len(matches) != 1:
        raise ValueError("Expected exactly one merged PR to master for this commit")
    return matches[0]


def prepare():
    config = json.loads(Path("build-logic/ci.json").read_text())
    event = json.loads(Path(os.environ["GITHUB_EVENT_PATH"]).read_text())
    if os.environ["GITHUB_EVENT_NAME"] == "pull_request":
        pr = event["pull_request"]
        branch = pr["head"]["ref"]
        release_version = validate(branch, remote_tags()) if pr["base"]["ref"] == "master" else ""
    else:
        branch = merged_pr(os.environ["GITHUB_SHA"])["head"]["ref"]
        release_version = retry_version(branch, remote_tags(), os.environ["GITHUB_SHA"])
    output(runner=config["runner"], version=release_version, branch=branch)


def approved(sha):
    repository = os.environ["GITHUB_REPOSITORY"]
    # Tag creation happens before the producing CI run has finished; wait for that exact run.
    for attempt in range(60):
        runs = api(f"repos/{repository}/actions/workflows/ci.yml/runs?event=push&head_sha={sha}&per_page=100")["workflow_runs"]
        runs = [run for run in runs if run["head_branch"] == "master"]
        if runs:
            run = max(runs, key=lambda entry: entry["id"])
            if run["status"] == "completed":
                if run["conclusion"] != "success":
                    raise ValueError("CI for the tagged master commit did not succeed")
                jobs = pages(f"repos/{repository}/actions/runs/{run['id']}/jobs", "jobs")
                required = {"Release Policy", "Coverage Gate", "Static Analysis", "Docs Gate", "CodeQL", "CI Gate", "Create Release Tag"}
                success = {job["name"] for job in jobs if job["conclusion"] == "success"}
                if not required <= success:
                    raise ValueError(f"Missing successful gates: {sorted(required - success)}")
                return
        time.sleep(10)
    raise ValueError("Timed out waiting for successful CI on the tagged master commit")


def verify_tag(tag):
    version(tag)
    if tag not in remote_tags():
        raise ValueError("Release tag does not exist on origin")
    git("fetch", "origin", "master", f"refs/tags/{tag}:refs/tags/{tag}")
    if git("cat-file", "-t", f"refs/tags/{tag}") != "tag":
        raise ValueError("Release must use an annotated tag")
    sha = git("rev-parse", f"refs/tags/{tag}^{{}}")
    subprocess.run(["git", "merge-base", "--is-ancestor", sha, "origin/master"], check=True)
    branch = merged_pr(sha)["head"]["ref"]
    if branch not in (f"release/{tag}", f"hotfix/{tag}"):
        raise ValueError("Tag version does not match the merged PR")
    approved(sha)
    output(sha=sha, version=tag, runner=json.loads(Path("build-logic/ci.json").read_text())["runner"])


def create_tag(branch):
    sha = os.environ["GITHUB_SHA"]
    tags = remote_tags()
    release_version = retry_version(branch, tags, sha)
    if git("rev-parse", "HEAD") != sha:
        raise ValueError("Checkout does not match the approved commit")
    if merged_pr(sha)["head"]["ref"] != branch:
        raise ValueError("Release branch does not match the merged PR")
    if release_version in tags:
        print("Existing annotated tag matches this commit; no tag mutation needed")
        return
    git("config", "user.name", "github-actions[bot]")
    git("config", "user.email", "41898282+github-actions[bot]@users.noreply.github.com")
    git("tag", "-a", release_version, sha, "-m", release_version)
    # Authentication is supplied by gh auth setup-git; never put a token in a URL.
    git("push", "origin", f"refs/tags/{release_version}")


def codeql():
    config = json.loads(Path("build-logic/ci.json").read_text())
    if compiler := config.get("codeql_kotlin"):
        path = Path("gradle/libs.versions.toml")
        path.write_text(re.sub(r'jetbrains-kotlin = "[^"]+"', f'jetbrains-kotlin = "{compiler}"', path.read_text()))


def security():
    repository = os.environ["GITHUB_REPOSITORY"]
    ref = "refs/heads/master" if os.environ["GITHUB_EVENT_NAME"] == "push" else os.environ["GITHUB_REF"]
    from urllib.parse import quote
    alerts = pages(f"repos/{repository}/code-scanning/alerts?state=open&ref={quote(ref, safe='')}")
    blocked = [alert["number"] for alert in alerts
               if alert["rule"].get("security_severity_level") in ("high", "critical")
               or alert["rule"].get("severity") == "error"]
    if blocked:
        raise ValueError(f"Blocking code scanning alerts: {blocked}")


def publications(destination):
    """Prove skipped destinations exist, or confirm completion of both registries."""
    import base64
    from urllib.error import HTTPError
    from urllib.request import Request, urlopen

    selected = {"both": [], "central": ["github"], "github": ["central"],
                "release-only": ["central", "github"], "complete": ["central", "github"]}[destination]
    coordinates = [line.split("\t") for line in Path("build/ci/publications.tsv").read_text().splitlines()]
    if not coordinates:
        raise ValueError("Empty publication manifest")
    for registry in selected:
        for group, artifact, value in coordinates:
            version(value)
            path = f"{group.replace('.', '/')}/{artifact}/{value}/{artifact}-{value}.pom"
            if registry == "central":
                url = f"https://repo.maven.apache.org/maven2/{path}"
                headers = {}
            else:
                url = f"https://maven.pkg.github.com/{os.environ['GITHUB_REPOSITORY']}/{path}"
                auth = f"{os.environ['GITHUB_ACTOR']}:{os.environ['GH_TOKEN']}".encode()
                headers = {"Authorization": "Basic " + base64.b64encode(auth).decode()}
            attempts = 60 if destination == "complete" else 1
            for attempt in range(attempts):
                try:
                    with urlopen(Request(url, headers=headers), timeout=30) as response:
                        response.read(1)
                    break
                except HTTPError as error:
                    if error.code != 404 or attempt == attempts - 1:
                        raise ValueError(f"Publication not confirmed: {registry} {group}:{artifact}:{value}") from error
                    time.sleep(15)
            print(f"Confirmed {registry}: {group}:{artifact}:{value}")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("command", choices=("prepare", "verify-tag", "create-tag", "codeql", "security", "publications"))
    parser.add_argument("value", nargs="?")
    args = parser.parse_args()
    {"prepare": prepare, "verify-tag": lambda: verify_tag(args.value),
     "create-tag": lambda: create_tag(args.value), "codeql": codeql, "security": security,
     "publications": lambda: publications(args.value)}[args.command]()


if __name__ == "__main__":
    main()
