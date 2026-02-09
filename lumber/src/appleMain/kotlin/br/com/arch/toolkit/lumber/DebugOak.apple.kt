@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "MatchingDeclarationName")

package br.com.arch.toolkit.lumber

/**
 * # DebugOak (Apple)
 *
 * The Apple-specific implementation of [DebugOak] (iOS, macOS, etc.) that logs messages
 * to the standard output using `println` with ANSI color coding.
 *
 * Each log level is assigned a distinct color for better visibility in the Xcode console
 * or terminal:
 * - **Error**: Red
 * - **Warn**: Yellow
 * - **Info**: Blue
 * - **Debug**: Green
 * - **Verbose**: Gray
 * - **Assert**: Cyan
 *
 * This implementation maps each [Lumber.Level] to a specific style via [ColoredLog].
 */
actual open class DebugOak : Lumber.Oak() {

    /** Maps a `Lumber.Level` to its corresponding ANSI color style. */
    private val Lumber.Level.toStyle: ColoredLog
        get() = when (this) {
            Lumber.Level.Error -> ColoredLog.Red
            Lumber.Level.Warn -> ColoredLog.Yellow
            Lumber.Level.Info -> ColoredLog.Blue
            Lumber.Level.Debug -> ColoredLog.Green
            Lumber.Level.Verbose -> ColoredLog.Gray
            Lumber.Level.Assert -> ColoredLog.Cyan
        }

    /**
     * For Apple platforms, all log levels are considered loggable by default.
     *
     * @return Always `true`.
     */
    actual override fun isLoggable(tag: String?, level: Lumber.Level) = true

    /**
     * Writes the log message to the standard output, applying ANSI colors based on the level.
     *
     * @param level The log level, used to determine the color.
     * @param tag The optional log tag.
     * @param message The formatted log message.
     * @param error An optional `Throwable` (its stack trace is included in the formatted message).
     */
    actual override fun log(
        level: Lumber.Level,
        tag: String?,
        message: String,
        error: Throwable?
    ) {
        val formattedMessage =
            if (tag == null) {
                "[%s] -> %s".format(level.name, message)
            } else {
                "[%s]-[%s] -> %s".format(level.name, tag, message)
            }
        // Apply style to each line separately to preserve formatting
        println(
            formattedMessage.lineSequence().joinToString(
                separator = "\n",
                transform = level.toStyle::invoke
            )
        )
    }
}
