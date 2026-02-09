@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package br.com.arch.toolkit.lumber

/**
 * # DebugOak (JVM)
 *
 * The JVM-specific implementation of [DebugOak] that logs messages to the standard
 * output (`stdout`) with ANSI color coding.
 *
 * Each log level is assigned a distinct color to improve readability in terminal environments:
 * - **Error**: Red
 * - **Warn**: Yellow
 * - **Info**: Blue
 * - **Debug**: Green
 * - **Verbose**: Gray
 * - **Assert**: Cyan
 *
 * This implementation is ideal for server-side or desktop applications where console
 * output is the primary means of debugging.
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
     * For the JVM implementation, all log levels are considered loggable by default.
     *
     * @return Always `true`.
     */
    actual override fun isLoggable(tag: String?, level: Lumber.Level) = true

    /**
     * Writes the log message to `stdout`, applying ANSI colors based on the log level.
     *
     * The final output is formatted to include the level, tag (if present), and the message.
     *
     * @param level The log level, used to determine the color.
     * @param tag The optional log tag.
     * @param message The formatted log message.
     * @param error An optional `Throwable` (its stack trace is appended to the message).
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

        // Apply style to each line separately
        println(
            formattedMessage.lineSequence().joinToString(
                separator = "\n",
                transform = level.toStyle::invoke
            )
        )
    }
}
