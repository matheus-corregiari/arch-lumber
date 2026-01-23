//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[maxTagLength](max-tag-length.md)

# maxTagLength

[common]\
open override fun [maxTagLength](max-tag-length.md)(maxTagLength: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](../-oak/index.md)

Sets a one-time max tag length to be used for the next logging call on all planted Oaks. This method propagates the max tag length to every individual Oak managed by OakWood. The flag is stored using a thread-local to ensure thread safety and is cleared after the log call.

#### Return

The OakWood instance for method chaining.

## Example:

```kotlin
Lumber.maxTagLength(3).tag("LongTag").debug("Debug message")
// Expected output for ConsoleOak:
// Debug: [Lon] Debug message

// Expected output for FileOak:
// Debug: [Lon] Debug message
```

#### Parameters

common

| | |
|---|---|
| maxTagLength | The maximum length of the tag for all Oaks. |