//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[maxLogLength](max-log-length.md)

# maxLogLength

[common]\
open override fun [maxLogLength](max-log-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](../-oak/index.md)

Sets a one-time max log length to be used for the next logging call on all planted Oaks. This method propagates the max log length to every individual Oak managed by OakWood. The flag is stored using a thread-local to ensure thread safety and is cleared after the log call.

#### Return

The OakWood instance for method chaining.

## Example:

```kotlin
Lumber.maxLogLength(10).tag("Tag").debug("Debug message")
// Expected output for ConsoleOak:
// Debug: [Tag #0] Debug mess
// Debug: [Tag #1] age

// Expected output for FileOak:
// Debug: [Tag #0] Debug mess
// Debug: [Tag #1] age
```

#### Parameters

common

| | |
|---|---|
| length | The maximum length of the log message for all Oaks. |