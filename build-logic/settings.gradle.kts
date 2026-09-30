@file:Suppress("UnstableApiUsage")

// Setting up all repository plugins

pluginManagement {
    apply(from = "$rootDir/../gradle/repositories.gradle.kts")
    @Suppress("UNCHECKED_CAST")
    val repositoryList = extra["repositoryList"] as RepositoryHandler.() -> Unit
    repositories(repositoryList)
}

dependencyResolutionManagement {
    apply(from = "$rootDir/../gradle/repositories.gradle.kts")
    @Suppress("UNCHECKED_CAST")
    val repositoryList = extra["repositoryList"] as RepositoryHandler.() -> Unit
    repositories(repositoryList)
    versionCatalogs { register("libs") { from(files("$rootDir/../gradle/libs.versions.toml")) } }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
