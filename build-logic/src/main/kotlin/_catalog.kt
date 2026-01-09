import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import kotlin.jvm.optionals.getOrNull

internal val Project.libraries: VersionCatalog
    get() = extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")
        ?: error("Cannot find libraries in version catalog!")

internal fun VersionCatalog.version(alias: String) =
    findVersion(alias).getOrNull()?.requiredVersion
        ?: error("Unable to find version with alias: $alias")
