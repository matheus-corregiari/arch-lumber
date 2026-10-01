/**
 * Configures API documentation for publishable modules.
 *
 * The plugin enables Dokka so CI can generate API reference material.
 */
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.dokka.gradle.DokkaExtension

plugins {
    id("org.jetbrains.dokka")
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
