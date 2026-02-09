//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[maxTagLength](max-tag-length.md)

# maxTagLength

[common]\
open override fun [maxTagLength](max-tag-length.md)(length: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Lumber.Oak](../-oak/index.md)

Sets a one-time maximum length for the tag on the next log message.

If the tag exceeds this length, it will be truncated.

#### Return

The current `Oak` instance for chaining.

#### Parameters

common

| | |
|---|---|
| length | The maximum length for the tag. Must be positive. |