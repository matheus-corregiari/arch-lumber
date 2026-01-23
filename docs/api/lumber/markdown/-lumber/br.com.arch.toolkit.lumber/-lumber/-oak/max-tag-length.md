//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)/[maxTagLength](max-tag-length.md)

# maxTagLength

[common]\
open fun [maxTagLength](max-tag-length.md)(maxTagLength: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](index.md)

Sets a one-time max log length to be used for the next logging call on this specific [Oak](index.md). When enabled, some loggers might skip logging the message based on their implementation. The quiet flag is temporary and only affects the immediate next log message.

The flag is stored using a ThreadSafe to ensure it is only applied for the current thread and is cleared automatically after the log call.

#### Return

The [Oak](index.md) instance for method chaining.

## Example:

```kotlin
Lumber.maxTagLength(3).tag("LongTag").debug("Debug message");
// Expected output:
Debug: [Lon] Debug message
```

#### Parameters

common

| | |
|---|---|
| quiet | True to enable quiet mode for the next log call; false otherwise. |