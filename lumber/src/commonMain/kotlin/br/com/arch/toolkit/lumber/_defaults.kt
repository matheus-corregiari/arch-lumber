@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "ktlint:standard:filename")

package br.com.arch.toolkit.lumber

/**
 * The default maximum length for a single log message line.
 * If a message exceeds this, it is split into multiple parts.
 */
internal expect val MAX_LOG_LENGTH: Int

/**
 * The default maximum length for a log tag.
 * If a tag exceeds this, it is truncated.
 */
internal expect val MAX_TAG_LENGTH: Int

/**
 * Provides a default tag for logging when no explicit tag is provided.
 *
 * Typically, this uses reflection or stack trace analysis to determine
 * the calling class name.
 *
 * @return The default tag string, or `null` if it cannot be determined.
 */
internal expect fun defaultTag(): String?
