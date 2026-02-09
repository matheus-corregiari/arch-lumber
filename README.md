# 🌲 Lumber – A Lightweight Logging Library for Kotlin Multiplatform

#### Version

[![Maven Central][badge-maven]][link-maven] · [![LICENSE][badge-license]][link-license]

---

#### Platforms

![Android][badge-android] · ![Apple][badge-apple] · ![JVM][badge-jvm] · ![JS][badge-js] · ![WASM][badge-wasm]

---

#### Quality

![Lint][badge-lint] · ![Test][badge-test] · ![Coverage][badge-coverage]

---

A flexible, type-safe, and multiplatform logging library inspired
by [Timber](https://github.com/JakeWharton/timber).  
Lumber brings a **simple API**, **prebuilt DebugOaks**, and **extensibility** for your Kotlin
Multiplatform projects.

To see more, take a look at the [documentation](/docs/api/lumber/index.md).

> Special thanks to Jake Wharton for the inspiration and making Timber the go-to logging library for
> Android.

---

## 📑 Table of Contents

* [Features](#-features)
* [Installation](#-installation)
* [Quick Start](#-quick-start)
* [Usage](#-usage)
    * [1. Plant a DebugOak](#1-plant-a-DebugOak)
    * [2. Log messages](#2-log-messages)
    * [3. Manage Oaks](#3-manage-oaks)
* [Prebuilt DebugOaks](#-prebuilt-oaks)
* [Custom Oaks](#-custom-oaks)
* [API Reference](#-api-reference)
* [Usability Tips](#-usability-tips)
* [Contributing](#-contributing)
* [License](#-license)

---

## 🏷️ Features

* **Kotlin Multiplatform Ready**
* **Multiple Log Levels** — `Verbose`, `Debug`, `Info`, `Warn`, `Error`, `Assert`.
* **Prebuilt DebugOaks** — Drop-in defaults per target:
    * Android → Logcat
    * Apple → ANSI colored `println`
    * JS/WASM → Native `console.*` (browser or Node.js/wasm runtime)
    * JVM → ANSI colored `println`
* **Composable System** — Plant one or many Oaks (`ConsoleOak`, `FileOak`, custom).
* **Thread-Safe** — Safe for concurrent logging.
* **Inspired by [Timber](https://github.com/JakeWharton/timber)** — Similar ergonomics, extended for
  KMP.

---

## 🚀 Installation

Add Lumber to your Gradle build:

```kotlin
// Kotlin DSL
dependencies {
    implementation("io.github.matheus-corregiari:arch-lumber:<latest-version>")
}
````

```groovy
// Groovy
dependencies {
    implementation 'io.github.matheus-corregiari:arch-lumber:<latest-version>'
}
````

---

## ⚡ Quick Start

1. Add the dependency to your shared (KMP) module.
2. Plant a `DebugOak()` once (usually during app startup).
3. Use `Lumber.*` wherever you need logging.

```kotlin
Lumber.plant(DebugOak())
Lumber.info("KMP logging ready")
```

---

## 💡 Usage

### 1. Plant a DebugOak

```kotlin
Lumber.plant(DebugOak())
```

### 2. Log messages

```kotlin
Lumber.verbose("Verbose details")
Lumber.debug("Debugging info")
Lumber.info("Initialization complete")
Lumber.warn("This might need attention")
Lumber.error(message = "Something failed!", error = Exception("Boom"))
Lumber.wtf("Critical failure!", Exception("Should never happen"))
```

### 3. Manage Oaks

```kotlin
val console = ConsoleOak()
Lumber.plant(console)

// Remove one oak
Lumber.uproot(console)

// Or clear all oaks
Lumber.uprootAll()
```

---

## 🌲 Prebuilt Oaks

| Target    | Implementation | Backend                              |
|-----------|----------------|--------------------------------------|
| Android   | `DebugOak`     | `android.util.Log` (Logcat)          |
| Apple     | `DebugOak`     | `println` with ANSI colors           |
| JS (IR)   | `DebugOak`     | Native `console.log/info/warn/error` |
| WASM (IR) | `DebugOak`     | Native `console.log/info/warn/error` |
| JVM       | `DebugOak`     | `println` with ANSI colors           |

---

## 🛠️ Custom Oaks

Extend `Lumber.Oak` for your own logging:

```kotlin
class ConsoleOak : Lumber.Oak() {
    override fun log(level: Level, tag: String?, message: String, error: Throwable?) {
        println("$level: [$tag] $message")
    }
}
```

Plant it like this:

```kotlin
Lumber.plant(ConsoleOak(), FileOak())
Lumber.debug("Logged to console and file")
```

---

## 📚 API Reference

### `Lumber` – static API

| Method                      | Description                                |
|-----------------------------|--------------------------------------------|
| `plant(vararg oaks: Oak)`   | Add logging oaks                           |
| `uproot(oak: Oak)`          | Remove a specific oak                      |
| `uprootAll()`               | Remove all oaks                            |
| `tag(tag: String)`          | Set a custom tag for the next log          |
| `quiet(enabled: Boolean)`   | Enable/disable quiet mode for the next log |
| `maxLogLength(length: Int)` | Set a custom log length for the next log   |
| `maxTagLength(length: Int)` | Set a custom tag length for the next log   |

### `Lumber.Level`

| Level   | Purpose                          |
|---------|----------------------------------|
| Verbose | Detailed logs, tracing/debugging |
| Debug   | Debugging information            |
| Info    | Informational messages           |
| Warn    | Warnings, potential issues       |
| Error   | Errors and exceptions            |
| Assert  | Critical failures (WTF)          |

---

## ✅ Usability Tips

* **No oaks, no logs:** If nothing is planted, logs are intentionally ignored.
* **One-shot settings:** `tag()`, `quiet()`, `maxLogLength()`, and `maxTagLength()` apply only to the *next* call.
* **Throwable output is platform-specific:** Android/JVM DebugOak do not print stack traces by default. Use a custom Oak
  if you want to include error output.

---

## 🤝 Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for a straightforward, KMP-focused workflow.

---

## 📄 License

This module is released under the **Apache 2.0 License**.
See [LICENSE](LICENSE) for details.

---

## Coverage detail charts

### Sunburst

> The inner-most circle is the entire project, moving away from the center are folders then,
> finally, a single file. The size and color of each slice is representing the number of statements
> and the coverage, respectively.
[![codecov](https://codecov.io/gh/matheus-corregiari/arch-lumber/graphs/sunburst.svg?token=P977R4GMUO)](https://codecov.io/gh/matheus-corregiari/arch-lumber)

### Grid

> Each block represents a single file in the project. The size and color of each block is
> represented by the number of statements and the coverage, respectively.
[![codecov](https://codecov.io/gh/matheus-corregiari/arch-lumber/graphs/tree.svg?token=P977R4GMUO)](https://codecov.io/gh/matheus-corregiari/arch-lumber)

### Icicle

> The top section represents the entire project. Proceeding with folders and finally individual
> files. The size and color of each slice is representing the number of statements and the coverage,
> respectively.
[![codecov](https://codecov.io/gh/matheus-corregiari/arch-lumber/graphs/icicle.svg?token=P977R4GMUO)](https://codecov.io/gh/matheus-corregiari/arch-lumber)

---

[link-maven]: https://search.maven.org/artifact/io.github.matheus-corregiari/arch-lumber

[link-ci]: https://github.com/matheus-corregiari/arch-lumber/actions/workflows/generate-tag.yml

[link-license]: /LICENSE

[badge-android]: http://img.shields.io/badge/-android-6EDB8D.svg?style=flat

[badge-apple]: http://img.shields.io/badge/-apple-000000.svg?style=flat

[badge-js]: http://img.shields.io/badge/-js-F7DF1E.svg?style=flat

[badge-wasm]: http://img.shields.io/badge/-wasm-654FF0.svg?style=flat

[badge-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg?style=flat

[badge-maven]: https://img.shields.io/maven-central/v/io.github.matheus-corregiari/arch-lumber.svg

[badge-license]: https://img.shields.io/github/license/matheus-corregiari/arch-lumber

[badge-coverage]: https://codecov.io/gh/matheus-corregiari/arch-lumber/branch/master/graph/badge.svg?token=P977R4GMUO

[badge-lint]: https://github.com/matheus-corregiari/arch-lumber/actions/workflows/lint.yml/badge.svg

[badge-test]: https://github.com/matheus-corregiari/arch-lumber/actions/workflows/coverage.yml/badge.svg
