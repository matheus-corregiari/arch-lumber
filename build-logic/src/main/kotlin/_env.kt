@file:Suppress("ktlint:standard:filename")

import org.gradle.api.Project

internal fun Project.env(name: String) =
    System
        .getenv(name)
        .orEmpty()
        .ifBlank { properties[name]?.toString().orEmpty() }
