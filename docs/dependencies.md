# Dependencies

Audited against Maven Central, Google Maven and the Gradle Plugin Portal on 2026-09-29 for `1.4.2`.
Runtime dependencies and AGP use stable releases. Detekt retains its existing alpha line.
Android compile SDK **37.2**, minimum SDK **20**, Build Tools **37.0.0**.
Gradle **9.8.0**, JDK **21**, Kover **0.9.11**, MkDocs Material **9.7.7**.

A Git tag does not guarantee Maven availability: Arch Lumber currently resolves to **1.4.0** in Maven Central.
The `1.4.2` hotfix remains a release candidate until its artifacts are published.

| Alias | Version | Source |
| --- | --- | --- |
| `jetbrains-atomic` | `0.33.0` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlinx/atomicfu/maven-metadata.xml) |
| `jetbrains-dokka` | `2.2.0` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/dokka/dokka-gradle-plugin/maven-metadata.xml) |
| `jetbrains-plugin` | `2.4.20` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-gradle-plugin/maven-metadata.xml) |
| `jetbrains-multiplatform` | `2.4.20` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/multiplatform/org.jetbrains.kotlin.multiplatform.gradle.plugin/maven-metadata.xml) |
| `jetbrains-kover` | `0.9.11` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlinx/kover-gradle-plugin/maven-metadata.xml) |
| `jetbrains-kotlin-test` | `2.4.20` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-test/maven-metadata.xml) |
| `androidx-library` | `9.4.1` | [Metadata](https://dl.google.com/dl/android/maven2/com/android/kotlin/multiplatform/library/com.android.kotlin.multiplatform.library.gradle.plugin/maven-metadata.xml) |
| `detekt` | `2.0.0-alpha.6` | [Metadata](https://repo.maven.apache.org/maven2/dev/detekt/detekt-gradle-plugin/maven-metadata.xml) |
| `ktlint` | `14.2.0` | [Metadata](https://plugins.gradle.org/m2/org/jlleitschuh/gradle/ktlint/org.jlleitschuh.gradle.ktlint.gradle.plugin/maven-metadata.xml) |
| `vanniktech-publish` | `0.37.0` | [Metadata](https://repo.maven.apache.org/maven2/com/vanniktech/gradle-maven-publish-plugin/maven-metadata.xml) |

## Tooling sources

- [Gradle current release](https://services.gradle.org/versions/current)
- [MkDocs Material](https://pypi.org/project/mkdocs-material/)
- [JaCoCo](https://repo.maven.apache.org/maven2/org/jacoco/org.jacoco.core/maven-metadata.xml)

Android SDK setup uses [`android-actions/setup-android@v4`](https://github.com/android-actions/setup-android/tree/v4)
with Node 24 and the maintained command-line tools provided by the action.

## Android SDK audit

The [official SDK repository](https://dl.google.com/android/repository/repository2-3.xml)
provides `platforms;android-37.2` on the stable channel without a preview codename.
Compile SDK uses major API 37 and minor API 2 through the
[expanded DSL](https://developer.android.com/build#module-level), rather than a decimal integer.
Build Tools 37.0.0 is the latest stable package. CI installs the latest stable `platform-tools`
(currently 37.0.1). Minimum SDK remains 20; increasing the compilation SDK does not require
increasing the minimum supported Android version. This library has no application `targetSdk`.

The finalized compilation SDK package is independent of the Android 17 QPR2 device beta rollout.
