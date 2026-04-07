# Arch Lumber

Arch Lumber is a Kotlin Multiplatform logging library with a small public API and platform-specific `DebugOak` defaults.

It is designed around three ideas:

- plant one or more oaks
- log with one-line calls
- keep the behavior predictable across targets

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

If you want the full contract, read the [Getting Started](getting-started.md) guide and the [API Reference](api/lumber/index.md).

## What you will find here

- concise setup instructions
- focused usage recipes
- generated API docs from Dokka
- versioned changelog pages
- a short roadmap for future enhancements
