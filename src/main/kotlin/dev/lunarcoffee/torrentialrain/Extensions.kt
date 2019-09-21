package dev.lunarcoffee.torrentialrain

import kotlin.system.exitProcess

fun String.errorAndExit(): Nothing {
    println("(error) $this")
    exitProcess(1)
}

fun Iterable<Byte>.toActualString() = joinToString("") { it.toChar().toString() }
fun ByteArray.toActualString() = joinToString("") { it.toChar().toString() }
