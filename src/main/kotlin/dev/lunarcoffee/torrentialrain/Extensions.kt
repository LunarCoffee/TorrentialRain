package dev.lunarcoffee.torrentialrain

import kotlin.system.exitProcess

fun String.errorAndExit(): Nothing {
    println("(error) $this")
    exitProcess(1)
}
