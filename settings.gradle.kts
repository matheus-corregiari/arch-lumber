@file:Suppress("UnstableApiUsage")

pluginManagement {
    apply(from = "$rootDir/gradle/repositories.gradle.kts")
    @Suppress("UNCHECKED_CAST")
    val repositoryList = extra["repositoryList"] as RepositoryHandler.() -> Unit
    repositories(repositoryList)
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    apply(from = "$rootDir/gradle/repositories.gradle.kts")
    @Suppress("UNCHECKED_CAST")
    val repositoryList = extra["repositoryList"] as RepositoryHandler.() -> Unit
    repositories(repositoryList)
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}

include(":lumber")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
