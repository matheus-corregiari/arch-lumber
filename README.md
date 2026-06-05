# Arch Lumber

Arch Lumber is a Kotlin Multiplatform logging library with a small public API and platform-specific
`DebugOak` defaults.

[![Maven Central][badge-maven]][link-maven]
[![License][badge-license]](/LICENSE)
[![Kotlin][badge-kotlin]](https://kotlinlang.org)
![Lint][badge-lint]
![Test][badge-test]
[![Coverage][badge-coverage]][link-coverage]

## Requirements

- Kotlin `2.4.0`
- Gradle wrapper `9.5.1`
- JDK `21` via the Gradle toolchain and Foojay resolver
- Android `minSdk 20` and `compileSdk 36`
- Use the project wrapper instead of a local Gradle install

## What it does

- `Lumber` is the logging entry point
- `Lumber.Oak` is the extension point for custom sinks
- `DebugOak` gives you platform defaults out of the box
- tagged facades keep context across calls, while quiet and length options stay one-shot

## Documentation Maintenance

- Keep KDoc aligned with the shipped contract of public APIs.
- Update MkDocs pages and the generated Dokka HTML when setup, usage, API, compatibility, or
  platform behavior changes.
- Keep dependency versions, Android compatibility notes, and toolchain references aligned with
  project configuration.
- Generate release changelogs from the diff between the previous tag and the release tag.
- Keep contributor-facing guidance in [`CONTRIBUTING.md`](CONTRIBUTING.md).

## Quick Start

```kotlin
Lumber.plant(DebugOak())
Lumber.info("App started")
```

## Installation

```kotlin
dependencies {
    implementation("io.github.matheus-corregiari:arch-lumber:<latest>")
}
```

## Core API

### Plant an oak

```kotlin
Lumber.plant(DebugOak())
```

### Log a message

```kotlin
Lumber.debug("User %s signed in", userName)
Lumber.warn("Cache miss for %s", cacheKey)
Lumber.error(exception, "Operation failed")
```

### Add context

```kotlin
val authLog = Lumber.tag("Auth")

authLog.info("Session created")
authLog.info("Session refreshed")
```

### Write a custom oak

```kotlin
class AnalyticsOak : Lumber.Oak() {
    override fun isLoggable(tag: String?, level: Lumber.Level) = level >= Lumber.Level.Info

    override fun log(level: Lumber.Level, tag: String?, message: String, error: Throwable?) {
        Analytics.track(level.name, tag, message, error)
    }
}

Lumber.plant(AnalyticsOak())
```

## Platform behavior

| Target      | Default `DebugOak` output |
|-------------|---------------------------|
| Android     | `android.util.Log`        |
| JVM         | ANSI colored stdout       |
| Apple       | ANSI colored stdout       |
| JS / WasmJS | native `console`          |

## Target compatibility

| Area              | Current value                   |
|-------------------|---------------------------------|
| Kotlin            | `2.4.0`                         |
| Gradle wrapper    | `9.5.1`                         |
| JDK toolchain     | `21`                            |
| Android           | `minSdk 20`, `compileSdk 36`    |
| Published targets | Android, JVM, Apple, JS, WasmJS |

## Docs

- [Getting Started](docs/getting-started.md)
- [Core Concepts](docs/core-concepts.md)
- [Usage Recipes](docs/recipes.md)
- [API Reference](https://matheus-corregiari.github.io/arch-lumber/api/)
- [Changelog](docs/changelog/index.md)
- [Contributing](CONTRIBUTING.md)

## Future Enhancements

- add a first-class structured logging adapter
- add a file-backed oak example for long-running apps
- add richer Android log filtering examples
- add release-note automation so changelog pages are generated from commits
- add platform-specific docs snippets for Android, JVM, Apple, JS, and WasmJS

## License

```text
Copyright 2025 Matheus Corregiari

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[link-maven]: https://search.maven.org/artifact/io.github.matheus-corregiari/arch-lumber

[link-coverage]: https://codecov.io/gh/matheus-corregiari/arch-lumber

[badge-kotlin]: https://img.shields.io/badge/kotlin-2.4.0-blue.svg?logo=kotlin

[badge-maven]: https://img.shields.io/maven-central/v/io.github.matheus-corregiari/arch-lumber.svg

[badge-license]: https://img.shields.io/github/license/matheus-corregiari/arch-lumber

[badge-coverage]: https://codecov.io/gh/matheus-corregiari/arch-lumber/graph/badge.svg?token=P977R4GMUO

[badge-lint]: https://github.com/matheus-corregiari/arch-lumber/actions/workflows/lint.yml/badge.svg

[badge-test]: https://github.com/matheus-corregiari/arch-lumber/actions/workflows/coverage.yml/badge.svg
