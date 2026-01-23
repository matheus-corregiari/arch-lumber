//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)/[maxLogLength](max-log-length.md)

# maxLogLength

[common]\
open fun [maxLogLength](max-log-length.md)(maxLogLength: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](index.md)

Sets a one-time max log length to be used for the next logging call on this specific [Oak](index.md). When enabled, some loggers might skip logging the message based on their implementation. The quiet flag is temporary and only affects the immediate next log message.

The flag is stored using a ThreadSafe to ensure it is only applied for the current thread and is cleared automatically after the log call.

#### Return

The [Oak](index.md) instance for method chaining.

## Example:

```kotlin
Lumber.maxLogLength(10).tag("Tag").debug("Debug message");
// Expected output:
Debug: [Tag #0] Debug mess
Debug: [Tag #1] age
```

#### Parameters

common

| | |
|---|---|
| maxLogLength | The maximum length of the log message. |