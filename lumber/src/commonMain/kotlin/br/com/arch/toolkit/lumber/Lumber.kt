@file:Suppress("unused", "TooManyFunctions")

package br.com.arch.toolkit.lumber

import br.com.arch.toolkit.lumber.Lumber.Level.Assert
import br.com.arch.toolkit.lumber.Lumber.Level.Debug
import br.com.arch.toolkit.lumber.Lumber.Level.Error
import br.com.arch.toolkit.lumber.Lumber.Level.Info
import br.com.arch.toolkit.lumber.Lumber.Level.Verbose
import br.com.arch.toolkit.lumber.Lumber.Level.Warn
import br.com.arch.toolkit.lumber.Lumber.OakWood.plant
import br.com.arch.toolkit.lumber.Lumber.OakWood.tag
import br.com.arch.toolkit.lumber.Lumber.OakWood.uproot
import br.com.arch.toolkit.lumber.Lumber.OakWood.uprootAll
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlin.jvm.JvmSynthetic

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
 * [Lumber] keeps one-shot options scoped to the next log call on the current logging facade.
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
     * Use [tag] and one-shot options for direct destination logging, or [OakWood] to dispatch
     * to the planted forest.
     *
     * @see OakWood
     * @see DebugOak
     */
    abstract class Oak {
        private val atomicOptions = atomic(LogOptions())

        /** Consumes the legacy one-time tag for this destination. */
        protected open val tag: String?
            get() = consumeOption({ it.tag }, { it.copy(tag = null) })

        /** Consumes the one-time suppression flag for this destination. */
        protected open val quiet: Boolean
            get() = consumeOption({ it.quiet }, { it.copy(quiet = false) })

        /** Consumes the one-time maximum message length, or returns null when unset. */
        protected open val maxLogLength: Int?
            get() = consumeOption({ it.maxLogLength }, { it.copy(maxLogLength = null) })

        /** Consumes the one-time maximum tag length, or returns null when unset. */
        protected open val maxTagLength: Int?
            get() = consumeOption({ it.maxTagLength }, { it.copy(maxTagLength = null) })

        /** Returns a facade with a fixed tag for this destination. */
        open fun tag(tag: String): Oak = TaggableOak(tag.trim(), logDestination, consumeOptions())

        /** Suppresses the next message on this destination. */
        open fun quiet(quiet: Boolean): Oak = apply {
            atomicOptions.update { it.copy(quiet = quiet) }
        }

        /** Sets the maximum length of the next message on this destination. */
        open fun maxLogLength(length: Int): Oak = apply {
            require(length > 0) { "length must be positive" }
            atomicOptions.update { it.copy(maxLogLength = length) }
        }

        /** Sets the maximum tag length for the next message on this destination. */
        open fun maxTagLength(length: Int): Oak = apply {
            require(length > 0) { "length must be positive" }
            atomicOptions.update { it.copy(maxTagLength = length) }
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
        open fun log(
            level: Level,
            error: Throwable?,
            message: String?,
            vararg args: Any?
        ) = logWithOptions(level, error, message, consumeOptions(), *args)
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

        internal open val logDestination: Oak get() = this

        internal fun writeLog(level: Level, tag: String?, message: String, error: Throwable?) =
            log(level, tag, message, error)

        internal open fun logWithOptions(
            level: Level,
            error: Throwable?,
            message: String?,
            options: LogOptions,
            vararg args: Any?
        ) = prepareLog(level, error, message, options.tag, options, *args)

        internal fun consumeOptions(): LogOptions = LogOptions(
            tag = tag,
            quiet = quiet,
            maxLogLength = maxLogLength,
            maxTagLength = maxTagLength
        )

        internal fun initializeOptions(options: LogOptions) {
            atomicOptions.value = options
        }

        @Suppress("LongParameterList")
        internal fun prepareLog(
            level: Level,
            error: Throwable?,
            message: String?,
            tag: String?,
            options: LogOptions,
            vararg args: Any?
        ) {
            val currentTag = (tag?.takeIf { it.isNotBlank() } ?: defaultTag())
                ?.take(options.maxTagLength ?: MAX_TAG_LENGTH)

            if (!isLoggable(currentTag, level) || options.quiet) return

            var formattedMessage = message.orEmpty().format(*args)
            if (formattedMessage.isBlank()) {
                // Drop empty entries unless there is a throwable to print.
                formattedMessage = error?.stackTraceToString() ?: return
            } else if (error != null) {
                formattedMessage += "\n\n${error.stackTraceToString()}"
            }

            val maxLength = options.maxLogLength ?: MAX_LOG_LENGTH
            if (formattedMessage.length <= maxLength) {
                log(level = level, tag = currentTag, message = formattedMessage, error = error)
            } else {
                formattedMessage.chunked(maxLength).forEachIndexed { index, part ->
                    val newTag = currentTag?.let { "$it #$index" } ?: "#$index"
                    log(level = level, tag = newTag, message = part.trimStart('\n'), error = error)
                }
            }
        }

        private fun <T> consumeOption(
            read: (LogOptions) -> T,
            clear: (LogOptions) -> LogOptions
        ): T {
            while (true) {
                val options = atomicOptions.value
                if (atomicOptions.compareAndSet(options, clear(options))) return read(options)
            }
        }
    }

    internal data class LogOptions(
        val tag: String? = null,
        val quiet: Boolean = false,
        val maxLogLength: Int? = null,
        val maxTagLength: Int? = null
    )

    /** Retains the published 1.2-1.4 JVM name and fluent return types. */
    class TaggedLumber internal constructor(tag: String, initialOptions: LogOptions) :
        TaggableOak(tag, OakWood, initialOptions) {

        override fun tag(tag: String): TaggedLumber = TaggedLumber(tag.trim(), consumeOptions())

        override fun quiet(quiet: Boolean): TaggedLumber = apply { super.quiet(quiet) }

        override fun maxLogLength(length: Int): TaggedLumber = apply { super.maxLogLength(length) }

        override fun maxTagLength(length: Int): TaggedLumber = apply { super.maxTagLength(length) }

        override fun isLoggable(tag: String?, level: Level): Boolean = super.isLoggable(tag, level)

        @JvmSynthetic
        public override fun log(level: Level, tag: String?, message: String, error: Throwable?) =
            super.log(level, tag, message, error)

        override fun log(level: Level, error: Throwable?, message: String?, vararg args: Any?) =
            super.log(level, error, message, *args)
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
            super.log(level, error, message, *args)

        override fun logWithOptions(
            level: Level,
            error: Throwable?,
            message: String?,
            options: LogOptions,
            vararg args: Any?
        ) = dispatchLog(level, error, message, options.tag, options, *args)

        override fun isLoggable(tag: String?, level: Level) =
            trees.any { it.isLoggable(tag, level) }

        /**
         * Returns a tagged logging facade backed by the current forest.
         *
         * The returned facade keeps the tag for every log call. One-shot options configured before
         * [tag] are transferred to the returned facade and consumed by its next log call.
         */
        override fun tag(tag: String): TaggedLumber = TaggedLumber(
            tag = tag.trim(),
            initialOptions = consumeOptions()
        )

        /**
         * Suppresses the next log message emitted through [Lumber].
         */
        override fun quiet(quiet: Boolean): OakWood = apply { super.quiet(quiet) }

        /** Sets a one-time maximum message length for the forest. */
        override fun maxLogLength(length: Int): OakWood = apply { super.maxLogLength(length) }

        /** Sets a one-time maximum tag length for the forest. */
        override fun maxTagLength(length: Int): OakWood = apply { super.maxTagLength(length) }

        /**
         * Adds one or more [Oak] instances to the logging system.
         *
         * @param tree The first [Oak] to plant.
         * @param trees Additional [Oak] instances to plant.
         * @throws IllegalArgumentException if [OakWood] itself is planted.
         */
        fun plant(tree: Oak, vararg trees: Oak) = apply {
            val allTrees = listOf(tree, *trees)
            allTrees.forEach {
                require(it !== this) { "Cannot plant Lumber itself." }
                require(it !is TaggableOak) { "Cannot plant tagged Lumber." }
            }
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

        @Suppress("LongParameterList")
        internal fun dispatchLog(
            level: Level,
            error: Throwable?,
            message: String?,
            tag: String?,
            options: LogOptions,
            vararg args: Any?
        ) {
            trees.forEach {
                it.prepareLog(
                    level = level,
                    error = error,
                    message = message,
                    args = args,
                    tag = tag,
                    options = options
                )
            }
        }
    }
}
