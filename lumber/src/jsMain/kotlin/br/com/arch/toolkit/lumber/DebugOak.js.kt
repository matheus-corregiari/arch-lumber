@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "MatchingDeclarationName")

package br.com.arch.toolkit.lumber

/**
 * # DebugOak (JS)
 *
 * The JavaScript-specific implementation of [DebugOak], which delegates all log messages to
 * the native `console` object.
 *
 * It maps each [Lumber.Level] to the most appropriate `console` method:
 * - **Verbose**: `console.log`
 * - **Debug**: `console.log`
 * - **Info**: `console.info`
 * - **Warn**: `console.warn`
 * - **Error**: `console.error`
 * - **Assert**: `console.error`
 *
 * The output includes the level name and the tag to provide context within the browser
 * or Node.js console.
 */
actual open class DebugOak actual constructor() : Lumber.Oak() {

    /**
     * For JS, all log levels are considered loggable by default.
     *
     * @return Always `true`.
     */
    actual override fun isLoggable(tag: String?, level: Lumber.Level) = true

    /**
     * Writes the log message to the JavaScript console.
     *
     * @param level The log level, used to select the `console` method.
     * @param tag The optional log tag.
     * @param message The formatted log message.
     * @param error An optional `Throwable` (currently its stack trace is handled within the message).
     */
    actual override fun log(
        level: Lumber.Level,
        tag: String?,
        message: String,
        error: Throwable?
    ) = when (level) {
        Lumber.Level.Verbose -> console.log("VERBOSE $tag : $message")
        Lumber.Level.Debug -> console.log("DEBUG $tag : $message")
        Lumber.Level.Info -> console.info("INFO $tag : $message")
        Lumber.Level.Warn -> console.warn("WARNING $tag : $message")
        Lumber.Level.Error -> console.error("ERROR $tag : $message")
        Lumber.Level.Assert -> console.error("ASSERT $tag : $message")
    }
}
