plugins {
    id("arch-multi-library")
    id("arch-lint")
    id("arch-documentation")
    id("arch-coverage")
    id("arch-optimize")
    id("arch-publish")
    id("arch-compatibility")
    alias(libs.plugins.jetbrains.atomic)
}

configure<CompatibilityExtension> {
    consumers = listOf(
        PublishedConsumer("1.1.0", "Consumer"),
        PublishedConsumer("1.4.4", "CurrentConsumer")
    )
    brokenVersion = "1.4.4"
}

kotlin {
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
