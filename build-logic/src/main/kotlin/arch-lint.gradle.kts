@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.androidLibrary
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

extensions.configure<DetektExtension> {
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    allRules = false
    config.setFrom("$rootDir/tools/detekt.yml")
    baseline = file("$rootDir/tools/detekt-baseline.xml")
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

extensions.configure<KotlinMultiplatformExtension> {
    androidLibrary {
        lint {
            checkReleaseBuilds = true
            abortOnError = true
            ignoreWarnings = false
            absolutePaths = false
            warningsAsErrors = false

            htmlOutput = File("${rootDir}/build/reports/lint/html/${project.name}-lint.html")
            xmlOutput = File("${rootDir}/build/reports/lint/xml/${project.name}-lint.xml")
        }
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = projectJavaVersionName
    reports {
        xml.required.set(true)
        html.required.set(true)
        md.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
    }
}
tasks.withType<DetektCreateBaselineTask>().configureEach { jvmTarget = projectJavaVersionName }

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${libraries.version("detekt")}")
}
