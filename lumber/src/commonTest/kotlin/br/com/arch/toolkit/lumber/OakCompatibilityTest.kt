package br.com.arch.toolkit.lumber

import kotlin.test.Test
import kotlin.test.assertEquals

class OakCompatibilityTest {
    private class Sink : Lumber.Oak() {
        val entries = mutableListOf<String>()

        override fun isLoggable(tag: String?, level: Lumber.Level) = true

        override fun log(level: Lumber.Level, tag: String?, message: String, error: Throwable?) {
            entries.add("$tag:$message")
        }
    }

    @Test
    fun directDestinationOptionsAndTagsDoNotDispatchToForest() {
        val sink = Sink()
        val forestSink = Sink()
        Lumber.uprootAll()
        try {
            Lumber.plant(forestSink)
            sink.quiet(true).debug("hidden")
            sink.tag("Direct").debug("visible")
            sink.tag("Original").quiet(true).debug("hidden")
            sink.tag("Original").maxLogLength(3).maxTagLength(2).debug("abcdef")
            sink.tag("Retagged").debug("visible")
            assertEquals(
                listOf("Direct:visible", "Or #0:abc", "Or #1:def", "Retagged:visible"),
                sink.entries
            )
            assertEquals(emptyList(), forestSink.entries)
        } finally {
            Lumber.uprootAll()
        }
    }

    @Test
    fun oakReferenceDispatchesThroughCurrentFacade() {
        val sink = Sink()
        Lumber.uprootAll()
        try {
            Lumber.plant(sink)
            val oak: Lumber.Oak = Lumber.OakWood
            oak.quiet(true).tag("Navigation").error("hidden")
            oak.tag("Navigation").error("visible")
            assertEquals(listOf("Navigation:visible"), sink.entries)
        } finally {
            Lumber.uprootAll()
        }
    }

    @Test
    fun taggedDestinationKeepsItsTagAndConsumesTransferredOptionsOnce() {
        val sink = Sink()
        val tagged = sink.quiet(true).maxLogLength(3).maxTagLength(2).tag("Direct")
        tagged.debug("filtered options")
        tagged.debug("abcdef")
        val retagged = tagged.maxLogLength(3).tag("Other")
        retagged.debug("abcdef")
        tagged.debug("unchanged")
        assertEquals(
            listOf("Direct:abcdef", "Other #0:abc", "Other #1:def", "Direct:unchanged"),
            sink.entries
        )
    }

    @Test
    fun taggableOakCanBeExtendedWithoutDuplicatingOptionHandling() {
        val sink = Sink()
        val tagged = object : TaggableOak("Fixed", sink) {
            override val tag = "Override"
        }
        tagged.maxTagLength(2).debug("first")
        tagged.debug("second")
        assertEquals(listOf("Ov:first", "Override:second"), sink.entries)
    }
}
