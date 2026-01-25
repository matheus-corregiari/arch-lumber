//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[quiet](quiet.md)

# quiet

[common]\
open override fun [quiet](quiet.md)(quiet: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [Lumber.Oak](../-oak/index.md)

Sets a one-time quiet flag to be used for the next logging call on all planted Oaks. This method propagates the quiet flag to every individual Oak managed by OakWood.

#### Return

The OakWood instance for method chaining.

## Example:

```kotlin
Lumber.quiet(true).error("This will not be logged")
// No output in quiet mode.
```

#### Parameters

common

| | |
|---|---|
| quiet | True to enable quiet mode for the next log call in all Oaks; false otherwise. |