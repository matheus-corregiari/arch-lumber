plugins {
    id("arch-multi-library")
    id("arch-lint")
    id("arch-documentation")
    id("arch-coverage")
    id("arch-optimize")
    id("arch-publish")
    alias(libs.plugins.jetbrains.atomic)
}

apply(from = rootProject.file("gradle/compatibility.gradle.kts"))

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
    abiValidation {
        keepLocallyUnsupportedTargets.set(false)
    }

    // Libraries
    sourceSets {
        // Common Setup
        commonTest.dependencies { implementation(libs.jetbrains.kotlin.test) }
    }
}

dokka.dokkaSourceSets.configureEach {
    sourceLink {
        localDirectory.set(projectDir.resolve("src"))
        remoteUrl("${env("POM_URL")}/tree/master/lumber/src")
    }
}
