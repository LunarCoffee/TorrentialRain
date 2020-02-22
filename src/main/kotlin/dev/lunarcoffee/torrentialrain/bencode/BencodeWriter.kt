package dev.lunarcoffee.torrentialrain.bencode

import java.lang.IllegalArgumentException

class BencodeWriter(private val obj: Any) {
    fun write(): String = when (obj) {
        is Int, Long -> "i${obj}e"
        is String -> "${obj.length}:$obj"
        is List<*> -> "l${obj.joinToString("") { BencodeWriter(it ?: throw IllegalArgumentException()).write() }}e"
        is Map<*, *> -> obj
            .toList()
            .joinToString("") {
                val first = BencodeWriter(it.first ?: throw IllegalArgumentException())
                val second = BencodeWriter(it.second ?: throw IllegalArgumentException())
                "d$first${second}e"
            }
        else -> throw IllegalArgumentException()
    }
}
