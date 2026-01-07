@file:Suppress("UnstableApiUsage")

pluginManagement {
    apply(from = "$rootDir/gradle/repositories.gradle.kts")
    val repositoryList: RepositoryHandler.() -> Unit by extra
    repositories(repositoryList)
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    apply(from = "$rootDir/gradle/repositories.gradle.kts")
    val repositoryList: RepositoryHandler.() -> Unit by extra
    repositories(repositoryList)
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}

rootProject.name = "Arch Lumber"
include(":lumber")
