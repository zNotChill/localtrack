package me.znotchill.localtrack.tosu

import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.MainRenderScope
import com.varabyte.kotter.runtime.render.RenderScope

private fun MainRenderScope.colored(r: Int, g: Int, b: Int, s: String) = rgb(r, g, b) { text(s) }

fun MainRenderScope.tosu() = colored(87, 139, 198, "tosu")
fun MainRenderScope.port() = colored(87, 139, 198, "24050")
fun MainRenderScope.y() = colored(109, 198, 87, "Y")
fun MainRenderScope.n() = colored(198, 100, 87, "N")
fun MainRenderScope.localtrack(label: String = "localtrack") = colored(194, 87, 198, label)
fun MainRenderScope.localtrack(block: RenderScope.() -> Unit) = rgb(194, 87, 198) { block() }

fun MainRenderScope.options() { text(" ("); y(); text("/"); n(); text(")") }

fun MainRenderScope.important() {
    textLine()
    yellow(isBright = true) { p { textLine("  ⚠ IMPORTANT! ⚠") } }
}

fun MainRenderScope.cfgValue(value: String) =
    yellow(isBright = true) { italic { text("config: $value") } }

fun parseOption(input: String) =
    input.isBlank() || input.lowercase().let { it.startsWith("y") || it.startsWith("t") }