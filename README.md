# 🌲 Lumber

**A Lightweight, Type-Safe Logging Library for Kotlin Multiplatform.**

[![Maven Central][badge-maven]][link-maven]
[![License][badge-license]](/LICENSE)
[![Kotlin][badge-kotlin]](https://kotlinlang.org)
![Lint][badge-lint]
![Test][badge-test]
[![Coverage][badge-coverage]][link-coverage]

---

Lumber is a modern logging library for Kotlin Multiplatform (KMP) inspired by the simplicity
of [Timber][link-timber]. It provides a clean, idiomatic API for logging across all your KMP targets
with zero boilerplate.

## ✨ Features

- **🚀 KMP First**: Designed from the ground up for Kotlin Multiplatform.
- **🛠️ Prebuilt DebugOaks**: Drop-in defaults for Android, iOS/Apple, JVM, JS, and Wasm.
- **🎨 ANSI Color Support**: Beautifully colored logs in your terminal for JVM and Apple targets.
- **🧩 Extensible Architecture**: Easily create custom "Oaks" to redirect logs to any destination (
  files, crash reporting, etc.).
- **🧵 Thread-Safe**: Built-in protection for concurrent logging.
- **📏 Smart Formatting**: Support for simple string formatting (`%s`, `%d`) across all platforms.
- **✂️ Automatic Splitting**: Large log messages are automatically split into manageable chunks.

## 📦 Installation

Add the dependency to your `commonMain` source set:

```kotlin
// build.gradle.kts
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.github.matheus-corregiari:arch-lumber:<latest-version>")
            }
        }
    }
}
```

## 🛠️ Usage

### 1. Plant an Oak

Before logging, you need to "plant" at least one `Oak`. A `DebugOak` is provided for standard
platform logging.

```kotlin
// In your platform-specific entry point (e.g., Application.onCreate, main)
Lumber.plant(DebugOak())
```

### 2. Log Away

Use the `Lumber` static API to log at various levels.

```kotlin
Lumber.verbose("Detailed trace information")
Lumber.debug("User %s has logged in", username)
Lumber.info("Network request completed in %dms", latency)
Lumber.warn("Cache miss for key: %s", key)
Lumber.error(exception, "An unexpected error occurred")
Lumber.wtf("Critical failure that should never happen!")
```

### 3. Contextual Tags

Use `tag()` for one-time contextual information. The tag is automatically cleared after the next log
call.

```kotlin
Lumber.tag("AuthService").info("User session started")
```

### 4. Custom Oaks

Create your own logging destinations by extending `Lumber.Oak`.

```kotlin
class AnalyticsOak : Lumber.Oak() {
    override fun isLoggable(tag: String?, level: Lumber.Level) = level >= Lumber.Level.INFO

    override fun log(level: Lumber.Level, tag: String?, message: String, error: Throwable?) {
        // Send to your analytics service
        Analytics.logEvent(
            "app_log", mapOf(
                "level" to level.name,
                "message" to message,
                "tag" to tag
            )
        )
    }
}

Lumber.plant(AnalyticsOak())
```

## 🌍 Platform Support

| Target                | DebugOak Implementation       | Output                    |
|:----------------------|:------------------------------|:--------------------------|
| **Android**           | `android.util.Log`            | Logcat                    |
| **JVM**               | ANSI Colored `println`        | Terminal / Console        |
| **Apple** (iOS/macOS) | ANSI Colored `println`        | Terminal / Xcode Console  |
| **JS / Wasm**         | `console.log/info/warn/error` | Browser / Node.js Console |

## 🏗️ Built With

| Tool       | Version  |
|:-----------|:---------|
| **Kotlin** | `2.3.10` |
| **Gradle** | `9.3.1`  |
| **Java**   | `21`     |

## 🤝 Contributing

Contributions are welcome! If you find a bug or have a feature request, please open an issue. If
you'd like to contribute code, please fork the repository and submit a pull request.

Please read [CONTRIBUTING](CONTRIBUTING.md) for a straightforward, KMP-focused workflow.

## 📚 Documentation

For detailed API information, please refer to
the [KDocs](/docs/api/lumber/index.md).

## 📄 License

```text
Copyright 2025 Matheus Corregiari

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

*Inspired by [Timber][link-timber] by Jake Wharton.*

[link-maven]: https://search.maven.org/artifact/io.github.matheus-corregiari/arch-lumber

[link-timber]: https://github.com/JakeWharton/timber

[link-coverage]: https://codecov.io/gh/matheus-corregiari/arch-lumber

[badge-kotlin]: https://img.shields.io/badge/kotlin-2.3.10-blue.svg?logo=kotlin

[badge-maven]: https://img.shields.io/maven-central/v/io.github.matheus-corregiari/arch-lumber.svg

[badge-license]: https://img.shields.io/github/license/matheus-corregiari/arch-lumber

[badge-coverage]: https://codecov.io/gh/matheus-corregiari/arch-lumber/graph/badge.svg?token=P977R4GMUO

[badge-lint]: https://github.com/matheus-corregiari/arch-lumber/actions/workflows/lint.yml/badge.svg

[badge-test]: https://github.com/matheus-corregiari/arch-lumber/actions/workflows/coverage.yml/badge.svg
