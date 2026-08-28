@file:Suppress("ktlint:standard:filename")

import org.gradle.api.Project

fun Project.env(name: String) = System.getenv(name).orEmpty()
    .ifBlank { findProperty(name)?.toString().orEmpty() }
