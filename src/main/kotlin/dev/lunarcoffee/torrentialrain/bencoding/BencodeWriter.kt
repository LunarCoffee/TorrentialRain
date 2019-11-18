package dev.lunarcoffee.torrentialrain.bencoding

class BTypeException : Exception()

fun Any.bencode(): String {
    return when (this) {
        is Int, Long -> "i${this}e"
        is String -> "$length:$this"
        is List<*> -> "l${this.joinToString("") { it?.bencode() ?: throw BTypeException() }}e"
        is Map<*, *> -> this
            .toList()
            .joinToString("", "d", "e") {
                val first = it.first?.bencode() ?: throw BTypeException()
                val second = it.second?.bencode() ?: throw BTypeException()
                "$first$second"
            }
        is BInt -> "i${value}e"
        is BString -> "$length:${string.toByteArray().joinToString("") { it.toChar().toString() }}"
        is BList -> "l${this.joinToString("") { it.bencode() }}e"
        is BDict -> this
            .toList()
            .joinToString("", "d", "e") { "${it.first.bencode()}${it.second.bencode()}" }
        else -> throw BTypeException()
    }
}
