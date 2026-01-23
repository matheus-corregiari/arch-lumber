//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[tag](tag.md)

# tag

[common]\
open override fun [tag](tag.md)(tag: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Lumber.Oak](../-oak/index.md)

Sets a one-time tag to be used for the next logging call on all planted Oaks. This method propagates the tag to every individual Oak managed by OakWood. The tag is stored in each Oak using a thread-local to ensure thread safety and is cleared after the log call.

#### Return

The OakWood instance for method chaining.

## Example:

```kotlin
Lumber.tag("MyActivity").debug("Debug message")
// Expected output for ConsoleOak: Debug: [MyActivity] Debug message
// Expected output for FileOak: Debug: [MyActivity] Debug message
```

#### Parameters

common

| | |
|---|---|
| tag | The tag to attach to the next log message for all Oaks. |