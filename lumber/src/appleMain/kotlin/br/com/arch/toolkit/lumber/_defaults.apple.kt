@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "ktlint:standard:filename")

package br.com.arch.toolkit.lumber

internal actual const val MAX_LOG_LENGTH: Int = 8000
internal actual const val MAX_TAG_LENGTH: Int = 30
private val METHOD_REGEX =
    "(?<full>(?:[a-zA-Z]+\\.)+(?<className>[a-zA-Z]+))#(?<method>[a-zA-Z ]+)\\(".toRegex()

internal actual fun defaultTag(): String? {
    val ignore = fqcnIgnore.map { it.qualifiedName }
    return METHOD_REGEX
        .findAll(Throwable("Default Log Exception").stackTraceToString())
        .mapNotNull(::extractData)
        .filter { (full, _, _) -> full !in ignore }
        .map { (_, className, method) -> "$className:$method" }
        .firstOrNull()
        ?.chunked(MAX_TAG_LENGTH)
        ?.first()
}

private fun extractData(match: MatchResult): Triple<String, String, String>? {
    val group = (match.groups as MatchNamedGroupCollection)
    val full = group["full"]
    val className = group["className"]
    val method = group["method"]
    if (full == null || className == null || method == null) return null
    return Triple(full.value, className.value, method.value.camelcase())
}
