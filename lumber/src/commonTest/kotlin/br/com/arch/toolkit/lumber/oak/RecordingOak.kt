package br.com.arch.toolkit.lumber.oak

import br.com.arch.toolkit.lumber.DebugOak
import br.com.arch.toolkit.lumber.Lumber.Level
import kotlin.test.assertEquals

class RecordingOak(
    private val blockLevel: Level? = null
) : DebugOak() {
    val history = mutableListOf<Entry>()

    override fun isLoggable(
        tag: String?,
        level: Level
    ): Boolean {
        super.isLoggable(tag, level)
        return (level == blockLevel).not()
    }

    override fun log(
        level: Level,
        tag: String?,
        message: String,
        error: Throwable?
    ) {
        super.log(level, tag, message, error)
        history.add(Entry(level, tag, message, error))
    }

    fun assertAll(vararg other: Entry) {
        assertEquals(other.size, history.size)
        other.forEachIndexed { index, data -> assertEquals(data, history[index]) }
    }

    data class Entry(
        private val level: Level,
        private val tag: String?,
        private val message: String,
        private val error: Throwable?
    )
}
