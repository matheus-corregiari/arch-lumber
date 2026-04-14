import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.dokka.gradle.DokkaExtension

plugins {
    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlinx.kover")
    jacoco
}
extensions.configure(JacocoPluginExtension::class) {
    toolVersion = libraries.version("jacoco")
}

extensions.configure(DokkaExtension::class) {
    moduleName.set(project.name.capitalized())
    moduleVersion.set(project.versionName)
    basePublicationsDirectory.set(file("$rootDir/docs/api"))
    dokkaPublications.getByName("html").outputDirectory = basePublicationsDirectory

    dokkaSourceSets.configureEach {
        reportUndocumented.set(true)
        skipDeprecated.set(true)
        skipEmptyPackages.set(true)
        jdkVersion.set(projectJavaVersionCode)
        enableAndroidDocumentationLink.set(true)
        enableJdkDocumentationLink.set(true)
        enableKotlinStdLibDocumentationLink.set(true)
    }
}

extensions.configure(KoverProjectExtension::class) {
    reports {
        total {
            verify {
                rule("Minimum line coverage") {
                    minBound(65, CoverageUnit.LINE)
                }
                rule("Minimum instruction coverage") {
                    minBound(65, CoverageUnit.INSTRUCTION)
                }
                rule("Minimum branch coverage") {
                    minBound(65, CoverageUnit.BRANCH)
                }
            }
        }
    }
}
