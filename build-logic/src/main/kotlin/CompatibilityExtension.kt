/** A consumer source compiled against one released Maven publication. */
data class PublishedConsumer(val version: String, val mainClass: String)

/** Per-library inputs for the shared published-consumer convention. */
open class CompatibilityExtension {
    var consumers: List<PublishedConsumer> = emptyList()
    var brokenVersion: String? = null
    var sourceDirectory: String = "src/compatibility/java"
    var javaVersion: Int = 21
}
