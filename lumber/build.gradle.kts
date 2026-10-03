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
        PublishedConsumer("Consumer", beforeVersion = "1.2.0"),
        PublishedConsumer("CurrentConsumer")
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
