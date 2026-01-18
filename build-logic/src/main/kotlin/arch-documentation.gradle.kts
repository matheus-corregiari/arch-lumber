import kotlinx.kover.gradle.plugin.dsl.tasks.KoverHtmlReport
import kotlinx.kover.gradle.plugin.dsl.tasks.KoverReport
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.tasks.DokkaBaseTask
import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

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
    dokkaSourceSets {
        configureEach {
            reportUndocumented.set(true)
            skipDeprecated.set(true)
            skipEmptyPackages.set(true)
            jdkVersion.set(projectJavaVersionCode)

            externalDocumentationLinks.register("androidRef") {
                url("https://developer.android.com/reference/")
                packageListUrl.set(rootProject.file("/tools/android-package-list").toURI())
            }
        }
    }
}

tasks.withType(DokkaBaseTask::class) { group = "documentation" }
tasks.withType(DokkaGeneratePublicationTask::class) {
    doLast {
        val reportDir = outputDirectory.asFile.get()
        val targetDir = File("$rootDir/docs", "api")

        if (targetDir.exists()) targetDir.deleteRecursively()
        targetDir.mkdirs()

        reportDir.copyRecursively(targetDir, overwrite = true)
    }
}

tasks.withType(KoverReport::class) { group = "documentation" }
tasks.withType(KoverHtmlReport::class) {
    doLast {
        val reportDir = reportDir.get().asFile
        val targetDir = File("$rootDir/docs", "coverage")

        if (targetDir.exists()) targetDir.deleteRecursively()
        targetDir.mkdirs()

        reportDir.copyRecursively(targetDir, overwrite = true)
    }
}

val updateDocs by tasks.registering {
    group = "documentation"
    dependsOn(tasks.withType(DokkaGeneratePublicationTask::class))
    dependsOn(tasks.withType(KoverHtmlReport::class))
}
