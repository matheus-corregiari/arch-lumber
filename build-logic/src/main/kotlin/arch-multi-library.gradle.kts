@file:Suppress("UnstableApiUsage", "OPT_IN_USAGE")

import com.android.build.api.variant.impl.capitalizeFirstChar
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

extensions.configure<KotlinMultiplatformExtension> {
    compilerOptions { jvmToolchain(projectJavaVersionCode) }
    withSourcesJar(true)
    applyDefaultHierarchyTemplate {
        common {
            group("java") {
                withJvm()
                withAndroidTarget()
            }
            group("kotlin") {
                withJs()
                withWasmJs()
            }
        }
    }

    androidLibrary {
        namespace = "br.com.arch.toolkit.${project.name}"
        testNamespace = "test.$namespace"
        androidResources { enable = false }
        withHostTest {
            enableCoverage = true
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        lint {
            checkReleaseBuilds = true
            abortOnError = true
            ignoreWarnings = false
            absolutePaths = false
            warningsAsErrors = false

            htmlOutput = File("$rootDir/build/reports/lint/html/${project.name}-lint.html")
            xmlOutput = File("$rootDir/build/reports/lint/xml/${project.name}-lint.xml")
        }
        testCoverage { jacocoVersion = libraries.version("jacoco") }
        optimization.consumerKeepRules.file("consumer-proguard-rules.pro")
    }
    jvm { }
    wasmJs {
        browser { testTask { useKarma { useChromeHeadless() } } }
        binaries.library()
    }
    js(IR) {
        browser { testTask { useKarma { useChromeHeadless() } } }
        binaries.library()
    }
    // iOS Targets
    val exportName =
        project.name.split("-").joinToString(
            separator = "",
            transform = String::capitalizeFirstChar,
        )
    val exportId = "br.com.arch.toolkit.${project.name}"
    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "${exportName}Kit"
            isStatic = true
            freeCompilerArgs += listOf("-bundle-id", exportId)
        }
    }

    sourceSets {
        val javaMain by getting
        androidMain { dependsOn(javaMain) }
    }
}
