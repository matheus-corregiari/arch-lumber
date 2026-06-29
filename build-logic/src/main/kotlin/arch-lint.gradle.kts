@file:Suppress("UnstableApiUsage")

/**
 * Configures static analysis for library modules.
 *
 * The plugin applies Detekt and Ktlint, points both tools at the repository config files, and
 * standardizes report generation for CI artifacts.
 */
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    id("dev.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

extensions.configure<DetektExtension> {
    parallel = true
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$rootDir/tools/detekt-config.yml")
    baseline = file("$rootDir/tools/detekt-baseline.xml")
    basePath.set(rootDir)
}
extensions.configure<KtlintExtension> {
    android.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    ignoreFailures.set(false)

    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
    }
}

tasks.withType<Detekt>().configureEach {
    basePath.set(rootDir.absolutePath)
    reports {
        html.required.set(true)
        checkstyle.required.set(true)
        sarif.required.set(true)
        markdown.required.set(true)
    }
}
