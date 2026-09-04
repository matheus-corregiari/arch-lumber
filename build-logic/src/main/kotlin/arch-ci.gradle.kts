/** Common CI contract. Target selection remains in the module conventions. */
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
    id("org.jetbrains.kotlinx.kover")
}

dependencies {
    subprojects.filter { it.name != "test" }.forEach { add("kover", project(it.path)) }
}

val coverageLines = providers.gradleProperty("ci.coverage.lines").get().toInt()
val coverageInstructions = providers.gradleProperty("ci.coverage.instructions").get().toInt()
val coverageBranches = providers.gradleProperty("ci.coverage.branches").get().toInt()

kover {
    reports {
        total {
            verify {
                rule("Minimum line coverage") { minBound(coverageLines, CoverageUnit.LINE) }
                rule("Minimum instruction coverage") { minBound(coverageInstructions, CoverageUnit.INSTRUCTION) }
                rule("Minimum branch coverage") { minBound(coverageBranches, CoverageUnit.BRANCH) }
            }
        }
    }
}

val syncContributingDocs = tasks.register("syncContributingDocs", Copy::class) {
    from(layout.projectDirectory.file("CONTRIBUTING.md"))
    into(layout.projectDirectory.dir("docs"))
    rename { "contributing.md" }
    filter { line: String -> line.replace("(docs/ci.md)", "(ci.md)") }
}

val ciLint = tasks.register("ciLint") { group = "CI" }
val ciDocs = tasks.register("ciDocs") { group = "CI"; dependsOn(syncContributingDocs) }
val ciBuild = tasks.register("ciBuild") { group = "CI" }
val ciTest = tasks.register("ciTest") { group = "CI" }
val ciCoverage = tasks.register("ciCoverage") { group = "CI"; dependsOn(ciTest) }
val ciCodeql = tasks.register("ciCodeql") { group = "CI" }
val ciPublishMavenCentral = tasks.register("ciPublishMavenCentral") { group = "CI" }
val ciPublishGithubPackages = tasks.register("ciPublishGithubPackages") { group = "CI" }
val ciPublishLocal = tasks.register("ciPublishLocal") { group = "CI" }
val ciPublicationManifest = tasks.register("ciPublicationManifest") { group = "CI" }

gradle.projectsEvaluated {
    val libraries = subprojects.filter { it.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") }
    val publishable = subprojects.filter { it.plugins.hasPlugin("com.vanniktech.maven.publish") }
    fun List<Project>.paths(name: String) = mapNotNull { it.tasks.findByName(name)?.path }

    // Master must execute tests again; compilation remains cacheable.
    if (System.getenv("GITHUB_EVENT_NAME") == "push" && System.getenv("GITHUB_REF") == "refs/heads/master") {
        libraries.forEach { project ->
            project.tasks.withType(AbstractTestTask::class.java).configureEach {
                outputs.upToDateWhen { false }
                outputs.cacheIf { false }
            }
        }
    }

    ciLint.configure {
        dependsOn(libraries.paths("detekt"), libraries.paths("ktlintCheck"), libraries.paths("lint"))
    }
    ciDocs.configure { dependsOn(publishable.paths("dokkaGenerate")) }
    ciBuild.configure { dependsOn(publishable.paths("assemble")) }
    ciTest.configure { dependsOn(libraries.paths("allTests")) }
    ciCoverage.configure {
        dependsOn("koverXmlReport", "koverHtmlReport", "koverVerify")
        dependsOn(publishable.paths("koverVerify"))
    }
    ciCodeql.configure {
        val compilations = libraries.paths("compileKotlinJvm") + libraries.paths("compileAndroidMain")
        check(compilations.isNotEmpty()) { "No JVM/Android compilation configured for CodeQL" }
        dependsOn(compilations)
    }
    ciPublishMavenCentral.configure { dependsOn(publishable.paths("publishAndReleaseToMavenCentral")) }
    ciPublishGithubPackages.configure { dependsOn(publishable.paths("publishAllPublicationsToGithubRepository")) }
    ciPublishLocal.configure { dependsOn(publishable.paths("publishToMavenLocal")) }
    ciPublicationManifest.configure {
        doLast {
            val coordinates = publishable.flatMap { project ->
                project.extensions.getByType(PublishingExtension::class.java)
                    .publications.withType(MavenPublication::class.java)
                    .map { "${it.groupId}\t${it.artifactId}\t${it.version}" }
            }.sorted()
            check(coordinates.isNotEmpty()) { "No Maven publications found" }
            val manifest = layout.buildDirectory.file("ci/publications.tsv").get().asFile
            manifest.parentFile.mkdirs()
            manifest.writeText(coordinates.joinToString("\n"))
        }
    }
}
