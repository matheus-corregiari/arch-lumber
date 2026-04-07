# Getting Started

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

Use `tag()` for a one-shot tag on the next log call.

```kotlin
Lumber.tag("Auth").info("Session created")
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

## What to read next

- [Core Concepts](core-concepts.md)
- [Usage Recipes](recipes.md)
- [API Reference](api/lumber/index.md)
