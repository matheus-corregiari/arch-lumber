//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)/[isLoggable](is-loggable.md)

# isLoggable

[common]\
abstract fun [isLoggable](is-loggable.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, level: [Lumber.Level](../-level/index.md)): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

Determines whether a message should be logged.

Override this to implement custom filtering logic, such as logging only messages above a certain severity in production.

#### Return

`true` if the message should be logged, `false` otherwise.

#### Parameters

common

| | |
|---|---|
| tag | The tag associated with the message, or `null` if none was provided. |
| level | The severity [Level](../-level/index.md) of the message. |