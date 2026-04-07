package br.com.arch.toolkit.lumber

import br.com.arch.toolkit.lumber.ColoredLog.Companion.RESET

/**
 * # ColoredLog
 *
 * An inline value class representing an ANSI-colored log line.
 *
 * This class wraps an ANSI color escape code and provides an [invoke] operator to
 * wrap a given string with that color and reset it at the end.
 *
 * @property color The ANSI escape sequence for the color.
 */
internal value class ColoredLog private constructor(
    private val color: String
) {
    /**
     * Wraps the provided [text] with the [color] escape sequence and appends [RESET].
     *
     * @param text The text to colorize.
     * @return The colorized string.
     */
    internal operator fun invoke(text: String): String = "$color$text$RESET"

    companion object {
        internal const val RESET = "\u001B[0m"

        internal val Red = ColoredLog("\u001B[31m")
        internal val Green = ColoredLog("\u001B[32m")
        internal val Yellow = ColoredLog("\u001B[33m")
        internal val Blue = ColoredLog("\u001B[34m")
        internal val Cyan = ColoredLog("\u001B[36m")
        internal val Gray = ColoredLog("\u001B[90m")
    }
}
