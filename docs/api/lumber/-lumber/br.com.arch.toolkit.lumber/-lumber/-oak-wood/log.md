//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[log](log.md)

# log

[common]\
open override fun [log](log.md)(level: [Lumber.Level](../-level/index.md), error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?, message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)

Dispatches the log message to all planted Oaks.

#### Parameters

common

| | |
|---|---|
| level | The logging level for the message. |
| message | The log message. |
| error | An optional throwable to be logged.<br>Example:<br>```kotlin // Assume two Oaks: consoleOak and fileOak are planted Lumber.debug(message = "Debug message", error = Exception("Example Exception")) // ConsoleOak: Debug message // FileOak: Debug message ``` |