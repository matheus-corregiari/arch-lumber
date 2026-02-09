//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[maxLogLength](max-log-length.md)

# maxLogLength

[common]\
open override fun [maxLogLength](max-log-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](../-oak/index.md)

Sets a one-time maximum length for the next log message.

If a formatted message exceeds this length, it will be split into multiple chunks.

#### Return

The current `Oak` instance for chaining.

#### Parameters

common

| | |
|---|---|
| length | The maximum number of characters per log entry. Must be positive. |