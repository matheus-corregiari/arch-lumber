# Contributing to Lumber

Thanks for taking the time to contribute.

## Quick Start

1. Fork and clone the repo.
2. Use JDK `21`.
3. Build once to warm up Gradle:

```bash
./gradlew :lumber:assemble
```

## Repository Layout

- `lumber/src/commonMain` -> shared API and behavior
- `lumber/src/<platform>Main` -> platform-specific `DebugOak` implementations
- `lumber/src/commonTest` -> cross-platform tests
- `docs/` -> published MkDocs content

## Development Workflow

1. Create a branch from `master`.
2. Make changes with KMP in mind. Prefer `commonMain` when possible.
3. Keep KDoc and docs aligned with shipped behavior.
4. Add or update tests when behavior changes.
5. Sync the MkDocs contributing page before building docs.

## Branching and Releases

- This repository has no `develop` branch.
- Work branches (`feature/*`, `fix/*`, `bugfix/*`, `config/*`, `docs/*`, and `chore/*`) open
  pull requests into a `release/x.y.0[-rcN]` or `hotfix/x.y.z[-rcN]` branch, where `z >= 1`.
- Pull requests targeting `master` must originate from those `release/*` or `hotfix/*` branches.
- Merging either branch type into `master` is the only automatic publication trigger.
- The release workflow derives the tag from the branch name, publishes artifacts, and creates the
  GitHub Release.

```text
feature/*, fix/*, bugfix/*, docs/*, config/*, chore/*
                         |
                         v
                 release/* or hotfix/*
                         |
                         v
                      master
                         |
                         v
            publish artifacts, tag, GitHub Release
```

## Local Validation

```bash
./gradlew :lumber:assemble
./gradlew :lumber:allTests
./gradlew detekt
./gradlew ktlintCheck
./gradlew :lumber:dokkaGeneratePublicationHtml
python -m mkdocs build --strict
```

Use the project wrapper and toolchain settings when validating changes.

## Documentation Expectations

- Keep examples short, real, and KMP-friendly.
- Explain tagged facade behavior and one-shot options like `quiet`, `maxLogLength`, and `maxTagLength` when relevant.
- Mention platform differences when they affect output.
- Update README, KDoc, generated API docs under `docs/api/`, and examples when public behavior changes.
- Generate each changelog page from the diff between the previous tag and the release tag.

## Dependency Hygiene

- Keep direct build and tooling dependencies current when updates are low risk.
- Prefer stable releases over RC, beta, or alpha unless the repo already depends on a prerelease.
- If an update needs a bigger migration, call that out explicitly instead of sneaking it into a routine refresh.

## Pull Request Checklist

- [ ] Tests updated or added when behavior changed
- [ ] KDoc updated if behavior or usage changed
- [ ] README or docs updated if the public API changed
- [ ] Validation checks passing
