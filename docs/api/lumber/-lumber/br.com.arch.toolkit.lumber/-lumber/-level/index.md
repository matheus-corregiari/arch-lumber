//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Level](index.md)

# Level

[common]\
enum [Level](index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[Lumber.Level](index.md)&gt; 

Defines the severity level of a log message.

Each level indicates intent and should be used consistently across the app:

- 
   [Verbose](-verbose/index.md) → **lowest priority**. Use for extremely detailed logs (e.g., internal variable dumps, step-by-step flows).

```kotlin
Lumber.verbose("Loop iteration=%d, value=%s", i, value)
```

- 
   [Debug](-debug/index.md) → Developer-facing messages useful during development but usually turned off in production.

```kotlin
Lumber.debug("Loaded user profile for %s", userId)
```

- 
   [Info](-info/index.md) → High-level events that describe the normal flow of the app. Safe to keep enabled in production.

```kotlin
Lumber.info("App started in %d ms", startupTime)
```

- 
   [Warn](-warn/index.md) → Something unexpected happened or a potential issue was detected, but the app is still working correctly.

```kotlin
Lumber.warn("Cache miss for key=%s, falling back to network", key)
```

- 
   [Error](-error/index.md) → A failure occurred that needs attention, usually accompanied by an exception.

```kotlin
try {
    fetchData()
} catch (ex: IOException) {
    Lumber.error(ex, "Network request failed")
}
```

- 
   [Assert](-assert/index.md) → Highest priority. Use for conditions that should **never** happen (fatal errors, invariant violations).

```kotlin
checkNotNull(user) ?: Lumber.wtf("User must not be null here!")
```

## Entries

| | |
|---|---|
| [Verbose](-verbose/index.md) | [common]<br>[Verbose](-verbose/index.md)<br>Extremely detailed logging, usually disabled in production. |
| [Debug](-debug/index.md) | [common]<br>[Debug](-debug/index.md)<br>Debug information for developers, disabled in release builds. |
| [Info](-info/index.md) | [common]<br>[Info](-info/index.md)<br>General information about app state and high-level events. |
| [Warn](-warn/index.md) | [common]<br>[Warn](-warn/index.md)<br>Something unexpected but not fatal. |
| [Error](-error/index.md) | [common]<br>[Error](-error/index.md)<br>Recoverable or unrecoverable errors, often with exceptions. |
| [Assert](-assert/index.md) | [common]<br>[Assert](-assert/index.md)<br>Critical failures that should never happen (What a Terrible Failure) |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [common]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.enums/-enum-entries/index.html)&lt;[Lumber.Level](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](-assert/index.md#-372974862%2FProperties%2F1314131990) | [common]<br>expect val [name](-assert/index.md#-372974862%2FProperties%2F1314131990): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](-assert/index.md#-739389684%2FProperties%2F1314131990) | [common]<br>expect val [ordinal](-assert/index.md#-739389684%2FProperties%2F1314131990): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Lumber.Level](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[Lumber.Level](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |