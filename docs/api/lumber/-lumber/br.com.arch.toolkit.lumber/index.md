//[Lumber](../../index.md)/[br.com.arch.toolkit.lumber](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [DebugOak](-debug-oak/index.md) | [common]<br>expect open class [DebugOak](-debug-oak/index.md) : [Lumber.Oak](-lumber/-oak/index.md)<br>A platform-specific implementation of [Lumber.Oak](-lumber/-oak/index.md) designed for development logging.<br>[android]<br>actual open class [DebugOak](-debug-oak/index.md) : [Lumber.Oak](-lumber/-oak/index.md)<br>Android-specific implementation of [Lumber.Oak](-lumber/-oak/index.md), delegating logs to the [Android's Log](https://developer.android.com/reference/android/util/Log) framework.<br>[apple]<br>actual open class [DebugOak](-debug-oak/index.md) : Lumber.Oak<br>The Apple-specific implementation of [DebugOak](-debug-oak/index.md) (iOS, macOS, etc.) that logs messages to the standard output using `println` with ANSI color coding.<br>[js]<br>actual open class [DebugOak](-debug-oak/index.md) : Lumber.Oak<br>The JavaScript-specific implementation of [DebugOak](-debug-oak/index.md), which delegates all log messages to the native `console` object.<br>[jvm]<br>actual open class [DebugOak](-debug-oak/index.md) : Lumber.Oak<br>The JVM-specific implementation of [DebugOak](-debug-oak/index.md) that logs messages to the standard output (`stdout`) with ANSI color coding.<br>[wasmJs]<br>actual open class [DebugOak](-debug-oak/index.md) : Lumber.Oak<br>The WasmJS-specific implementation of [DebugOak](-debug-oak/index.md), which delegates all log messages to the native JavaScript `console` object via helper functions. |
| [Lumber](-lumber/index.md) | [common]<br>class [Lumber](-lumber/index.md)<br>A lightweight and extensible logging library for Kotlin Multiplatform, inspired by Timber. |