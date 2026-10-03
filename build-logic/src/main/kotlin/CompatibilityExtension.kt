/** A consumer compiled against the latest release tag within its API generation. */
data class PublishedConsumer(val mainClass: String, val beforeVersion: String? = null)

/** Per-library inputs for the shared published-consumer convention. */
open class CompatibilityExtension {
    var consumers: List<PublishedConsumer> = emptyList()
    var brokenVersion: String? = null
    var sourceDirectory: String = "src/compatibility/java"
    var javaVersion: Int = 21
}
