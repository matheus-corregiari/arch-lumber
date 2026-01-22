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
            url = uri(buildFile.get().absolutePath)
        }

        maven {
            name = "Github"
            url = uri(env("REPO_MAVEN_URL"))
            credentials {
                username = env("GITHUB_ACTOR")
                password = env("GITHUB_TOKEN")
            }
        }
    }

    publications.withType(MavenPublication::class.java) {
        version = versionName
        pom {
            // SCM
            scm { tag.set(versionName) }

            // Ci Management
            ciManagement {
                system.set("GitHub Actions")
                url.set("${env("POM_URL")}/actions")
            }
        }
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
