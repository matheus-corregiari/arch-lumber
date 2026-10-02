import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaToolchainService

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

listOf("1.1.0", "1.4.4").forEach { releasedVersion ->
    val suffix = releasedVersion.replace(".", "_")
    val released = configurations.create("consumerCompile$suffix")
    dependencies.add(released.name, "io.github.matheus-corregiari:arch-lumber-jvm:$releasedVersion")
    val consumerClass = if (releasedVersion == "1.1.0") "Consumer" else "CurrentConsumer"
    val compileConsumer = tasks.register<JavaCompile>("compileConsumer$suffix") {
        source(rootProject.file("compatibility/$consumerClass.java"))
        classpath = released
        destinationDirectory.set(layout.buildDirectory.dir("compatibility/$releasedVersion"))
        javaCompiler.set(javaToolchains.compilerFor { languageVersion.set(JavaLanguageVersion.of(21)) })
        options.release.set(21)
    }
    listOf("jvm", "android").forEach { target ->
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
            javaLauncher.set(javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) })
        }
        compatibilityCheck.configure { dependsOn(runConsumer) }
    }
    if (releasedVersion == "1.1.0") {
        val brokenRelease = configurations.create("consumerBrokenRuntime")
        dependencies.add(brokenRelease.name, "io.github.matheus-corregiari:arch-lumber-jvm:1.4.4")
        val negativeControl = tasks.register<JavaExec>("consumerNegativeControl") {
            dependsOn(compileConsumer)
            val output = java.io.ByteArrayOutputStream()
            classpath = files(compileConsumer.flatMap { it.destinationDirectory }) + brokenRelease
            mainClass.set("Consumer")
            errorOutput = output
            isIgnoreExitValue = true
            javaLauncher.set(javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) })
            doLast {
                check(executionResult.get().exitValue != 0 && output.toString().contains("NoSuchMethodError")) {
                    "The old consumer must reproduce the published 1.4.4 linkage failure: $output"
                }
            }
        }
        compatibilityCheck.configure { dependsOn(negativeControl) }
    }
}
