package dev.lunarcoffee.torrentialrain.metainfo

import dev.lunarcoffee.torrentialrain.getSha1Checksum
import dev.lunarcoffee.torrentialrain.intoString

class TorrentInfoHash(private val text: ByteArray) {
    fun getInfoHash(): String {
        val start = text.intoString().windowed(7).indexOf("4:infod") + 6
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

        return text.slice(start..pos).toByteArray().getSha1Checksum()
    }
}
