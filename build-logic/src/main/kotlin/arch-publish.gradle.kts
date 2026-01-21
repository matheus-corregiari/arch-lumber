import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    `maven-publish`
    id("com.vanniktech.maven.publish")
}

extensions.configure(PublishingExtension::class) {
    repositories {
        maven {
            val buildFile = project.rootProject.layout.buildDirectory.asFile
            name = "LocalPath"
            url = project.uri(buildFile.get().absolutePath)
        }
    }

    publications.withType(MavenPublication::class.java) {
        groupId = properties["GROUP"] as String
        version = versionName
        // pom { configurePom(it, false) }
    }
}

extensions.configure(MavenPublishBaseExtension::class) {
    signAllPublications()
    publishToMavenCentral(true)
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaGenerate"),
            sourcesJar = true,
        ),
    )
}
