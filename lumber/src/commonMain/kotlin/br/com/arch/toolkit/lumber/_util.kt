@file:Suppress("ktlint:standard:filename")

package br.com.arch.toolkit.lumber

/**
 * A set of classes to ignore when attempting to determine the calling class or file
 * for automatic tagging.
 */
internal val fqcnIgnore = setOf(
    Lumber::class,
    Lumber.Level::class,
    Lumber.OakWood::class,
    Lumber.Oak::class,
    DebugOak::class
)

/**
 * A simple platform-agnostic string formatter.
 *
 * It supports basic `%s` (string) and `%d` (integer) placeholders.
 * Placeholders inside argument values remain literal; extra arguments are ignored.
 * With no arguments, the template is returned unchanged.
 *
 * @receiver The template string containing placeholders.
 * @param args The arguments to inject into the template.
 * @return The formatted string.
 * @throws IllegalStateException if nonempty arguments are fewer than placeholders.
 */
internal fun String.format(vararg args: Any?): String {
    // Ignore in case of no arguments, there is nothing to do
    if (args.isEmpty()) return this

    // Find all matches and verify if the number of matches is enough to format properly
    val matches = Regex("%[sd]").findAll(this).toList()
    if (matches.size > args.size) {
        error("Wrong number of arguments, expected ${matches.size}, actual ${args.size}")
    }

    // Match only the template: placeholders inside argument values are literal text.
    return buildString {
        var offset = 0
        matches.forEachIndexed { index, match ->
            append(this@format, offset, match.range.first)
            val argument = args[index]
            val formatted =
                if (match.value == "%d") (argument as? Number).toString() else argument.toString()
            append(formatted)
            offset = match.range.last + 1
        }
        append(this@format, offset, this@format.length)
    }
}

/**
 * Converts a string to camelCase.
 *
 * It splits the string by spaces, underscores, or hyphens and joins the parts
 * with the first letter of each part (except the first one) capitalized.
 *
 * @receiver The string to convert.
 * @return The camelCased string.
 */
internal fun String.camelcase(): String {
    val parts = trim().split(" ", "_", "-")
    return if (parts.size == 1) {
        parts.first()
    } else {
        parts.joinToString("") { part -> part.lowercase().replaceFirstChar { it.titlecase() } }
    }
}
