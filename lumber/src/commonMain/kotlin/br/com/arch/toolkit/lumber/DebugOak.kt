@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package br.com.arch.toolkit.lumber

/**
 * # DebugOak
 *
 * A platform-specific implementation of [Lumber.Oak] designed for development logging.
 *
 * `DebugOak` provides a sensible default for each target platform, using the standard logging
 * mechanisms available (e.g., Logcat on Android, `console` on JS/Wasm, and colored `println`
 * on JVM and Apple platforms).
 *
 * ## Behavior by Platform:
 *
 * - **Android**: Logs to `android.util.Log`.
 * - **JVM**: Logs to standard output with ANSI colors.
 * - **Apple**: Logs to standard output with ANSI colors.
 * - **JS/Wasm**: Logs to the browser or Node.js `console` object.
 *
 * ## Usage:
 *
 * ```kotlin
 * Lumber.plant(DebugOak())
 * ```
 *
 * @see Lumber.Oak
 */
expect open class DebugOak() : Lumber.Oak {
    /**
     * Determines whether a log message should be output.
     *
     * The implementation of this method is platform-dependent. For example, on Android,
     * it might check the system log level.
     *
     * @param tag The tag associated with the log message.
     * @param level The severity [Lumber.Level].
     * @return `true` if the message should be logged, `false` otherwise.
     */
    override fun isLoggable(
        tag: String?,
        level: Lumber.Level
    ): Boolean

    /**
     * Performs the actual logging using the platform's default mechanism.
     *
     * @param level The severity [Lumber.Level].
     * @param tag The optional tag.
     * @param message The formatted log message.
     * @param error An optional `Throwable`.
     */
    override fun log(
        level: Lumber.Level,
        tag: String?,
        message: String,
        error: Throwable?
    )
}
