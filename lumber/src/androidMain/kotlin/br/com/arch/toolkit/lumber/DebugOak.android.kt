@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package br.com.arch.toolkit.lumber

import android.util.Log

/**
 * Android [DebugOak] backed by [Log].
 *
 * Entries are mapped to the corresponding Android log priority and written to Logcat.
 *
 * @see Lumber.Oak
 */
actual open class DebugOak : Lumber.Oak() {
    /**
     * Delegates loggability checks to [Log.isLoggable] using the mapped Android priority.
     *
     * @param tag Optional tag, can be `null` (Android will use `"null"`).
     * @param level The logging level.
     * @return `true` if Android allows logging at this level, `false` otherwise.
     */
    actual override fun isLoggable(
        tag: String?,
        level: Lumber.Level
    ) = Log.isLoggable(
        tag,
        when (level) {
            Lumber.Level.Verbose -> Log.VERBOSE
            Lumber.Level.Debug -> Log.DEBUG
            Lumber.Level.Info -> Log.INFO
            Lumber.Level.Warn -> Log.WARN
            Lumber.Level.Error -> Log.ERROR
            Lumber.Level.Assert -> Log.ASSERT
        }
    )

    /**
     * Writes the entry to the matching Android [Log] method.
     *
     * @param level The logging level.
     * @param tag Optional tag (may be `null`).
     * @param message The message to log.
     * @param error Optional throwable to log.
     */
    actual override fun log(
        level: Lumber.Level,
        tag: String?,
        message: String,
        error: Throwable?
    ) {
        when (level) {
            Lumber.Level.Verbose -> Log.v(tag, message)
            Lumber.Level.Debug -> Log.d(tag, message)
            Lumber.Level.Info -> Log.i(tag, message)
            Lumber.Level.Warn -> Log.w(tag, message)
            Lumber.Level.Error -> Log.e(tag, message)
            Lumber.Level.Assert -> Log.wtf(tag, message)
        }
    }
}