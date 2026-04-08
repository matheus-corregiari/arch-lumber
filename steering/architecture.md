# Architecture Steering

## Repository Shape

- Treat `lumber/` as the single published library module.
- Keep reusable Gradle conventions in `build-logic/`.
- Keep published documentation in `docs/`; keep contributor-facing implementation guidance in `steering/`.

## Kotlin Multiplatform Boundaries

- Put shared API and behavior in `lumber/src/commonMain`.
- Put platform-specific logging backends in `lumber/src/<platform>Main`.
- Prefer changing `commonMain` first; move to platform source sets only when the behavior truly depends on the runtime.
- Keep tests in `lumber/src/commonTest` unless a platform-specific behavior cannot be verified there.

## Current Source-Set Responsibilities

- `commonMain`: public API, log dispatch, one-shot state, formatting helpers, `expect` declarations.
- `androidMain`: Android `Log` integration and Android-specific loggability behavior.
- `jvmMain` and `appleMain`: stdout logging with ANSI color formatting.
- `jsMain` and `wasmJsMain`: console-oriented implementations.
- `javaMain` and `kotlinMain`: shared intermediate source sets created by the hierarchy template; avoid placing public behavior here unless the split is intentional.

## Public Surface Strategy

- Keep the public API small and centered around `Lumber`, `Lumber.Oak`, `Lumber.Level`, and `DebugOak`.
- Add extension points through `Oak` rather than multiplying top-level APIs.
- Preserve one-shot fluent calls such as `tag`, `quiet`, `maxLogLength`, and `maxTagLength`.

## Platform Abstraction Rules

- Use `expect` and `actual` only for runtime-specific defaults and execution-context behavior.
- Keep the contract stable in `commonMain`; platform files should implement it, not redefine it.
- When adding a new platform concern, first decide whether it belongs in `_defaults.kt` or inside a platform-specific `DebugOak` implementation.

## Build Conventions

- Follow the shared Gradle convention plugins already applied by `lumber/build.gradle.kts`.
- Assume JDK 21+, Kotlin Multiplatform, Dokka, lint, optimization, and publishing are standard for the module.
- Keep dependencies minimal. This library is small and infrastructure-heavy dependencies should face a high bar.
