package dev.lunarcoffee.torrentialrain.bencode

import dev.lunarcoffee.torrentialrain.intoString

class BencodeReader(private val text: ByteArray) {
    private var pos = 0

    @Suppress("UNCHECKED_CAST")
    fun <T> read(): T? {
        val char = text[pos].toChar()
        if (char.isDigit())
            return string() as T

        return when (char) {
            'i' -> int() as T
            'l' -> list() as T
            'd' -> dict() as T
            'e' -> null
            else -> throw IllegalArgumentException()
        }
    }

    private fun string(): String {
        val length = consumeWhile("[^:]".toRegex()).toIntOrNull() ?: throw IllegalArgumentException()
        consumeOne(":".toRegex())

        val string = text.drop(pos).take(length)
        pos += length

        return string.intoString()
    }

    private fun int(): Long {
        consumeOne("i".toRegex())
        val value = consumeWhile("[^e]".toRegex()).toLongOrNull() ?: throw IllegalArgumentException()

        consumeOne("e".toRegex())
        return value
    }

    private fun list(): List<Any> {
        consumeOne("l".toRegex())
        val list = mutableListOf<Any>()

        while (true)
            list.add(read() ?: break)

        consumeOne("e".toRegex())
        return list
    }

    private fun dict(): Map<String, Any> {
        consumeOne("d".toRegex())
        val dict = mutableMapOf<String, Any>()

        while (text[pos].toChar() != 'e') {
            val key = string()
            dict[key] = read() ?: throw IllegalArgumentException()
        }

        consumeOne("e".toRegex())
        return dict
    }

    private fun consumeOne(regex: Regex): String {
        val res = text[pos++].toChar().toString()
        if (res matches regex)
            return res
        throw IllegalArgumentException()
    }

    private fun consumeWhile(regex: Regex): String {
        val string = text.drop(pos).takeWhile { it.toChar().toString() matches regex }
        pos += string.size
        return string.intoString()
    }
}
