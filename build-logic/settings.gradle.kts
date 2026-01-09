@file:Suppress("UnstableApiUsage")

// Setting up all repository plugins
pluginManagement {
    apply(from = "$rootDir/../gradle/repositories.gradle.kts")
    val repositoryList: RepositoryHandler.() -> Unit by extra
    repositories{
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    apply(from = "$rootDir/../gradle/repositories.gradle.kts")
    val repositoryList: RepositoryHandler.() -> Unit by extra
    repositories(repositoryList)
    versionCatalogs { register("libs") { from(files("$rootDir/../gradle/libs.versions.toml")) } }
}
