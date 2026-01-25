//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[OakWood](index.md)/[plant](plant.md)

# plant

[common]\
fun [plant](plant.md)(tree: [Lumber.Oak](../-oak/index.md), vararg trees: [Lumber.Oak](../-oak/index.md)): [Lumber.OakWood](index.md)

Plants new logging trees into the forest. Accepts one or more Oak instances and adds them to the logging system.

#### Parameters

common

| | |
|---|---|
| tree | Oak instance to be added to the forest. |
| trees | An array of Oak instances to be added to the forest. |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if trying to plant the same OakWood instance.<br>Example:<br>```kotlin val consoleOak = ConsoleOak() // A custom Oak implementation val fileOak = FileOak() // Another custom Oak implementation Lumber.plant(consoleOak, fileOak) // Adds both Oaks to the forest Lumber.debug("Message to consoleOak and fileOak") ``` |