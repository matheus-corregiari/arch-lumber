@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "MatchingDeclarationName")

package br.com.arch.toolkit.lumber

/**
 * # DebugTree (Apple - Darwin)
 *
 * This tree prints log messages to the standard output (`println`)
 * with ANSI colors applied to each log [ColoredLog].
 * for better readability in terminal environments.
 *
 * Each [Lumber.Level] is mapped to a distinct color style:
 * - [Lumber.Level.Error]   → Red
 * - [Lumber.Level.Warn]    → Yellow
 * - [Lumber.Level.Info]    → Blue
 * - [Lumber.Level.Debug]   → Green
 * - [Lumber.Level.Verbose] → Gray
 * - [Lumber.Level.Assert]  → Cyan
 *
 * The [isLoggable] method always returns `true`, ensuring all logs
 * are emitted without filtering.
 *
 * ### Example usage:
 * ```kotlin
 * Lumber.plant(DebugTree())
 * Lumber.i("Startup", "Application initialized")
 * ```
 *
 * Console output (with colors applied):
 * ```
 * [Info]-[Startup] -> Application initialized
 * ```
 *
 * @constructor Creates a debug tree that logs with colored output
 * for Apple/Darwin targets.
 * @see Lumber
 * @see Lumber.Oak
 */
actual open class DebugTree : Lumber.Oak() {
    /** Maps [Lumber.Level] to a [ColoredLog] for colored output. */
    private val Lumber.Level.toStyle: ColoredLog
        get() =
            when (this) {
                Lumber.Level.Error -> ColoredLog.Red
                Lumber.Level.Warn -> ColoredLog.Yellow
                Lumber.Level.Info -> ColoredLog.Blue
                Lumber.Level.Debug -> ColoredLog.Green
                Lumber.Level.Verbose -> ColoredLog.Gray
                Lumber.Level.Assert -> ColoredLog.Cyan
            }

    /** Always returns `true`, allowing all logs to be emitted. */
    actual override fun isLoggable(
        tag: String?,
        level: Lumber.Level,
    ) = true

    /**
     * Prints the formatted log message to the standard output with colors.
     *
     * @param level The log level as defined in [Lumber.Level].
     * @param tag An optional tag identifying the log source.
     * @param message The log message content.
     * @param error An optional [Throwable] attached to the log (currently ignored).
     */
    actual override fun log(
        level: Lumber.Level,
        tag: String?,
        message: String,
        error: Throwable?,
    ) {
        val formattedMessage =
            if (tag == null) {
                "[%s] -> %s".format(level.name, message)
            } else {
                "[%s]-[%s] -> %s".format(level.name, tag, message)
            }
        println(formattedMessage.lineSequence().map(level.toStyle::invoke).joinToString("\n"))
    }
}
