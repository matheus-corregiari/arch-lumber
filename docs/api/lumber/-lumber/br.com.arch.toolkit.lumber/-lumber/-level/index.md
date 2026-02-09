//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Level](index.md)

# Level

[common]\
enum [Level](index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[Lumber.Level](index.md)&gt; 

Defines the severity level of a log message.

Each level provides a clear indication of the log's importance and is used by `Oak` implementations to filter and format messages appropriately.

## Entries

| | |
|---|---|
| [Verbose](-verbose/index.md) | [common]<br>[Verbose](-verbose/index.md)<br>For detailed, fine-grained debugging information. Typically disabled in production. |
| [Debug](-debug/index.md) | [common]<br>[Debug](-debug/index.md)<br>For developer-facing messages to debug application flow. |
| [Info](-info/index.md) | [common]<br>[Info](-info/index.md)<br>For high-level events that mark the application's lifecycle. |
| [Warn](-warn/index.md) | [common]<br>[Warn](-warn/index.md)<br>For potential issues or unexpected events that do not halt execution. |
| [Error](-error/index.md) | [common]<br>[Error](-error/index.md)<br>For errors and exceptions that impact functionality but may be recoverable. |
| [Assert](-assert/index.md) | [common]<br>[Assert](-assert/index.md)<br>For critical, unrecoverable failures. Stands for &quot;What a Terrible Failure.&quot; |

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