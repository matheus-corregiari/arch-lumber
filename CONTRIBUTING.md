# Contributing to Lumber

Thanks for taking the time to contribute! This guide keeps the workflow simple and aligned with
common open-source practices.

## Quick Start

1. **Fork & clone** the repo.
2. **Use a recent JDK** (21+ recommended).
3. **Build once** to warm up Gradle:
   ```bash
   ./gradlew :lumber:assemble
   ```

## Repository Layout (KMP focus)

* `lumber/src/commonMain` → shared API and behavior.
* `lumber/src/<platform>Main` → platform-specific DebugOak implementations.
* `lumber/src/commonTest` → cross-platform tests (preferred).

## Development Workflow

1. Create a branch from `main`.
2. Make changes with KMP in mind (favor `commonMain` when possible).
3. Keep KDoc/Javadoc **accurate and consistent** with actual behavior.
4. Add or update **unit tests** for changes in behavior.

## Running Tests

```bash
./gradlew :lumber:allTests
```

## Documentation Expectations

Keep docs simple and easy to follow:

* Prefer **short code snippets** that work in KMP.
* Explain **one-shot** behaviors (`tag`, `quiet`, `maxLogLength`, `maxTagLength`) where relevant.
* Mention **platform differences** when they affect output.

## Dependency Hygiene

* Keep libraries updated where possible.
* If a problem looks complex, propose **more efficient or KMP-friendly alternatives** in the PR
  description (e.g., smaller runtime, better multiplatform support).

## Pull Request Checklist

* [ ] Tests updated/added and passing.
* [ ] KDoc/Javadoc updated if behavior or usage changed.
* [ ] README/docs updated if the public API changed.
* [ ] Changes verified across KMP targets where possible.
