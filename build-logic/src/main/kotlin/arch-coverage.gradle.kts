/** Shared coverage policy for module reports and root aggregation. */
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
    id("org.jetbrains.kotlinx.kover")
}

kover {
    reports {
        filters {
            excludes {
                // Android-generated constants/resources contain no handwritten behavior.
                classes("*.BuildConfig", "*.R", "*.R$*")
            }
        }
        total {
            verify {
                rule("Minimum line coverage") {
                    minBound(providers.gradleProperty("ci.coverage.lines").get().toInt(), CoverageUnit.LINE)
                }
                rule("Minimum instruction coverage") {
                    minBound(providers.gradleProperty("ci.coverage.instructions").get().toInt(), CoverageUnit.INSTRUCTION)
                }
                rule("Minimum branch coverage") {
                    minBound(providers.gradleProperty("ci.coverage.branches").get().toInt(), CoverageUnit.BRANCH)
                }
            }
        }
    }
}
