//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[plant](plant.md)

# plant

[common]\
fun [plant](plant.md)(tree: [Lumber.Oak](../-oak/index.md), vararg trees: [Lumber.Oak](../-oak/index.md)): [Lumber.OakWood](index.md)

Adds one or more `Oak`s to the logging system.

#### Parameters

common

| | |
|---|---|
| tree | The first `Oak` to plant. |
| trees | Additional `Oak`s to plant. |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if `OakWood` itself is planted. |