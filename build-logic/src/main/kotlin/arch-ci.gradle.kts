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

val syncContributingDocs = tasks.register("syncContributingDocs", Copy::class) {
    description = "Syncs CONTRIBUTING.md into the MkDocs source tree."
    from(layout.projectDirectory.file("CONTRIBUTING.md"))
    into(layout.projectDirectory.dir("docs"))
    rename { "contributing.md" }
}

val ciLint = tasks.register("ciLint") {
    group = "CI"
    description = "Runs lint checks for all modules that expose lint tasks."
}

val ciDocs = tasks.register("ciDocs") {
    group = "CI"
    description = "Generates API documentation inputs for the MkDocs site."
    dependsOn(syncContributingDocs)
}

val ciBuild = tasks.register("ciBuild") {
    group = "CI"
    description = "Assembles all publishable modules."
}

val ciTest = tasks.register("ciTest") {
    group = "CI"
    description = "Runs all supported test tasks."
}

val ciCoverage = tasks.register("ciCoverage") {
    group = "CI"
    description = "Runs tests and verifies merged coverage."
    dependsOn(ciTest)
}

val ciPublishMavenCentral = tasks.register("ciPublishMavenCentral") {
    group = "CI"
    description = "Publishes all publishable modules to Maven Central."
}

val ciPublishGithubPackages = tasks.register("ciPublishGithubPackages") {
    group = "CI"
    description = "Publishes all publishable modules to GitHub Packages."
}

val ciPublishLocal = tasks.register("ciPublishLocal") {
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
    fun List<Project>.taskPath(name: String) = mapNotNull { it.taskPath(name) }

    ciLint.configure {
        dependsOn(multiplatformProjects.taskPath("detekt"))
        dependsOn(multiplatformProjects.taskPath("ktlintCheck"))
    }
    ciDocs.configure { dependsOn(publishableProjects.taskPath("dokkaGeneratePublicationHtml")) }
    ciBuild.configure { dependsOn(publishableProjects.taskPath("assemble")) }
    ciTest.configure { dependsOn(multiplatformProjects.taskPath("allTests")) }
    ciCoverage.configure {
        dependsOn(
            listOf("koverXmlReport", "koverHtmlReport", "koverVerify").mapNotNull { taskPath(it) }
        )
        dependsOn(multiplatformProjects.taskPath("koverVerify"))
    }
    ciPublishMavenCentral.configure {
        dependsOn(publishableProjects.taskPath("publishAndReleaseToMavenCentral"))
    }
    ciPublishGithubPackages.configure {
        dependsOn(publishableProjects.taskPath("publishAllPublicationsToGithubRepository"))
    }
    ciPublishLocal.configure { dependsOn(publishableProjects.taskPath("publishToMavenLocal")) }
}
