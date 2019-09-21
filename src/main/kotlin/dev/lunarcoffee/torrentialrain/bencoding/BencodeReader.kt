package dev.lunarcoffee.torrentialrain.bencoding

import dev.lunarcoffee.torrentialrain.errorAndExit

class BencodeReader(private val text: ByteArray) {
    private var pos = 0

    fun parse(): BType? {
        val char = text[pos].toChar()
        if (char.isDigit())
            return string()

        return when (char) {
            'i' -> int()
            'l' -> list()
            'd' -> dict()
            'e' -> null
            else -> "Unexpected bencoded value '$char'!".errorAndExit()
        }
    }

    private fun string(): BString {
        val length = consumeWhile("[^:]".toRegex()).toIntOrNull()
            ?: "Integer invalid or greater than the capacity of an int!".errorAndExit()
        consumeOne(":".toRegex())

        val string = text.drop(pos).take(length)
        pos += length

        if (string.size < length)
            "Expected a string of length $length, got string of length ${string.size}"
                .errorAndExit()

        return BString(string.joinToString("") { it.toChar().toString() })
    }

    private fun int(): BInt {
        consumeOne("i".toRegex())
        val value = consumeWhile("[^e]".toRegex()).toLongOrNull()
            ?: "Integer invalid or greater than the capacity of a long!".errorAndExit()

        consumeOne("e".toRegex())
        return BInt(value)
    }

    private fun list(): BList {
        consumeOne("l".toRegex())
        val list = mutableListOf<BType>()

        while (true)
            list += parse() ?: break

        consumeOne("e".toRegex())
        return BList(list)
    }

    private fun dict(): BDict {
        consumeOne("d".toRegex())
        val dict = mutableMapOf<BString, BType>()

        while (text[pos].toChar() != 'e') {
            val key = string()
            dict[key] = parse() ?: "Expected a value!".errorAndExit()
        }

        consumeOne("e".toRegex())
        return BDict(dict)
    }

    private fun consumeOne(regex: Regex): String {
        val res = text[pos++].toChar().toString()
        if (res matches regex)
            return res
        "Expected a character matching '$regex', found '$res'!".errorAndExit()
    }

    private fun consumeWhile(regex: Regex): String {
        val string = text.drop(pos).takeWhile { it.toChar().toString() matches regex }
        pos += string.size
        return string.joinToString("") { it.toChar().toString() }
    }
}
