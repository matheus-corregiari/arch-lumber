//[Lumber](../../../index.md)/[br.com.arch.toolkit.lumber](../index.md)/[Lumber](index.md)

# Lumber

class [Lumber](index.md)

# 🌲 Lumber

A lightweight and extensible logging library for Kotlin Multiplatform, inspired by Timber.

## Core Concepts

- 
   [**Lumber**](index.md): The main entry point for all logging operations. It provides a static-like API for convenience.
- 
   [**Oak**](-oak/index.md): An abstraction for a logging destination. You can implement your own `Oak` to send logs to files, analytics services, or any other target.
- 
   [**DebugOak**](../-debug-oak/index.md): A pre-built `Oak` for platform-specific default logging (e.g., Logcat on Android, `println` on JVM).
- 
   [**plant**](-oak-wood/plant.md): Registers an `Oak` instance to receive log messages.
- 
   [**uproot**](-oak-wood/uproot.md): Removes a previously planted `Oak`.

## Quick Start

1. 
   **Plant an Oak:** In your application's entry point, plant a `DebugOak` or a custom one.
   
   ```kotlin
    Lumber.plant(DebugOak())
   ```
2. 
   **Log messages:** Use the static methods to log messages at different levels.
   
   ```kotlin
    Lumber.info("Application started")
    Lumber.debug("User ID: %s", "12345")
    Lumber.error(Exception("Something went wrong"), "Failed to load data")
   ```

## Advanced Usage

- 
   **Custom Tags:** Use `tag()` for one-time contextual tags.

```kotlin
Lumber.tag("Authentication").warn("Login failed for user: %s", "john.doe")
```

- 
   **Multiple Oaks:** Plant multiple `Oak`s to direct logs to different destinations simultaneously.

```kotlin
Lumber.plant(DebugOak(), FileOak("path/to/logfile.log"))
```

#### See also

| |
|---|
| [Lumber.Oak](-oak/index.md) |
| [DebugOak](../-debug-oak/index.md) |
| [Lumber.Level](-level/index.md) |

## Types

| Name | Summary |
|---|---|
| [Level](-level/index.md) | [common]<br>enum [Level](-level/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[Lumber.Level](-level/index.md)&gt; <br>Defines the severity level of a log message. |
| [Oak](-oak/index.md) | [common]<br>abstract class [Oak](-oak/index.md)<br>An `Oak` represents a single logging destination. |
| [OakWood](-oak-wood/index.md) | [common]<br>object [OakWood](-oak-wood/index.md) : [Lumber.Oak](-oak/index.md)<br>The central dispatcher that manages all planted `Oak`s. |