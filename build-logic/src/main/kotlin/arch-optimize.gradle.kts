/**
 * Applies dependency resolution guardrails.
 *
 * The plugin prefers project modules, fails on dependency conflicts, enables dependency
 * verification, and forces catalog-defined versions to keep builds reproducible.
 */
val allDefinedLibraries = libraries.allDefinedDependencies
configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
        preferProjectModules()
        enableDependencyVerification()
        force(allDefinedLibraries)
    }
}
