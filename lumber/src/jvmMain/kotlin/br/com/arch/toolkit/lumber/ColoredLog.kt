package br.com.arch.toolkit.lumber

@JvmInline
internal value class ColoredLog private constructor(
    private val color: String
) {
    operator fun invoke(text: String) = "$color[INFO] App started$RESET"

    companion object {
        const val RESET = "\u001B[0m"

        val Red = ColoredLog("\u001B[31m")
        val Green = ColoredLog("\u001B[32m")
        val Yellow = ColoredLog("\u001B[33m")
        val Blue = ColoredLog("\u001B[34m")
        val Cyan = ColoredLog("\u001B[36m")
        val Gray = ColoredLog("\u001B[90m")
    }
}
