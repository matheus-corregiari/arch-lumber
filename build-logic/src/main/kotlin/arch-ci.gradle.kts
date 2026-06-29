/**
 * Registers the root CI lifecycle tasks used by GitHub Actions.
 *
 * This plugin only aggregates tasks exposed by subprojects. Library, lint, documentation,
 * publication, target, and sample-specific configuration must stay in their own convention plugins
 * or module build files.
 */
plugins {
    id("org.jetbrains.kotlinx.kover")
}

dependencies {
    subprojects.forEach { add("kover", project(it.path)) }
}

val syncContributingDocs by tasks.registering(Copy::class) {
    description = "Syncs CONTRIBUTING.md into the MkDocs source tree."
    from(layout.projectDirectory.file("CONTRIBUTING.md"))
    into(layout.projectDirectory.dir("docs"))
    rename { "contributing.md" }
}

val ciLint by tasks.registering {
    group = "CI"
    description = "Runs lint checks for all modules that expose lint tasks."
}

val ciDocs by tasks.registering {
    group = "CI"
    description = "Generates API documentation inputs for the MkDocs site."
    dependsOn(syncContributingDocs)
}

val ciBuild by tasks.registering {
    group = "CI"
    description = "Assembles all publishable modules."
}

val ciTest by tasks.registering {
    group = "CI"
    description = "Runs all supported test tasks."
}

val ciCoverage by tasks.registering {
    group = "CI"
    description = "Runs tests and verifies merged coverage."
    dependsOn(ciTest)
}

val ciPublishMavenCentral by tasks.registering {
    group = "CI"
    description = "Publishes all publishable modules to Maven Central."
}

val ciPublishGithubPackages by tasks.registering {
    group = "CI"
    description = "Publishes all publishable modules to GitHub Packages."
}

val ciPublishLocal by tasks.registering {
    group = "CI"
    description = "Publishes all publishable modules to the local Maven repository."
}

gradle.projectsEvaluated {
    val multiplatformProjects = subprojects.filter {
        it.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")
    }
    val publishableProjects = subprojects.filter {
        it.plugins.hasPlugin("com.vanniktech.maven.publish")
    }

    fun Project.taskPath(name: String): String? = tasks.findByName(name)?.path

    ciLint.configure {
        dependsOn(multiplatformProjects.mapNotNull { it.taskPath("detekt") })
        dependsOn(multiplatformProjects.mapNotNull { it.taskPath("ktlintCheck") })
    }
    ciDocs.configure {
        dependsOn(publishableProjects.mapNotNull { it.taskPath("dokkaGeneratePublicationHtml") })
    }
    ciBuild.configure {
        dependsOn(publishableProjects.mapNotNull { it.taskPath("assemble") })
    }
    ciTest.configure {
        dependsOn(multiplatformProjects.mapNotNull { it.taskPath("allTests") })
    }
    ciCoverage.configure {
        dependsOn(
            listOfNotNull(
                taskPath("koverXmlReport"),
                taskPath("koverHtmlReport"),
                taskPath("koverVerify")
            )
        )
        dependsOn(multiplatformProjects.mapNotNull { it.taskPath("koverVerify") })
    }
    ciPublishMavenCentral.configure {
        dependsOn(publishableProjects.mapNotNull { it.taskPath("publishAndReleaseToMavenCentral") })
    }
    ciPublishGithubPackages.configure {
        dependsOn(
            publishableProjects.mapNotNull {
                it.taskPath("publishAllPublicationsToGithubRepository")
            }
        )
    }
    ciPublishLocal.configure {
        dependsOn(publishableProjects.mapNotNull { it.taskPath("publishToMavenLocal") })
    }
}
