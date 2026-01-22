plugins {
    id("arch-multi-library")
    id("arch-lint")
    id("arch-documentation")
    id("arch-optimize")
    id("arch-publish")
}

kotlin {
    androidLibrary {
        compileSdk = versionInt { libs.versions.build.sdk.compile }
        minSdk = versionInt { libs.versions.build.sdk.min }
        buildToolsVersion = versionString { libs.versions.build.tools }
    }

    // Libraries
    sourceSets {
        // Common Setup
        commonMain.dependencies { implementation(libs.jetbrains.coroutines.core) }
        commonTest.dependencies { implementation(libs.jetbrains.kotlin.test) }
    }
}

dokka.dokkaSourceSets.configureEach {
    sourceLink {
        localDirectory.set(projectDir.resolve("src"))
        remoteUrl("https://github.com/matheus-corregiari/arch-lumber/tree/master/lumber/src")
    }
}
