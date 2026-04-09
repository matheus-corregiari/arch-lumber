@file:Suppress("unused", "TooManyFunctions")

package br.com.arch.toolkit.lumber

import br.com.arch.toolkit.lumber.Lumber.Level.Assert
import br.com.arch.toolkit.lumber.Lumber.Level.Debug
import br.com.arch.toolkit.lumber.Lumber.Level.Error
import br.com.arch.toolkit.lumber.Lumber.Level.Info
import br.com.arch.toolkit.lumber.Lumber.Level.Verbose
import br.com.arch.toolkit.lumber.Lumber.Level.Warn
import br.com.arch.toolkit.lumber.Lumber.OakWood.plant
import br.com.arch.toolkit.lumber.Lumber.OakWood.uproot
import br.com.arch.toolkit.lumber.Lumber.OakWood.uprootAll
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

/**
 * Main entry point for Arch Lumber logging.
 *
 * Plant one or more [Oak] instances once, then use the static-style API on [Lumber] to emit
 * messages across platforms.
 *
 * ```kotlin
 * Lumber.plant(DebugOak())
 * Lumber.tag("Auth").info("Session created")
 * ```
 *
 * [Lumber] keeps one-shot tag and length overrides scoped to the next log call on each planted
 * [Oak].
 *
 * @see Oak
 * @see DebugOak
 * @see Level
 */
class Lumber private constructor() {
    init {
        throw AssertionError("No instances allowed.")
    }

    /**
     * Severity used by [Oak] implementations to filter and format log entries.
     */
    enum class Level {
        /** For detailed, fine-grained debugging information. Typically disabled in production. */
        Verbose,

        /** For developer-facing messages to debug application flow. */
        Debug,

        /** For high-level events that mark the application's lifecycle. */
        Info,

        /** For potential issues or unexpected events that do not halt execution. */
        Warn,

        /** For errors and exceptions that impact functionality but may be recoverable. */
        Error,

        /** For critical, unrecoverable failures. Stands for "What a Terrible Failure." */
        Assert
    }

    /**
     * A single logging destination.
     *
     * Extend [Oak] to send log entries to a console, file, analytics backend, or any other sink.
     * Implement [isLoggable] for filtering and [log] for the final write step.
     *
     * One-shot configuration methods such as [tag], [quiet], [maxLogLength], and [maxTagLength]
     * apply only to the next log call for the current [Oak].
     *
     * @see OakWood
     * @see DebugOak
     */
    abstract class Oak {
        private val explicitTag = ThreadSafe<String?>()
        private val explicitQuiet = ThreadSafe<Boolean?>()
        private val explicitMaxLogLength = ThreadSafe<Int?>()
        private val explicitMaxTagLength = ThreadSafe<Int?>()

        /**
         * A one-time tag for the next log message.
         *
         * This value is consumed after the next log call, even if the message is not ultimately logged.
         *
         * @see tag
         */
        protected open val tag: String?
            get() =
                explicitTag
                    .get()
                    .takeIf { it.isNullOrBlank().not() }
                    ?.also { explicitTag.remove() }

        /**
         * A one-time flag that suppresses the next log call for this [Oak].
         *
         * This value is consumed after the next log call.
         *
         * @see quiet
         */
        protected open val quiet: Boolean
            get() = explicitQuiet.get()?.also { explicitQuiet.remove() } == true

        /**
         * A one-time maximum length for the tag on the next log message.
         *
         * This value is consumed after the next log call.
         *
         * @see maxTagLength
         */
        protected open val maxTagLength: Int?
            get() = explicitMaxTagLength.get()?.also { explicitMaxTagLength.remove() }

        /**
         * A one-time maximum length for the log message on the next log call.
         *
         * This value is consumed after the next log call.
         *
         * @see maxLogLength
         */
        protected open val maxLogLength: Int?
            get() = explicitMaxLogLength.get()?.also { explicitMaxLogLength.remove() }

        /**
         * Sets a one-time tag for the next log message.
         *
         * The tag is applied only to the immediate next log and is then cleared.
         *
         * @param tag The tag string.
         * @return The current `Oak` instance for chaining.
         */
        open fun tag(tag: String): Oak {
            explicitTag.set(tag.trim())
            return this
        }

        /**
         * Suppresses the next log message for this `Oak`.
         *
         * @param quiet `true` to suppress the log, `false` otherwise.
         * @return The current `Oak` instance for chaining.
         */
        open fun quiet(quiet: Boolean): Oak {
            explicitQuiet.set(quiet)
            return this
        }

        /**
         * Sets a one-time maximum length for the next log message.
         *
         * If a formatted message exceeds this length, it will be split into multiple chunks.
         *
         * @param length The maximum number of characters per log entry. Must be positive.
         * @return The current `Oak` instance for chaining.
         */
        open fun maxLogLength(length: Int): Oak {
            require(length > 0) { "length must be positive" }
            explicitMaxLogLength.set(length)
            return this
        }

        /**
         * Sets a one-time maximum length for the tag on the next log message.
         *
         * If the tag exceeds this length, it will be truncated.
         *
         * @param length The maximum length for the tag. Must be positive.
         * @return The current `Oak` instance for chaining.
         */
        open fun maxTagLength(length: Int): Oak {
            require(length > 0) { "length must be positive" }
            explicitMaxTagLength.set(length)
            return this
        }

        //region Verbose

        /** Logs a [Verbose] message. */
        open fun verbose(message: String, vararg args: Any?) =
            log(level = Verbose, message = message, args = args)

        /** Logs a [Verbose] throwable using its stack trace as the message body. */
        open fun verbose(error: Throwable) = log(level = Verbose, error = error)

        /** Logs a [Verbose] error with a message. */
        open fun verbose(error: Throwable, message: String, vararg args: Any?) =
            log(level = Verbose, error = error, message = message, args = args)
        //endregion

        //region Debug

        /** Logs a [Debug] message. */
        open fun debug(message: String, vararg args: Any?) =
            log(level = Debug, message = message, args = args)

        /** Logs a [Debug] throwable using its stack trace as the message body. */
        open fun debug(error: Throwable) = log(level = Debug, error = error)

        /** Logs a [Debug] error with a message. */
        open fun debug(error: Throwable, message: String, vararg args: Any?) =
            log(level = Debug, error = error, message = message, args = args)
        //endregion

        //region Info

        /** Logs an [Info] message. */
        open fun info(message: String, vararg args: Any?) =
            log(level = Info, message = message, args = args)

        /** Logs an [Info] throwable using its stack trace as the message body. */
        open fun info(error: Throwable) = log(level = Info, error = error)

        /** Logs an [Info] error with a message. */
        open fun info(error: Throwable, message: String, vararg args: Any?) =
            log(level = Info, error = error, message = message, args = args)
        //endregion

        //region Warn

        /** Logs a [Warn] message. */
        open fun warn(message: String, vararg args: Any?) =
            log(level = Warn, message = message, args = args)

        /** Logs a [Warn] throwable using its stack trace as the message body. */
        open fun warn(error: Throwable) = log(level = Warn, error = error)

        /** Logs a [Warn] error with a message. */
        open fun warn(error: Throwable, message: String, vararg args: Any?) =
            log(level = Warn, error = error, message = message, args = args)
        //endregion

        //region Error

        /** Logs an [Error] message. */
        open fun error(message: String, vararg args: Any?) =
            log(level = Error, message = message, args = args)

        /** Logs an [Error] throwable using its stack trace as the message body. */
        open fun error(error: Throwable) = log(level = Error, error = error)

        /** Logs an [Error] error with a message. */
        open fun error(error: Throwable, message: String, vararg args: Any?) =
            log(level = Error, error = error, message = message, args = args)
        //endregion

        //region Assert

        /** Logs an [Assert] message. */
        open fun wtf(message: String, vararg args: Any?) =
            log(level = Assert, message = message, args = args)

        /** Logs an [Assert] throwable using its stack trace as the message body. */
        open fun wtf(error: Throwable) = log(level = Assert, error = error)

        /** Logs an [Assert] error with a message. */
        open fun wtf(error: Throwable, message: String, vararg args: Any?) =
            log(level = Assert, error = error, message = message, args = args)
        //endregion

        //region Raw Log

        /** Logs a message with a specific [Level] and optional arguments. */
        open fun log(level: Level, message: String, vararg args: Any?) =
            log(level = level, error = null, message = message, args = args)

        /** Logs an error with a specific [Level]. */
        open fun log(level: Level, error: Throwable) =
            log(level = level, error = error, message = null, args = emptyArray())

        /**
         * Lowest-level entry point before formatting, filtering, and chunking are applied.
         */
        open fun log(level: Level, error: Throwable?, message: String?, vararg args: Any?) =
            prepareLog(level = level, error = error, message = message, args = args)
        //endregion

        /**
         * Determines whether a message should be logged.
         *
         * Override this to implement custom filtering logic, such as logging only messages
         * above a certain severity in production.
         *
         * @param tag The tag associated with the message, or `null` if none was provided.
         * @param level The severity [Level] of the message.
         * @return `true` if the message should be logged, `false` otherwise.
         */
        abstract fun isLoggable(tag: String?, level: Level): Boolean

        /**
         * Writes the final formatted log entry.
         *
         * The [message] parameter already contains the final formatted text. Implementations should
         * write it to their destination without reformatting shared contract behavior.
         *
         * @param level The severity [Level] of the message.
         * @param tag The final tag, which may be `null`.
         * @param message The formatted and finalized log message.
         * @param error An optional `Throwable` associated with the log.
         */
        protected abstract fun log(level: Level, tag: String?, message: String, error: Throwable?)

        private fun prepareLog(
            level: Level,
            error: Throwable?,
            message: String?,
            vararg args: Any?
        ) {
            // Consume tag even when the entry is filtered so the next call starts clean.
            val tagLimit = maxTagLength ?: MAX_TAG_LENGTH
            val currentTag = (tag ?: defaultTag())?.take(tagLimit)

            if (!isLoggable(currentTag, level) || quiet) return

            var formattedMessage = message.orEmpty().format(*args)
            if (formattedMessage.isBlank()) {
                // Drop empty entries unless there is a throwable to print.
                formattedMessage = error?.stackTraceToString() ?: return
            } else if (error != null) {
                formattedMessage += "\n\n${error.stackTraceToString()}"
            }

            val logLength = maxLogLength ?: MAX_LOG_LENGTH
            if (formattedMessage.length <= logLength) {
                log(level = level, tag = currentTag, message = formattedMessage, error = error)
            } else {
                formattedMessage.chunked(logLength).forEachIndexed { index, part ->
                    val newTag = currentTag?.let { "$it #$index" } ?: "#$index"
                    log(level = level, tag = newTag, message = part.trimStart('\n'), error = error)
                }
            }
        }
    }

    /**
     * Dispatcher that forwards each [Lumber] call to every planted [Oak].
     *
     * @see plant
     * @see uproot
     * @see uprootAll
     */
    companion object OakWood : Oak() {
        private val treesRef = atomic<Set<Oak>>(emptySet())
        private val trees by treesRef

        /** Number of currently planted [Oak] instances. */
        val treeCount: Int get() = trees.size

        override fun log(level: Level, tag: String?, message: String, error: Throwable?) =
            kotlin.error("OakWood does not implement direct logging; it is a dispatcher.")

        override fun log(level: Level, error: Throwable?, message: String?, vararg args: Any?) =
            trees.forEach { it.log(level = level, error = error, message = message, args = args) }

        override fun isLoggable(tag: String?, level: Level) =
            trees.any { it.isLoggable(tag, level) }

        override fun tag(tag: String): Oak {
            trees.forEach { it.tag(tag) }
            return this
        }

        override fun quiet(quiet: Boolean): Oak {
            trees.forEach { it.quiet(quiet) }
            return this
        }

        override fun maxLogLength(length: Int): Oak {
            require(length > 0) { "length must be positive" }
            trees.forEach { it.maxLogLength(length) }
            return this
        }

        override fun maxTagLength(length: Int): Oak {
            require(length > 0) { "length must be positive" }
            trees.forEach { it.maxTagLength(length) }
            return this
        }

        /**
         * Adds one or more [Oak] instances to the logging system.
         *
         * @param tree The first [Oak] to plant.
         * @param trees Additional [Oak] instances to plant.
         * @throws IllegalArgumentException if [OakWood] itself is planted.
         */
        fun plant(tree: Oak, vararg trees: Oak) = apply {
            val allTrees = listOf(tree, *trees)
            allTrees.forEach { require(it !== this) { "Cannot plant Lumber itself." } }
            treesRef.update { it + allTrees }
        }

        /**
         * Removes a specific [Oak] from the logging system.
         *
         * @param tree The [Oak] instance to remove.
         */
        fun uproot(tree: Oak) = apply { treesRef.update { it - tree } }

        /**
         * Removes every planted [Oak].
         */
        fun uprootAll() = apply { treesRef.value = emptySet() }

        /**
         * Returns a snapshot of the currently planted [Oak] instances.
         */
        fun forest(): List<Oak> = trees.toList()
    }
}