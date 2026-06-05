# Core Concepts

## Lumber

`Lumber` is the static-style entry point.

```kotlin
Lumber.info("Ready")
```

## Oak

`Lumber.Oak` is the extension point for custom logging backends.

```kotlin
class ConsoleOak : Lumber.Oak() {
    override fun isLoggable(tag: String?, level: Lumber.Level) = true

    override fun log(level: Lumber.Level, tag: String?, message: String, error: Throwable?) {
        println("[$level] ${tag ?: "-"} $message")
        error?.printStackTrace()
    }
}
```

## DebugOak

`DebugOak` is the built-in platform oak.

- Android routes to `android.util.Log`
- JVM prints colored lines to stdout
- Apple prints colored lines to stdout
- JS and WasmJS use the native console

## Tagged facades

`Lumber.tag(...)` returns a lightweight facade that keeps the tag across calls.

```kotlin
val authLog = Lumber.tag("Auth")

authLog.info("Session created")
authLog.info("Session refreshed")
```

## One-shot options

These values are consumed after the next log call:

- `quiet(...)`
- `maxLogLength(...)`
- `maxTagLength(...)`

That behavior keeps temporary logging options local and predictable.

## Forest

`Lumber.plant(...)`, `Lumber.uproot(...)`, and `Lumber.uprootAll()` manage the active oaks.

```kotlin
Lumber.plant(DebugOak())
Lumber.uprootAll()
```
