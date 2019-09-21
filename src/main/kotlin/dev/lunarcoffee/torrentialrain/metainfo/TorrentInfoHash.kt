package dev.lunarcoffee.torrentialrain.metainfo

import dev.lunarcoffee.torrentialrain.toActualString
import java.math.BigInteger
import java.security.MessageDigest

class TorrentInfoHash(private val text: ByteArray) {
    fun getInfoHash(): String {
        val start = text.toActualString().windowed(7).indexOf("4:infod") + 6
        var pos = start
        var nesting = 0

        while (true) {
            // Jump past elements of the info dictionary.
            when (text[pos].toChar()) {
                'd' -> nesting++
                'i' -> pos += text.drop(pos + 1).takeWhile { it.toChar() != 'e' }.size + 1
                'l' -> nesting++
                in '0'..'9' -> {
                    val jumpLength = text
                        .drop(pos)
                        .takeWhile { it.toChar() != ':' }
                        .joinToString("") { it.toChar().toString() }
                    val jumpDigitLength = jumpLength.length
                    pos += jumpLength.toInt() + jumpDigitLength
                }
                'e' -> nesting--
            }
            // If the info dictionary has been exited, we are done.
            if (nesting == 0)
                break
            pos++
        }

        return getPercentEncodedSha1(text.slice(start..pos).toByteArray())
    }

    private fun getPercentEncodedSha1(value: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-1").apply { update(value) }
        return BigInteger(1, digest.digest()).toString(16)
    }
}
