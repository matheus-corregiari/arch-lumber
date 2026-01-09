@file:Suppress("UnstableApiUsage", "OPT_IN_USAGE")

import com.android.build.api.dsl.androidLibrary
import com.android.build.api.variant.impl.capitalizeFirstChar
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

extensions.configure<KotlinMultiplatformExtension> {
    compilerOptions { jvmToolchain(projectJavaVersionCode) }
    androidLibrary {
        namespace = "br.com.arch.toolkit.${project.name}"
        testNamespace = "test.$namespace"
        compileSdk = libraries.version("build-sdk-compile").toInt()
        minSdk = libraries.version("build-sdk-min").toInt()
        buildToolsVersion = libraries.version("build-tools")
        androidResources { enable = false }
        compilerOptions { jvmTarget.set(projectJavaTarget) }
    }
    jvm { compilerOptions { jvmTarget.set(projectJavaTarget) } }
    wasmJs {
        browser { testTask { useKarma { useChromeHeadless() } } }
        binaries.library()
    }
    js(IR) {
        browser { testTask { useKarma { useChromeHeadless() } } }
        binaries.library()
    }
    // iOS Targets
    val exportName = name.split("-").joinToString(
        separator = "",
        transform = String::capitalizeFirstChar
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

    sourceSets {
        val javaMain by getting
        androidMain { dependsOn(javaMain) }
    }

}
