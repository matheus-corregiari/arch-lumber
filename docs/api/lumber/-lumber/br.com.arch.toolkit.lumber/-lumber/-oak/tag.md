//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)/[tag](tag.md)

# tag

[common]\
open fun [tag](tag.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Lumber.Oak](index.md)

Sets a one-time tag to be used for the next logging call on this specific [Oak](index.md). This tag helps identify the source of the log, making it easier to trace. The tag is temporary and only affects the immediate next log message.

The tag is stored using a ThreadSafe to ensure it is only applied for the current thread and is cleared automatically after the log call.

#### Return

The [Oak](index.md) instance for method chaining.

## Example:

```kotlin
Lumber.tag("MyActivity").debug("Debug message")
// Expected output: Debug: [MyActivity] Debug message
```

#### Parameters

common

| | |
|---|---|
| tag | The tag to attach to the next log message. |