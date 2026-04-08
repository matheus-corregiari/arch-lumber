package br.com.arch.toolkit.lumber

import br.com.arch.toolkit.lumber.Lumber.Level
import br.com.arch.toolkit.lumber.oak.RecordingOak
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LumberForestTest {
    @BeforeTest
    @AfterTest
    fun resetForest() {
        Lumber.uprootAll()
    }

    @Test
    fun `plant adds trees to the forest`() {
        val first = RecordingOak()
        val second = RecordingOak()

        Lumber.plant(first, second)

        assertEquals(2, Lumber.treeCount)
        assertEquals(listOf(first, second), Lumber.forest())
    }

    @Test
    fun `uproot removes a planted tree`() {
        val first = RecordingOak()
        val second = RecordingOak()
        Lumber.plant(first, second)

        Lumber.uproot(first)

        assertEquals(listOf(second), Lumber.forest())
    }

    @Test
    fun `uproot all clears the forest`() {
        Lumber.plant(RecordingOak(), RecordingOak())

        Lumber.uprootAll()

        assertEquals(emptyList(), Lumber.forest())
        assertEquals(0, Lumber.treeCount)
    }

    @Test
    fun `plant rejects Lumber itself`() {
        val error = assertFailsWith<IllegalArgumentException> {
            Lumber.plant(Lumber)
        }

        assertEquals("Cannot plant Lumber itself.", error.message)
    }

    @Test
    fun `dispatch sends a log to every planted tree`() {
        val first = RecordingOak()
        val second = RecordingOak()
        Lumber.plant(first, second)

        Lumber.warn("fanout")

        val expected = RecordingOak.Entry(Level.Warn, defaultTag(), "fanout", null)
        first.assertAll(expected)
        second.assertAll(expected)
    }
}
