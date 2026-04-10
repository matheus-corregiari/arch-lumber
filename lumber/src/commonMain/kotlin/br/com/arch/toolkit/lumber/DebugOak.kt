@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package br.com.arch.toolkit.lumber

/**
 * Default development [Lumber.Oak] for each supported platform.
 *
 * `DebugOak` routes logs to the platform-native debug sink:
 * - Android uses `android.util.Log`
 * - JVM and Apple targets write colored console output
 * - JS and WasmJS use `console`
 *
 * ```kotlin
 * Lumber.plant(DebugOak())
 * ```
 *
 * @see Lumber.Oak
 */
expect open class DebugOak() : Lumber.Oak {
    /**
     * Returns whether a log entry should be emitted on the current platform.
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
     * Writes the final log entry using the platform's default debug mechanism.
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
