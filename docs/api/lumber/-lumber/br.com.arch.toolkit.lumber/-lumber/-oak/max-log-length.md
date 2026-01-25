//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)/[maxLogLength](max-log-length.md)

# maxLogLength

[common]\
open fun [maxLogLength](max-log-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](index.md)

Sets a one-time max log line length to be used for the next logging call on this specific [Oak](index.md).

This does **not** drop/omit the log. If the final formatted message exceeds [length](max-log-length.md), Lumber will **split the message into multiple log entries**, each with at most [length](max-log-length.md) characters.

### One-shot behavior

This setting is **consumed on the next log call** (including when the message is not loggable), and then cleared automatically for the current thread.

### Tag suffix for split parts

When splitting happens, Lumber appends a suffix to the tag:

- 
   `"$tag #0"`, `"$tag #1"`, ...
- 
   If the tag is null, it becomes `"#0"`, `"#1"`, ...

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
| length | Maximum number of characters per emitted log entry. Must be > 0. |