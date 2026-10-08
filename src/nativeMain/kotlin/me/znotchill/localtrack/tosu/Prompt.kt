package me.znotchill.localtrack.tosu

import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.MainRenderScope
import com.varabyte.kotter.runtime.Session

class Prompt(
    val completions: List<String> = emptyList(),
    val configKey: String? = null,
    val validate: (String) -> String? = { null },
    val body: MainRenderScope.() -> Unit,
)

fun Session.ask(prompt: Prompt): String {
    var error by liveVarOf<String?>(null)
    var attempts by liveVarOf(0)
    var answer = ""

    section {
        error?.let { msg ->
            red(isBright = true) {
                bold { text("✗ $msg") }
                if (attempts > 1) text(" (attempt $attempts)")
            }
            textLine(); textLine()
        }

        prompt.body(this)

        textLine()
        text("> ")
        if (prompt.completions.isEmpty()) input()
        else input(Completions(*prompt.completions.toTypedArray()))

        prompt.configKey?.let { textLine(); cfgValue(it) }
    }.runUntilInputEntered {
        onInputEntered {
            val msg = prompt.validate(input)
            if (msg == null) {
                answer = input
            } else {
                error = msg
                attempts++
                rejectInput()
            }
        }
    }
    return answer
}

fun Session.askYesNo(configKey: String, body: MainRenderScope.() -> Unit): Boolean =
    parseOption(ask(Prompt(listOf("yes", "no"), configKey, body = body)))