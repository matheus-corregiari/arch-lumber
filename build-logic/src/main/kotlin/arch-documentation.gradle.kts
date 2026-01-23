import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.tasks.KoverReport
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.tasks.DokkaGenerateTask

plugins {
    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlinx.kover")
    jacoco
}
extensions.configure(JacocoPluginExtension::class) {
    toolVersion = libraries.version("jacoco")
}

project.plugins.apply(DokkaMarkdownPlugin::class.java)
extensions.configure(DokkaExtension::class) {
    moduleName.set(project.name.capitalized())
    moduleVersion.set(project.versionName)
    basePublicationsDirectory.set(file("$rootDir/docs/api/${project.name}"))
    dokkaPublications.getByName("html").enabled.set(false)
    dokkaPublications.getByName("markdown").outputDirectory = basePublicationsDirectory

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
            xml { xmlFile.set(file("$rootDir/docs/coverage/${project.name}/coverage.xml")) }
            binary { file.set(file("$rootDir/docs/coverage/${project.name}/coverage.bin")) }
            html { onCheck.set(false) }
        }
    }
}

val updateDocs by tasks.registering {
    group = "documentation"
    dependsOn(tasks.withType(DokkaGenerateTask::class))
    dependsOn(tasks.withType(KoverReport::class))
}
