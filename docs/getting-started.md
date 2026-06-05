# Getting Started

## Requirements

- JDK `21` from the project toolchain
- Gradle wrapper `9.5.1`
- Kotlin `2.4.0`
- Android `minSdk 20` and `compileSdk 36` for Android consumers

## Add the dependency

```kotlin
dependencies {
    implementation("io.github.matheus-corregiari:arch-lumber:<latest>")
}
```

## Plant a default oak

`DebugOak` gives you the default platform behavior.

```kotlin
Lumber.plant(DebugOak())
```

## Log something

```kotlin
Lumber.verbose("Trace message")
Lumber.debug("User %s signed in", userName)
Lumber.info("Request finished in %d ms", elapsedMs)
Lumber.warn("Cache miss for %s", key)
Lumber.error(exception, "Unexpected failure")
Lumber.wtf("This should never happen")
```

## Add context

Use `tag()` to create a lightweight logging facade that keeps the same tag across calls.

```kotlin
val authLog = Lumber.tag("Auth")

authLog.info("Session created")
authLog.info("Session refreshed")
```

## Use your own oak

```kotlin
class AnalyticsOak : Lumber.Oak() {
    override fun isLoggable(tag: String?, level: Lumber.Level) = level >= Lumber.Level.Info

    override fun log(level: Lumber.Level, tag: String?, message: String, error: Throwable?) {
        Analytics.track(level.name, tag, message, error)
    }
}

Lumber.plant(AnalyticsOak())
```

## Compatibility notes

- Android support starts at `minSdk 20`.
- The build uses the Gradle toolchain with Foojay resolution for JDK `21`.
- The published API follows the same KDoc and Dokka HTML reference as the source code.
- If setup, platform behavior, or API shape changes, update this guide together with the generated API reference.

## What to read next

- [Core Concepts](core-concepts.md)
- [Usage Recipes](recipes.md)
- [API Reference](https://matheus-corregiari.github.io/arch-lumber/api/)
