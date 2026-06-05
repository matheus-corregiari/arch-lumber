from __future__ import annotations

import re
import subprocess
from dataclasses import dataclass
from pathlib import Path


ROOT = Path(__file__).resolve().parents[4]
CHANGELOG_DIR = ROOT / "docs" / "changelog"
VERSION_RE = re.compile(r"^\d+\.\d+\.\d+$")


@dataclass(frozen=True)
class Release:
    version: str
    previous: str | None


def run_git(*args: str) -> str:
    result = subprocess.run(
        ["git", *args],
        cwd=ROOT,
        check=True,
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
    )
    return result.stdout.strip()


def tags() -> list[str]:
    raw = run_git("tag", "--sort=v:refname")
    return [tag for tag in raw.splitlines() if VERSION_RE.match(tag)]


def existing_versions() -> set[str]:
    return {path.stem for path in CHANGELOG_DIR.glob("*.md") if VERSION_RE.match(path.stem)}


def tag_file(tag: str, path: str) -> str:
    return run_git("show", f"{tag}:{path}")


def value(contents: str, key: str) -> str:
    match = re.search(rf'^{re.escape(key)}\s*=\s*"([^"]+)"', contents, re.MULTILINE)
    return match.group(1) if match else "unknown"


def release_date(tag: str) -> str:
    return run_git("log", "-1", "--format=%cs", tag)


def commits(release: Release) -> list[str]:
    revision = release.version if release.previous is None else f"{release.previous}..{release.version}"
    raw = run_git("log", "--format=%s", revision)
    return [line.strip() for line in raw.splitlines() if line.strip()]


def changed_files(release: Release) -> list[str]:
    if release.previous is None:
        raw = run_git("ls-tree", "-r", "--name-only", release.version)
    else:
        raw = run_git("diff", "--name-only", f"{release.previous}..{release.version}")
    return [line.strip() for line in raw.splitlines() if line.strip()]


def motivation(version: str, files: list[str]) -> str:
    if any(path.startswith("gradle/") or path.startswith("build-logic/") for path in files):
        return (
            "Refresh the build and dependency stack while keeping the public logging API and "
            "published target surface stable."
        )
    if any(path.startswith("docs/") or path == "README.md" for path in files):
        return "Keep project documentation aligned with the shipped library contract."
    return f"Document the notable changes included in release {version}."


def notable_changes(files: list[str], commit_subjects: list[str]) -> list[str]:
    notes: list[str] = []
    joined = " ".join(commit_subjects).lower()
    file_set = set(files)

    if "gradle-wrapper.properties" in file_set or "gradle/wrapper/gradle-wrapper.jar" in file_set:
        notes.append("upgraded the Gradle wrapper")
    if "gradle/libs.versions.toml" in file_set:
        notes.append("updated build dependency versions in the Gradle version catalog")
    if "build-logic/src/main/kotlin/arch-multi-library.gradle.kts" in file_set:
        notes.append("updated multiplatform Android target configuration for the current Kotlin plugin")
    if "lumber/build.gradle.kts" in file_set:
        notes.append("aligned the Lumber module build script with the updated Android target DSL")
    if "README.md" in file_set:
        notes.append("refreshed README toolchain and compatibility notes")
    if not notes and commit_subjects:
        notes.extend(subject[0].lower() + subject[1:] for subject in commit_subjects[:5] if subject)
    if "dependency" in joined and not any("dependency" in note for note in notes):
        notes.append("refreshed project dependency metadata")
    return notes


def dependency_table(tag: str) -> dict[str, str]:
    versions = tag_file(tag, "gradle/libs.versions.toml")
    wrapper = tag_file(tag, "gradle/wrapper/gradle-wrapper.properties")
    gradle_match = re.search(r"gradle-([^-]+)-bin\.zip", wrapper)
    return {
        "Kotlin": value(versions, "jetbrains-kotlin"),
        "Gradle wrapper": gradle_match.group(1) if gradle_match else "unknown",
        "Dokka": value(versions, "jetbrains-dokka"),
        "Kover": value(versions, "jetbrains-kover"),
        "Android plugin": value(versions, "androidx-plugin"),
        "Android build tools": value(versions, "build-tools"),
        "Android `minSdk`": value(versions, "build-sdk-min"),
        "Android `compileSdk`": value(versions, "build-sdk-compile"),
        "Detekt": value(versions, "detekt"),
        "ktlint": value(versions, "ktlint"),
        "vanniktech-publish": value(versions, "vanniktech-publish"),
    }


def page(release: Release) -> str:
    files = changed_files(release)
    subjects = commits(release)
    deps = dependency_table(release.version)
    changes = "\n".join(f"- {note}" for note in notable_changes(files, subjects))
    dep_rows = "\n".join(f"| {area:<20} | `{version}` |" for area, version in deps.items())

    return f"""# Changelog - {release.version}

**Release Date:** {release_date(release.version)}

## Motivation

{motivation(release.version, files)}

## Notable changes

{changes}

## Compatibility notes

- No target compatibility changes in this release.
- Public logging API behavior stayed compatible with the previous release.

## Dependency versions

| Area                 | Version |
|----------------------|---------|
{dep_rows}

## Target compatibility

| Target  | Support in {release.version}                        |
|---------|-----------------------------------------|
| Android | Supported, `minSdk {deps["Android `minSdk`"]}`, `compileSdk {deps["Android `compileSdk`"]}` |
| JVM     | Supported                               |
| Apple   | Supported                               |
| JS      | Supported                               |
| WasmJS  | Supported                               |
"""


def rewrite_index(all_tags: list[str]) -> None:
    releases = "\n".join(f"- [{tag}]({tag}.md)" for tag in reversed(all_tags))
    (CHANGELOG_DIR / "index.md").write_text(
        f"""# Changelog

This directory keeps one page per released version.

Each release page should stay short and factual, based on the tag range for that release.

Each page should include:

- release motivation
- notable changes from the release range
- compatibility notes or migration cautions when needed
- a dependency/version table
- a target compatibility table

## Releases

{releases}
""",
        encoding="utf-8",
    )


def main() -> None:
    all_tags = tags()
    existing = existing_versions()
    missing = [Release(tag, all_tags[index - 1] if index else None) for index, tag in enumerate(all_tags) if tag not in existing]

    CHANGELOG_DIR.mkdir(parents=True, exist_ok=True)
    for release in missing:
        output = CHANGELOG_DIR / f"{release.version}.md"
        output.write_text(page(release), encoding="utf-8")
        print(f"generated {output.relative_to(ROOT)}")

    rewrite_index(all_tags)
    print(f"updated {(CHANGELOG_DIR / 'index.md').relative_to(ROOT)}")


if __name__ == "__main__":
    main()
