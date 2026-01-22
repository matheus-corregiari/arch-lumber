@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package br.com.arch.toolkit.lumber

/**
 * # DebugTree (JVM)
 *
 * JVM-specific implementation of [Lumber.Oak] that prints logs to the
 * standard output (`stdout`), with ANSI colors applied to each log [ColoredLog].
 *
 * Each [Lumber.Level] is mapped to a distinct color:
 * - [Lumber.Level.Error]   → Red
 * - [Lumber.Level.Warn]    → Yellow
 * - [Lumber.Level.Info]    → Blue
 * - [Lumber.Level.Debug]   → Green
 * - [Lumber.Level.Verbose] → Gray
 * - [Lumber.Level.Assert]  → Cyan
 *
 * ## Example
 * ```kotlin
 * // Plant the DebugTree for JVM
 * Lumber.plant(DebugTree())
 *
 * Lumber.info("Application started")
 * Lumber.debug("Processing request id=%d", 42)
 * Lumber.error(Exception("boom"), "Operation failed")
 * ```
 *
 * The output will be printed to the terminal with colors applied per log level.
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

    /**
     * Always returns `true` for JVM.
     *
     * Unlike Android, there’s no system filter here.
     * All log levels are considered loggable.
     *
     * @param tag The log tag (ignored in filtering).
     * @param level The log level.
     */
    actual override fun isLoggable(
        tag: String?,
        level: Lumber.Level,
    ): Boolean = true

    /**
     * Prints a log message to the console with a color based on [level].
     *
     * @param level The log level (controls formatting and color).
     * @param tag Optional tag. If non-null, it will be included in the prefix.
     * @param message The message to log.
     * @param error Optional throwable. Currently not printed; consider extending if needed.
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

        // Apply style to each line separately
        println(formattedMessage.lineSequence().map(level.toStyle::invoke).joinToString("\n"))
    }
}
