package dev.lunarcoffee.torrentialrain.metainfo

import java.math.BigInteger
import java.security.MessageDigest

class TorrentInfoHash(private val text: ByteArray) {
    fun getInfoHash(): String {
        val start = text.toList().windowed(4).indexOf(listOf(0x69.toByte(), 0x6E, 0x66, 0x6F)) + 4
        var pos = start
        var nesting = 0

        while (true) {
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
            if (nesting == 0)
                break
            pos++
        }

        val end = pos
        val infoRaw = text.slice(start..end).joinToString("") { it.toChar().toString() }

        println(getPercentEncodedSha1(infoRaw))
        return getPercentEncodedSha1(infoRaw)
    }

    private fun getPercentEncodedSha1(string: String): String {
        val digest = MessageDigest.getInstance("SHA-1").apply { update(string.toByteArray(Charsets.US_ASCII)) }
        return BigInteger(1, digest.digest()).toString(16)
    }
}
