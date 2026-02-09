//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)

# Oak

abstract class [Oak](index.md)

# Oak

An `Oak` represents a single logging destination.

Extend this abstract class to create a custom logger, whether it sends logs to the console, a file, a remote server, or an analytics service.

## Implementing a Custom Oak

You need to implement two methods:

- 
   [**isLoggable**](is-loggable.md): Determines if a message with a given `level` and `tag` should be logged.
- 
   [**log**](log.md): Performs the actual logging of the formatted message.

### Example

```kotlin
class ConsoleOak : Lumber.Oak() {
    override fun isLoggable(tag: String?, level: Level) = true

    override fun log(level: Level, tag: String?, message: String, error: Throwable?) {
        println("[$level] ${tag?.let { "($it) " } ?: ""} - $message")
        error?.printStackTrace()
    }
}

// Plant it
Lumber.plant(ConsoleOak())
```

#### See also

| |
|---|
| [Lumber.OakWood](../-oak-wood/index.md) |
| [DebugOak](../../-debug-oak/index.md) |

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
| [debug](debug.md) | [common]<br>open fun [debug](debug.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Logs a [Debug](../-level/-debug/index.md) error.<br>[common]<br>open fun [debug](debug.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs a [Debug](../-level/-debug/index.md) message.<br>[common]<br>open fun [debug](debug.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs a [Debug](../-level/-debug/index.md) error with a message. |
| [error](error.md) | [common]<br>open fun [error](error.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Logs an [Error](../-level/-error/index.md) error.<br>[common]<br>open fun [error](error.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs an [Error](../-level/-error/index.md) message.<br>[common]<br>open fun [error](error.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs an [Error](../-level/-error/index.md) error with a message. |
| [info](info.md) | [common]<br>open fun [info](info.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Logs an [Info](../-level/-info/index.md) error.<br>[common]<br>open fun [info](info.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs an [Info](../-level/-info/index.md) message.<br>[common]<br>open fun [info](info.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs an [Info](../-level/-info/index.md) error with a message. |
| [isLoggable](is-loggable.md) | [common]<br>abstract fun [isLoggable](is-loggable.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, level: [Lumber.Level](../-level/index.md)): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Determines whether a message should be logged. |
| [log](log.md) | [common]<br>open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Logs an error with a specific [Level](../-level/index.md).<br>[common]<br>open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs a message with a specific [Level](../-level/index.md) and optional arguments.<br>[common]<br>open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?, message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>The most generic log method, handling all parameters. |
| [maxLogLength](max-log-length.md) | [common]<br>open fun [maxLogLength](max-log-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](index.md)<br>Sets a one-time maximum length for the next log message. |
| [maxTagLength](max-tag-length.md) | [common]<br>open fun [maxTagLength](max-tag-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](index.md)<br>Sets a one-time maximum length for the tag on the next log message. |
| [quiet](quiet.md) | [common]<br>open fun [quiet](quiet.md)(quiet: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [Lumber.Oak](index.md)<br>Suppresses the next log message for this `Oak`. |
| [tag](tag.md) | [common]<br>open fun [tag](tag.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Lumber.Oak](index.md)<br>Sets a one-time tag for the next log message. |
| [verbose](verbose.md) | [common]<br>open fun [verbose](verbose.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Logs a [Verbose](../-level/-verbose/index.md) error.<br>[common]<br>open fun [verbose](verbose.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs a [Verbose](../-level/-verbose/index.md) message.<br>[common]<br>open fun [verbose](verbose.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs a [Verbose](../-level/-verbose/index.md) error with a message. |
| [warn](warn.md) | [common]<br>open fun [warn](warn.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Logs a [Warn](../-level/-warn/index.md) error.<br>[common]<br>open fun [warn](warn.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs a [Warn](../-level/-warn/index.md) message.<br>[common]<br>open fun [warn](warn.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs a [Warn](../-level/-warn/index.md) error with a message. |
| [wtf](wtf.md) | [common]<br>open fun [wtf](wtf.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))<br>Logs an [Assert](../-level/-assert/index.md) error.<br>[common]<br>open fun [wtf](wtf.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs an [Assert](../-level/-assert/index.md) message.<br>[common]<br>open fun [wtf](wtf.md)(error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Logs an [Assert](../-level/-assert/index.md) error with a message. |