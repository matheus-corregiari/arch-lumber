# Arch Lumber

Arch Lumber is a Kotlin Multiplatform logging library with a small public API and platform-specific `DebugOak` defaults.

It is designed around three ideas:

- plant one or more oaks
- log with one-line calls
- keep the behavior predictable across targets

## Compatibility

- Kotlin `2.4.10`
- Android `minSdk 20`, `compileSdk 37`
- Gradle wrapper `9.7.1`
- JDK toolchain `21` resolved through Foojay

## Why it exists

The library aims to give you a Timber-like logging flow without forcing a JVM-only model.

It keeps the core surface small:

- `Lumber` as the entry point
- `Lumber.Oak` as the extension point
- `DebugOak` as the default target implementation

## Start Here

```kotlin
Lumber.plant(DebugOak())
Lumber.info("App started")
```

If you want the full contract, read the [Getting Started](getting-started.md) guide and the [API Reference](https://matheus-corregiari.github.io/arch-lumber/api/).

## What you will find here

- concise setup instructions
- focused usage recipes
- direct API docs generated from Dokka HTML
- versioned changelog pages
- a short roadmap for future enhancements
- contributor notes that keep docs and API references in sync
- compatibility and toolchain references that match the project configuration
