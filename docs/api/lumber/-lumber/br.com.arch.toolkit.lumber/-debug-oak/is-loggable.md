//[Lumber](../../../index.md)/[br.com.arch.toolkit.lumber](../index.md)/[DebugOak](index.md)/[isLoggable](is-loggable.md)

# isLoggable

[common]\
expect open override fun [isLoggable](is-loggable.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, level: [Lumber.Level](../-lumber/-level/index.md)): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

Determines whether a log message should be output.

The implementation of this method is platform-dependent. For example, on Android, it might check the system log level.

#### Return

`true` if the message should be logged, `false` otherwise.

#### Parameters

common

| | |
|---|---|
| tag | The tag associated with the log message. |
| level | The severity [Lumber.Level](../-lumber/-level/index.md). |

android

| | |
|---|---|
| tag | Optional tag, can be `null` (Android will use `"null"`). |
| level | The logging level. |

[android]\
actual open override fun [isLoggable](is-loggable.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, level: [Lumber.Level](../-lumber/-level/index.md)): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

Determines whether a log at the given `level` should be printed.

Delegates to `Log.isLoggable`, mapping [Lumber.Level](../-lumber/-level/index.md) to the corresponding Android priority:

- 
   [Lumber.Level.Verbose](../-lumber/-level/-verbose/index.md) → `Log.VERBOSE`
- 
   [Lumber.Level.Debug](../-lumber/-level/-debug/index.md)   → `Log.DEBUG`
- 
   [Lumber.Level.Info](../-lumber/-level/-info/index.md)    → `Log.INFO`
- 
   [Lumber.Level.Warn](../-lumber/-level/-warn/index.md)    → `Log.WARN`
- 
   [Lumber.Level.Error](../-lumber/-level/-error/index.md)   → `Log.ERROR`
- 
   [Lumber.Level.Assert](../-lumber/-level/-assert/index.md)  → `Log.ASSERT`

#### Return

`true` if Android allows logging at this level, `false` otherwise.

#### Parameters

common

| | |
|---|---|
| tag | The tag associated with the log message. |
| level | The severity [Lumber.Level](../-lumber/-level/index.md). |

android

| | |
|---|---|
| tag | Optional tag, can be `null` (Android will use `"null"`). |
| level | The logging level. |

[apple]\
actual open override fun [isLoggable](is-loggable.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, level: Lumber.Level): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

For Apple platforms, all log levels are considered loggable by default.

#### Return

Always `true`.

[js]\
actual open override fun [isLoggable](is-loggable.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, level: Lumber.Level): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

For JS, all log levels are considered loggable by default.

#### Return

Always `true`.

[jvm]\
actual open override fun [isLoggable](is-loggable.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, level: Lumber.Level): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

For the JVM implementation, all log levels are considered loggable by default.

#### Return

Always `true`.

[wasmJs]\
actual open override fun [isLoggable](is-loggable.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, level: Lumber.Level): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

For WasmJS, all log levels are considered loggable by default.

#### Return

Always `true`.