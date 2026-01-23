//[Lumber](../../../index.md)/[br.com.arch.toolkit.lumber](../index.md)/[Lumber](index.md)

# Lumber

[common]\
class [Lumber](index.md)

# Lumber - A Lightweight Logging Library for Kotlin Multiplatform

Lumber is a modern, Kotlin Multiplatform (KMP) logging library inspired by [Timber by Jake Wharton](https://github.com/JakeWharton/timber).

Unlike Timber (JVM/Android only), Lumber works across multiple targets:

- 
   **JVM** (desktop, backend)
- 
   **Android**
- 
   **Kotlin/Native** (iOS, Linux, Windows)
- 
   **Kotlin/JS** & **Wasm**

## Key Features

- 
   Idiomatic Kotlin API with extension-friendly design.
- 
   Multiple log levels ([Level.Verbose](-level/-verbose/index.md), [Level.Debug](-level/-debug/index.md), [Level.Info](-level/-info/index.md), [Level.Warn](-level/-warn/index.md), [Level.Error](-level/-error/index.md), [Level.Assert](-level/-assert/index.md)).
- 
   Delegation to multiple logging trees ([Oak](-oak/index.md)) via the [OakWood](-oak-wood/index.md) singleton.
- 
   One-time [tag](-oak-wood/tag.md) and [quiet](-oak-wood/quiet.md) controls for flexible output.
- 
   Automatic message splitting if longer than MAX_LOG_LENGTH.
- 
   Simple string formatting (`%s`, `%d`), independent of platform.

## Example Usage

```kotlin
// Plant a custom Oak
Lumber.plant(ConsoleOak())

// Simple log
Lumber.info("User loaded")

// Formatted log
Lumber.debug("User %s took %dms to load", username, duration)

// Logging with exception
try {
    doFail()
} catch (ex: Exception) {
    Lumber.error(ex, "Something went wrong with user %s", username)
}

// One-time tag
Lumber.tag("Auth").warn("User session expired")

// Suppress one log
Lumber.quiet(true).debug("This will not be logged")
```

## Best Practices

- 
   Plant at least one [Oak](-oak/index.md) before logging, otherwise logs are ignored.
- 
   Use different [Oak](-oak/index.md) implementations for different outputs (console, file, remote).
- 
   Use `tag()` sparingly to annotate context; don’t overuse as it resets after one call.
- 
   Avoid expensive string concatenation; rely on the formatting mechanism.

#### Author

Matheus Corregiari

## Types

| Name | Summary |
|---|---|
| [Level](-level/index.md) | [common]<br>enum [Level](-level/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[Lumber.Level](-level/index.md)&gt; <br>Defines the severity level of a log message. |
| [Oak](-oak/index.md) | [common]<br>abstract class [Oak](-oak/index.md)<br>Base class for logging destinations (&quot;trees&quot;). Extend [Oak](-oak/index.md) to implement your own logging strategy: |
| [OakWood](-oak-wood/index.md) | [common]<br>object [OakWood](-oak-wood/index.md) : [Lumber.Oak](-oak/index.md)<br>Singleton dispatcher that holds and manages multiple [Oak](-oak/index.md) trees. All calls to [Lumber](index.md) are delegated here. |