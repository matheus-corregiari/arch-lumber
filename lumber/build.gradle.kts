plugins {
    id("arch-multi-library")
    id("arch-lint")
    id("arch-documentation")
    id("arch-optimize")
}

kotlin {
    androidLibrary {
        compileSdk =
            libs.versions.build.sdk.compile
                .get()
                .toInt()
        minSdk =
            libs.versions.build.sdk.min
                .get()
                .toInt()
        buildToolsVersion =
            libs.versions.build.tools
                .get()
    }

    // Libraries
    sourceSets {
        // Common Setup
        commonMain.dependencies { implementation(libs.jetbrains.coroutines.core) }
        commonTest.dependencies { implementation(libs.jetbrains.kotlin.test) }

        // Target Setup
        jvmMain { dependencies { implementation(libs.ajalt.mordant) } }
        appleMain { dependencies { implementation(libs.ajalt.mordant) } }
    }
}

dokka.dokkaSourceSets.configureEach {
    sourceLink {
        localDirectory.set(projectDir.resolve("src"))
        remoteUrl("https://github.com/matheus-corregiari/arch-lumber/tree/master/lumber/src")
    }
}
