@file:Suppress("UnstableApiUsage", "OPT_IN_USAGE")

/**
 * Configures a Kotlin Multiplatform Android library module.
 *
 * The plugin owns common compiler, hierarchy, Android namespace, lint, test coverage, native
 * framework, and source jar defaults shared by publishable library modules.
 */
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

extensions.configure<KotlinMultiplatformExtension> {
    compilerOptions {
        jvmToolchain(projectJavaVersionCode)
        progressiveMode.set(true)
    }
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

    android {
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
        }
        testCoverage { jacocoVersion = libraries.version("jacoco") }
        optimization.consumerKeepRules.file("consumer-proguard-rules.pro")
    }
    jvm { }
    wasmJs {
        browser { testTask { useKarma { useChromeHeadless() } } }
        binaries.library()
    }
    js {
        browser { testTask { useKarma { useChromeHeadless() } } }
        binaries.library()
    }
    // iOS Targets
    val exportName =
        project.name.split("-").joinToString(
            separator = "",
            transform = { it.replaceFirstChar(Char::titlecase) }
        )
    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "${exportName}Kit"
            isStatic = true
        }
    }

    sourceSets {
        val javaMain = named("javaMain").get()
        androidMain { dependsOn(javaMain) }
    }
}
