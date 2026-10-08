package me.znotchill.localtrack.tosu

import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.*
import me.znotchill.localtrack.storage.StorageManager
import me.znotchill.localtrack.storage.config.ConfigManager
import kotlin.experimental.ExperimentalNativeApi

object TosuSetup {
    @OptIn(ExperimentalNativeApi::class)
    fun expectedTosuBinary(): String? {
        if (!StorageManager.isOSSupported()) return null
        return when (Platform.osFamily) {
            OsFamily.WINDOWS -> "tosu.exe"
            OsFamily.LINUX -> "tosu"
            else -> null
        }
    }

    private fun binaryPath() = "tosu/${expectedTosuBinary()}"

    @OptIn(ExperimentalNativeApi::class)
    fun init(): Boolean {
        val cfg = ConfigManager.config
        if (!cfg.setup.firstTimeRunning) return true

        session {
            ask(Prompt(listOf("let's go"), "setup.firstTimeRunning") {
                p { textLine(); localtrack("  FIRST TIME SETUP") }
                text("this is your first time using "); localtrack(); textLine("!")
                text("welcome! ")
                red(isBright = true) { bold { text("PLEASE ") } }
                textLine("take the time to read each question!")
            })

            val spinUp = askYesNo("setup.spinUpProcess") {
                important()
                localtrack(); text(" relies on "); tosu(); textLine(" for its beatmap and player data,")
                text("and is pivotal to "); localtrack(); textLine(" running."); textLine()
                text("IF "); tosu(); text(" is not running, or is not on port "); port(); textLine(",")
                text("do you want "); localtrack(); text(" to automatically spin up a "); tosu()
                text(" process at launch if one cannot be found?"); options(); textLine()
                textLine()
                text("if you answer "); n(); text(", you will have to launch "); tosu()
                text(" EVERY time manually when using "); localtrack(); textLine()
            }

            if (spinUp) {
                StorageManager.createDirectory("tosu")

                if (!StorageManager.isOSSupported()) {
                    ask(Prompt(listOf("okay")) {
                        text("your operating system is ")
                        yellow(isBright = true) { text(Platform.osFamily.toString()) }
                        text(" and is "); red(isBright = true) { text("UNSUPPORTED") }
                        text(" by "); tosu(); text(" and subsequently "); localtrack(); textLine(".")
                    })
                } else if (!StorageManager.exists(binaryPath())) {
                    ask(Prompt(
                        completions = listOf("okay, done!"),
                        configKey = "setup.tosuBinaryPath",
                        validate = {
                            if (StorageManager.exists(binaryPath())) null
                            else "no tosu binary detected at ${StorageManager.tosuDir}/${expectedTosuBinary()}"
                        },
                    ) {
                        text("since you entered "); y(); textLine(" for the previous question,")
                        text("you must supply a "); tosu(); textLine(" binary."); textLine()
                        tosu(); text(" binaries are available at ")
                        link("https://github.com/tosuapp/tosu/releases"); textLine()
                        textLine("and all files from the zip should be placed in:")
                        localtrack { textLine(StorageManager.tosuDir) }
                        textLine()
                        text("your operating system is ")
                        yellow(isBright = true) { text(Platform.osFamily.toString()) }
                        text(" and is "); green(isBright = true) { text("SUPPORTED") }
                        text(" by "); localtrack(); textLine("!")
                    })
                }
            }

            val startup = askYesNo("process.startup") {
                text("do you wish to make "); localtrack(); text(" run at startup?"); options(); textLine()
                textLine()
                text("doing so will launch "); localtrack(); textLine(" silently in the background,")
                text("using little RAM and processing power, since "); localtrack(); textLine()
                textLine("will not run the local website or the osu! tracking")
                textLine("while the osu! process is not detected.")
                textLine()
                text("internally, "); localtrack(); textLine(" checks once per 10 seconds for")
                text("the osu! process through "); tosu(); textLine("'s /json/v2 endpoint.")
            }

            cfg.setup.spinUpProcess = spinUp
            cfg.setup.firstTimeRunning = false
            cfg.process.startup = startup
            ConfigManager.save()

            ask(Prompt(listOf("ok")) {
                localtrack(); textLine(" setup complete!")
                textLine("welcome! config has been saved to:")
                localtrack { textLine(StorageManager.appDataDir() + "/config.toml") }
            })
        }
        return true
    }

    fun alertPtraceCap() = session {
        ask(Prompt(
            completions = listOf("done"),
            validate = {
                if (TosuProcess.hasPtraceCap()) null
                else "capability not set yet, run the command above"
            },
        ) {
            important()
            text("since you're using Linux, "); tosu(); textLine(" needs elevated permission")
            textLine("to read memory from osu!"); textLine()
            text("it has been detected that "); tosu(); textLine(" doesn't have these permissions.")
            text("this is mandatory, since it avoids having to run "); tosu(); textLine(" as sudo.")
            textLine()
            textLine("run this in another terminal, then come back:")
            localtrack { textLine("  sudo setcap cap_sys_ptrace=eip ${StorageManager.tosuDir}/tosu") }
            text("source: "); link("https://github.com/tosuapp/tosu/blob/a4da331515091db0eeb4b7bf363fcbfbbdfe0ded/packages/tsprocess/lib/memory/memory_linux.cc#L163")
            textLine()
        })
    }
}