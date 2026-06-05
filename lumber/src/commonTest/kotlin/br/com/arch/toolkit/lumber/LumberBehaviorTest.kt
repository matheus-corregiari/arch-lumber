package br.com.arch.toolkit.lumber

import br.com.arch.toolkit.lumber.Lumber.Level
import br.com.arch.toolkit.lumber.oak.RecordingOak
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LumberBehaviorTest {
    @BeforeTest
    @AfterTest
    fun resetForest() {
        Lumber.uprootAll()
    }

    @Test
    fun `dispatches the level-specific helpers`() {
        Level.entries.forEach { level ->
            val tree = newTree()
            tree.dispatch(level)
            tree.assertAll(
                RecordingOak.Entry(level, defaultTag(), expectedMessage(level), null)
            )
            Lumber.uprootAll()
        }
    }

    @Test
    fun `dispatches raw log calls`() {
        val tree = newTree()

        Lumber.log(Level.Info, "custom")

        tree.assertAll(
            RecordingOak.Entry(Level.Info, defaultTag(), "custom", null)
        )
    }

    @Test
    fun `formats string placeholders`() {
        val tree = newTree()

        Lumber.debug("value=%s", 42)

        tree.assertAll(
            RecordingOak.Entry(Level.Debug, defaultTag(), "value=42", null)
        )
    }

    @Test
    fun `ignores blank message without throwable`() {
        val tree = newTree()

        Lumber.info("")

        tree.assertAll()
    }

    @Test
    fun `logs throwable stacktrace when message is blank`() {
        val tree = newTree()
        val error = Throwable("boom")

        Lumber.maxLogLength(error.stackTraceToString().length).error(error)

        tree.assertAll(
            RecordingOak.Entry(Level.Error, defaultTag(), error.stackTraceToString(), error)
        )
    }

    @Test
    fun `appends throwable stacktrace to formatted message`() {
        val tree = newTree()
        val error = Throwable("boom")
        val message = "status=500\n\n${error.stackTraceToString()}"

        Lumber.maxLogLength(message.length).error(error, "status=%d", 500)

        tree.assertAll(
            RecordingOak.Entry(Level.Error, defaultTag(), message, error)
        )
    }

    @Test
    fun `splits long messages into numbered chunks`() {
        val tree = newTree()

        Lumber.maxLogLength(4).tag("Tag").warn("Hello")

        tree.assertAll(
            RecordingOak.Entry(Level.Warn, "Tag #0", "Hell", null),
            RecordingOak.Entry(Level.Warn, "Tag #1", "o", null)
        )
    }

    @Test
    fun `consumes pending length override when creating tagged facade`() {
        val tree = newTree()

        Lumber.maxTagLength(3).tag("CustomTag").info("first")
        Lumber.info("second")

        tree.assertAll(
            RecordingOak.Entry(Level.Info, "Cus", "first", null),
            RecordingOak.Entry(Level.Info, defaultTag(), "second", null)
        )
    }

    @Test
    fun `tagged facade keeps tag across calls`() {
        val tree = newTree()
        val tagged = Lumber.tag("Custom")

        tagged.info("first")
        tagged.info("second")

        tree.assertAll(
            RecordingOak.Entry(Level.Info, "Custom", "first", null),
            RecordingOak.Entry(Level.Info, "Custom", "second", null)
        )
    }

    @Test
    fun `tagged facade consumes length overrides once`() {
        val tree = newTree()
        val tagged = Lumber.tag("Tag")

        tagged.maxLogLength(4).warn("Hello")
        tagged.warn("World")

        tree.assertAll(
            RecordingOak.Entry(Level.Warn, "Tag #0", "Hell", null),
            RecordingOak.Entry(Level.Warn, "Tag #1", "o", null),
            RecordingOak.Entry(Level.Warn, "Tag", "World", null)
        )
    }

    @Test
    fun `tag transfers pending one-shot options from Lumber`() {
        val tree = newTree()

        Lumber.maxLogLength(4).tag("Tag").warn("Hello")
        Lumber.warn("World")

        tree.assertAll(
            RecordingOak.Entry(Level.Warn, "Tag #0", "Hell", null),
            RecordingOak.Entry(Level.Warn, "Tag #1", "o", null),
            RecordingOak.Entry(Level.Warn, defaultTag(), "World", null)
        )
    }

    @Test
    fun `quiet suppresses only the next message`() {
        val tree = newTree()

        Lumber.tag("Muted").quiet(true).debug("hidden")
        Lumber.debug("visible")

        tree.assertAll(
            RecordingOak.Entry(Level.Debug, defaultTag(), "visible", null)
        )
    }

    @Test
    fun `empty tags fall back to the default tag`() {
        val tree = newTree()

        Lumber.tag("").info("message")

        tree.assertAll(
            RecordingOak.Entry(Level.Info, defaultTag(), "message", null)
        )
    }

    @Test
    fun `max length must be positive`() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Lumber.maxLogLength(0)
            }

        assertEquals("length must be positive", error.message)
    }

    @Test
    fun `max tag length must be positive`() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Lumber.maxTagLength(0)
            }

        assertEquals("length must be positive", error.message)
    }

    @Test
    fun `tagged facade cannot be planted`() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                Lumber.plant(Lumber.tag("Tag"))
            }

        assertEquals("Cannot plant tagged Lumber.", error.message)
    }

    private fun newTree(blockedLevel: Level? = null): RecordingOak =
        RecordingOak(blockedLevel).also {
            Lumber.uproot(it)
            Lumber.plant(it)
        }

    private fun RecordingOak.dispatch(level: Level) {
        when (level) {
            Level.Verbose -> verbose("verbose")
            Level.Debug -> debug("debug")
            Level.Info -> info("info")
            Level.Warn -> warn("warn")
            Level.Error -> error("error")
            Level.Assert -> wtf("assert")
        }
    }

    private fun expectedMessage(level: Level): String =
        when (level) {
            Level.Verbose -> "verbose"
            Level.Debug -> "debug"
            Level.Info -> "info"
            Level.Warn -> "warn"
            Level.Error -> "error"
            Level.Assert -> "assert"
        }
}
