package dev.lunarcoffee.torrentialrain

import java.math.BigInteger
import java.net.URLEncoder
import java.security.MessageDigest
import kotlin.system.exitProcess

fun String.errorAndExit(): Nothing {
    println("(error) $this")
    exitProcess(1)
}

fun String.urlEncode(): String {
    return URLEncoder.encode(
        this.chunked(2).joinToString("") { it.toInt(16).toChar().toString() },
        Charsets.UTF_8
    )
}

fun Iterable<Byte>.toActualString() = joinToString("") { it.toChar().toString() }
fun ByteArray.toActualString() = joinToString("") { it.toChar().toString() }

fun ByteArray.getSha1Checksum(): String {
    val digest = MessageDigest.getInstance("SHA-1").apply { update(this@getSha1Checksum) }
    return BigInteger(1, digest.digest()).toString(16)
}
