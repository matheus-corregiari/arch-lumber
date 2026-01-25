//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)/[maxTagLength](max-tag-length.md)

# maxTagLength

[common]\
open fun [maxTagLength](max-tag-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](index.md)

Sets a one-time max tag length to be used for the next logging call on this specific [Oak](index.md).

If the tag exceeds [length](max-tag-length.md), Lumber will **truncate** it to at most [length](max-tag-length.md) characters (internally it takes only the first chunk).

### One-shot behavior

This setting is **consumed on the next log call** (including when the message is not loggable), and then cleared automatically for the current thread.

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
| length | The maximum length of the tag. Must be > 0. |