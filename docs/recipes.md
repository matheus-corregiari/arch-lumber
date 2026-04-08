# Usage Recipes

## Structured logging

```kotlin
Lumber.tag("Sync").info("Synced %d records", count)
```

## Log exceptions with context

```kotlin
runCatching { sync() }
    .onFailure { error ->
        Lumber.error(error, "Sync failed for %s", accountId)
    }
```

## Split long messages

```kotlin
Lumber.maxLogLength(120).debug(longMessage)
```

## Use a filtered oak

```kotlin
class AnalyticsSink {
    fun write(level: Lumber.Level, tag: String?, message: String, error: Throwable?) {
        Analytics.track(level.name, tag, message, error)
    }
}

class WarningsOnlyOak : Lumber.Oak() {
    private val sink = AnalyticsSink()

    override fun isLoggable(tag: String?, level: Lumber.Level) = level >= Lumber.Level.Warn

    override fun log(level: Lumber.Level, tag: String?, message: String, error: Throwable?) {
        sink.write(level, tag, message, error)
    }
}
```

## Plant more than one oak

```kotlin
class AnalyticsOak : Lumber.Oak() {
    override fun isLoggable(tag: String?, level: Lumber.Level) = level >= Lumber.Level.Info

    override fun log(level: Lumber.Level, tag: String?, message: String, error: Throwable?) {
        Analytics.track(level.name, tag, message, error)
    }
}

Lumber.plant(DebugOak(), AnalyticsOak())
```

## Keep the public API small

Use `internal` for helpers that are implementation detail only.

That keeps the published contract focused on `Lumber`, `Lumber.Oak`, `Lumber.Level`, and `DebugOak`.
