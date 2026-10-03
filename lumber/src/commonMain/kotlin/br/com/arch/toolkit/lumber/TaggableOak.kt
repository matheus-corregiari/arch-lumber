package br.com.arch.toolkit.lumber

import br.com.arch.toolkit.lumber.Lumber.Level
import br.com.arch.toolkit.lumber.Lumber.LogOptions
import br.com.arch.toolkit.lumber.Lumber.Oak

/** A logging destination with an immutable tag and the one-shot options inherited from [Oak]. */
open class TaggableOak internal constructor(
    protected override val tag: String,
    destination: Oak,
    initialOptions: LogOptions
) : Oak() {
    internal override val logDestination: Oak = destination.logDestination

    /** Creates a tagged facade for a destination without setting any one-shot options. */
    constructor(tag: String, destination: Oak) : this(tag.trim(), destination, LogOptions())

    init {
        initializeOptions(initialOptions)
    }

    override fun isLoggable(tag: String?, level: Level): Boolean =
        logDestination.isLoggable(tag ?: this.tag, level)

    override fun logWithOptions(
        level: Level,
        error: Throwable?,
        message: String?,
        options: LogOptions,
        vararg args: Any?
    ) = logDestination.logWithOptions(level, error, message, options.copy(tag = tag), *args)

    override fun log(level: Level, tag: String?, message: String, error: Throwable?) =
        logDestination.writeLog(level, tag, message, error)
}
