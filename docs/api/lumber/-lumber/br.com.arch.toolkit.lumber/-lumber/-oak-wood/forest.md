//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[forest](forest.md)

# forest

[common]\
fun [forest](forest.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Lumber.Oak](../-oak/index.md)&gt;

Returns a copy of all planted trees (Oaks). This can be useful for iterating or debugging.

#### Return

A list of all planted Oaks.

## Example:

```kotlin
val forest = Lumber.forest() // Get all planted Oaks
forest.forEach { oak -> oak.debug("Inspecting Oak: ${oak.javaClass.simpleName}") }
```