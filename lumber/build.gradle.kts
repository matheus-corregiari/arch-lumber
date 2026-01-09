plugins {
    id("arch-multi-library")
    id("arch-lint")
}

kotlin {
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
