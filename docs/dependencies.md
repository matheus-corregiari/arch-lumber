# Dependencies

Audited against Maven Central, Google Maven and the Gradle Plugin Portal on 2026-09-16 for `1.4.2`.
Runtime dependencies and AGP use stable releases. Detekt retains its existing alpha line.
Gradle **9.7.1**, JDK **21**, Kover **0.9.9**, MkDocs Material **9.7.7**.

A Git tag does not guarantee Maven availability: Arch Lumber currently resolves to **1.4.0** in Maven Central.
The patches in sibling repositories can be adopted after their artifacts are published.

| Alias | Version | Source |
| --- | --- | --- |
| `jetbrains-dokka` | `2.2.0` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/dokka/dokka-gradle-plugin/maven-metadata.xml) |
| `jetbrains-plugin` | `2.4.20` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-gradle-plugin/maven-metadata.xml) |
| `jetbrains-multiplatform` | `2.4.20` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/multiplatform/org.jetbrains.kotlin.multiplatform.gradle.plugin/maven-metadata.xml) |
| `jetbrains-kover` | `0.9.9` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlinx/kover-gradle-plugin/maven-metadata.xml) |
| `jetbrains-kotlin-test` | `2.4.20` | [Metadata](https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-test/maven-metadata.xml) |
| `androidx-library` | `9.4.0` | [Metadata](https://dl.google.com/dl/android/maven2/com/android/kotlin/multiplatform/library/com.android.kotlin.multiplatform.library.gradle.plugin/maven-metadata.xml) |
| `detekt` | `2.0.0-alpha.6` | [Metadata](https://repo.maven.apache.org/maven2/dev/detekt/detekt-gradle-plugin/maven-metadata.xml) |
| `ktlint` | `14.2.0` | [Metadata](https://plugins.gradle.org/m2/org/jlleitschuh/gradle/ktlint/org.jlleitschuh.gradle.ktlint.gradle.plugin/maven-metadata.xml) |
| `vanniktech-publish` | `0.37.0` | [Metadata](https://repo.maven.apache.org/maven2/com/vanniktech/gradle-maven-publish-plugin/maven-metadata.xml) |

## Tooling sources

- [Gradle current release](https://services.gradle.org/versions/current)
- [MkDocs Material](https://pypi.org/project/mkdocs-material/)
- [JaCoCo](https://repo.maven.apache.org/maven2/org/jacoco/org.jacoco.core/maven-metadata.xml)

Android SDK setup uses [`android-actions/setup-android@v4`](https://github.com/android-actions/setup-android/releases/tag/v4.0.1)
with Node 24 and command-line tools 20.0.
