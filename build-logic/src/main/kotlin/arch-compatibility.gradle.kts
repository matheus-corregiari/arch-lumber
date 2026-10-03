import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaToolchainService
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import java.io.ByteArrayOutputStream

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    `maven-publish`
}

val compatibility = extensions.create<CompatibilityExtension>("compatibility")
if (!providers.gradleProperty("codeqlAnalysis").map(String::toBoolean).getOrElse(false)) {
    extensions.configure<KotlinMultiplatformExtension> {
        @OptIn(ExperimentalAbiValidation::class)
        abiValidation { keepLocallyUnsupportedTargets.set(false) }
    }
}

// Compile once against a released library, then run the same bytecode against candidate publications.
val publishing = extensions.getByType<PublishingExtension>()
val javaToolchains = extensions.getByType<JavaToolchainService>()
val runtimeDependencies = configurations.named("jvmRuntimeClasspath")
val compatibilityCheck = tasks.register("compatibilityCheck") { group = "verification" }
tasks.named("check") { dependsOn(compatibilityCheck) }

fun publicationFile(target: String, extension: String) = providers.provider {
    val publication = publishing.publications.getByName(target) as MavenPublication
    rootProject.layout.buildDirectory.file(
        "${publication.groupId.replace('.', '/')}/${publication.artifactId}/${publication.version}/" +
            "${publication.artifactId}-${publication.version}.$extension"
    ).get().asFile
}

val androidClasses = tasks.register<Sync>("extractCompatibilityAndroidClasses") {
    dependsOn("publishAndroidPublicationToLocalPathRepository")
    from(publicationFile("android", "aar").map { zipTree(it) }) { include("classes.jar") }
    into(layout.buildDirectory.dir("compatibility/android"))
}

afterEvaluate {
    val publication = publishing.publications.getByName("jvm") as MavenPublication
    val releasedModule = "${publication.groupId}:${publication.artifactId}"
    val tagOutput = providers.exec {
        workingDir(rootProject.projectDir)
        commandLine("git", "tag", "--list")
    }.standardOutput.asText.get()
    val releaseVersion = Regex("v?\\d+\\.\\d+\\.\\d+")
    val releaseTags = tagOutput.lineSequence().map(String::trim)
        .filter { it.matches(releaseVersion) }
        .toList()
    fun versionParts(version: String) = version.removePrefix("v").split('.').map(String::toInt)
    val versionOrder = Comparator<String> { first, second ->
        versionParts(first).zip(versionParts(second))
            .map { (left, right) -> left.compareTo(right) }.firstOrNull { it != 0 } ?: 0
    }
    compatibility.consumers.forEachIndexed { index, consumer ->
        val releasedVersion = releaseTags.filter {
            (!publication.version.matches(releaseVersion) || versionOrder.compare(it, publication.version) < 0) &&
                (consumer.beforeVersion == null || versionOrder.compare(it, consumer.beforeVersion) < 0)
        }.maxWithOrNull(versionOrder)?.removePrefix("v")
            ?: error("No release tag found for ${consumer.mainClass}; fetch the repository tags first.")
        val suffix = releasedVersion.replace(".", "_")
        val released = configurations.create("consumerCompile$suffix")
        dependencies.add(released.name, "$releasedModule:$releasedVersion")
        val consumerClass = consumer.mainClass
        val compileConsumer = tasks.register<JavaCompile>("compileConsumer$suffix") {
            source(file("${compatibility.sourceDirectory}/${consumerClass.replace('.', '/')}.java"))
            classpath = released
            destinationDirectory.set(layout.buildDirectory.dir("compatibility/$releasedVersion"))
            javaCompiler.set(javaToolchains.compilerFor {
                languageVersion.set(JavaLanguageVersion.of(compatibility.javaVersion))
            })
            options.release.set(compatibility.javaVersion)
        }
        listOf("jvm", "android").filter { publishing.publications.findByName(it) != null }.forEach { target ->
            val runConsumer = tasks.register<JavaExec>("consumer${suffix}${target.replaceFirstChar(Char::uppercase)}") {
                dependsOn(compileConsumer)
                val candidate = if (target == "jvm") {
                    dependsOn("publishJvmPublicationToLocalPathRepository")
                    files(publicationFile("jvm", "jar"))
                } else {
                    dependsOn(androidClasses)
                    files(androidClasses.map { it.destinationDir.resolve("classes.jar") })
                }
                classpath = files(compileConsumer.flatMap { it.destinationDirectory }) + candidate + runtimeDependencies.get()
                mainClass.set(consumerClass)
                javaLauncher.set(javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(compatibility.javaVersion))
                })
            }
            compatibilityCheck.configure { dependsOn(runConsumer) }
        }
        if (index == 0 && compatibility.brokenVersion != null) {
            val brokenRelease = configurations.create("consumerBrokenRuntime")
            dependencies.add(brokenRelease.name, "$releasedModule:${compatibility.brokenVersion}")
            val negativeControl = tasks.register<JavaExec>("consumerNegativeControl") {
                dependsOn(compileConsumer)
                val output = ByteArrayOutputStream()
                classpath = files(compileConsumer.flatMap { it.destinationDirectory }) + brokenRelease
                mainClass.set(consumerClass)
                errorOutput = output
                isIgnoreExitValue = true
                javaLauncher.set(javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(compatibility.javaVersion))
                })
                doLast {
                    check(executionResult.get().exitValue != 0 && output.toString().contains("NoSuchMethodError")) {
                        "The old consumer must reproduce the known broken release linkage failure: $output"
                    }
                }
            }
            compatibilityCheck.configure { dependsOn(negativeControl) }
        }
    }
}
