---
name: project-changelog
description: Generate and review Arch Lumber project changelog pages from Git tag ranges. Use when Codex needs to find missing docs/changelog pages, create release changelogs that match this repo's format, update docs/changelog/index.md, or validate changelog/docs consistency against Gradle, README, MkDocs, and release tags.
---

# Project Changelog

## Workflow

1. Inspect tags with `git tag --sort=v:refname`.
2. Compare tags against `docs/changelog/*.md`.
3. For each missing release page, generate content from the previous tag range.
4. Update `docs/changelog/index.md` newest-first.
5. Validate the generated changelog against project configuration.
6. Run docs and test checks when requested or when public behavior changed.

## Generation

Use `scripts/generate_missing_changelogs.py` from the repository root:

```bash
python .codex/skills/project-changelog/scripts/generate_missing_changelogs.py
```

The script:

- reads semantic version tags from Git
- detects missing `docs/changelog/<version>.md` pages
- summarizes commits and changed files between the previous tag and release tag
- extracts dependency versions from `gradle/libs.versions.toml` at the release tag
- writes pages using the existing changelog structure
- rewrites `docs/changelog/index.md` newest-first

Review generated text before finalizing. Keep release notes factual and short.

## Project Rules

- Base each release page on the diff between the previous tag and the release tag.
- Prefer actual project configuration over README claims when they disagree.
- Keep target compatibility table stable unless the tag changes target support.
- Include compatibility or migration cautions only when the release range shows them.
- Do not create changelog pages for uncommitted worktree changes unless the user explicitly asks for an unreleased draft.

## Validation

Use the tightest checks that match the change:

```bash
python -m mkdocs build --strict
./gradlew allTests --no-daemon
```

Also run `git diff --check` after generated Markdown edits.
