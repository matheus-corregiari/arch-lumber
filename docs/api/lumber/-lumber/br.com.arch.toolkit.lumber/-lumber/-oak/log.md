//[Lumber](../../../../index.md)/[br.com.arch.toolkit.lumber](../../index.md)/[Lumber](../index.md)/[Oak](index.md)/[log](log.md)

# log

[common]\
open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)

Log a message at [level](log.md) with optional formatting args.

[common]\
open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html))

Log an exception only.

[common]\
open fun [log](log.md)(level: [Lumber.Level](../-level/index.md), error: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)?, message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)

Log exception + optional message with args.