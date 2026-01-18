val allDefinedLibraries = libraries.allDefinedDependencies
configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
        preferProjectModules()
        enableDependencyVerification()
        force(allDefinedLibraries)
    }
}
