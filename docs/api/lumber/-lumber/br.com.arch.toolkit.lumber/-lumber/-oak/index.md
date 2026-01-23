//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)

# Oak

abstract class [Oak](index.md)

# Oak - Abstract Logging Tree

Base class for logging destinations (&quot;oaks&quot;). Extend [Oak](index.md) to implement your own logging strategy:

- 
   console printing,
- 
   file logging,
- 
   network / crash reporting,
- 
   etc.

## Example

```kotlin
class ConsoleOak : Lumber.Oak() {
    override fun isLoggable(tag: String?, level: Level) = true
    override fun log(level: Level, tag: String?, message: String, error: Throwable?) {
        println("$level [$tag]: $message")
        error?.printStackTrace()
    }
}

Lumber.plant(ConsoleOak())
Lumber.debug("Debugging with %s", "Lumber")
```

#### See also

| | |
|---|---|
| [Lumber.OakWood](../-oak-wood/index.md) | for the singleton dispatcher that manages multiple [Oak](index.md) instances. |

#### Inheritors

| |
|---|
| [OakWood](../-oak-wood/index.md) |
| [DebugOak](../../-debug-oak/index.md) |

## Constructors

| | |
|---|---|
| [Oak](-oak.md) | [common]<br>constructor() |

## Functions

| Name | Summary |
|---|---|
| [debug](debug.md) | [common]<br>open fun [debug](debug.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Log a [Level.Debug](../-level/-debug/index.md) exception only.<br>[common]<br>open fun [debug](debug.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Debug](../-level/-debug/index.md) message.<br>[common]<br>open fun [debug](debug.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Debug](../-level/-debug/index.md) exception with message. |
| [error](error.md) | [common]<br>open fun [error](error.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Log a [Level.Error](../-level/-error/index.md) exception only.<br>[common]<br>open fun [error](error.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Error](../-level/-error/index.md) message.<br>[common]<br>open fun [error](error.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Error](../-level/-error/index.md) exception with message. |
| [info](info.md) | [common]<br>open fun [info](info.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Log a [Level.Info](../-level/-info/index.md) exception only.<br>[common]<br>open fun [info](info.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Info](../-level/-info/index.md) message.<br>[common]<br>open fun [info](info.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Info](../-level/-info/index.md) exception with message. |
| [log](log.md) | [common]<br>open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Log an exception only.<br>[common]<br>open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a message at [level](log.md) with optional formatting args.<br>[common]<br>open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?, message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log exception + optional message with args. |
| [maxLogLength](max-log-length.md) | [common]<br>open fun [maxLogLength](max-log-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](index.md)<br>Sets a one-time max log length to be used for the next logging call on this specific [Oak](index.md). When enabled, some loggers might skip logging the message based on their implementation. The quiet flag is temporary and only affects the immediate next log message. |
| [maxTagLength](max-tag-length.md) | [common]<br>open fun [maxTagLength](max-tag-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](index.md)<br>Sets a one-time max tag length to be used for the next logging call on this specific [Oak](index.md). When enabled, some loggers might skip logging the message based on their implementation. The quiet flag is temporary and only affects the immediate next log message. |
| [quiet](quiet.md) | [common]<br>open fun [quiet](quiet.md)(quiet: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [Lumber.Oak](index.md)<br>Sets a one-time quiet flag to be used for the next logging call on this specific [Oak](index.md). When enabled, some loggers might skip logging the message based on their implementation. The quiet flag is temporary and only affects the immediate next log message. |
| [tag](tag.md) | [common]<br>open fun [tag](tag.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Lumber.Oak](index.md)<br>Sets a one-time tag to be used for the next logging call on this specific [Oak](index.md). This tag helps identify the source of the log, making it easier to trace. The tag is temporary and only affects the immediate next log message. |
| [verbose](verbose.md) | [common]<br>open fun [verbose](verbose.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Log a [Level.Verbose](../-level/-verbose/index.md) exception only.<br>[common]<br>open fun [verbose](verbose.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Verbose](../-level/-verbose/index.md) message.<br>[common]<br>open fun [verbose](verbose.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Verbose](../-level/-verbose/index.md) exception with message. |
| [warn](warn.md) | [common]<br>open fun [warn](warn.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Log a [Level.Warn](../-level/-warn/index.md) exception only.<br>[common]<br>open fun [warn](warn.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Warn](../-level/-warn/index.md) message.<br>[common]<br>open fun [warn](warn.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Warn](../-level/-warn/index.md) exception with message. |
| [wtf](wtf.md) | [common]<br>open fun [wtf](wtf.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Log a [Level.Assert](../-level/-assert/index.md) exception only.<br>[common]<br>open fun [wtf](wtf.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Assert](../-level/-assert/index.md) message.<br>[common]<br>open fun [wtf](wtf.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Log a [Level.Assert](../-level/-assert/index.md) exception with message. |