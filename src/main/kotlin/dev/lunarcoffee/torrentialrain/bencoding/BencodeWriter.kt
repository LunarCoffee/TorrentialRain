package dev.lunarcoffee.torrentialrain.bencoding

class BTypeException : Exception()

fun Any.bencode(): String {
    return when (this) {
        is Int, Long -> "i${this}e"
        is String -> "$length:$this"
        is List<*> -> "l${this.joinToString("") { it?.bencode() ?: throw BTypeException() }}e"
        is Map<*, *> -> {
            val bencode = this.toList().joinToString("") {
                val first = it.first?.bencode() ?: throw BTypeException()
                val second = it.second?.bencode() ?: throw BTypeException()
                "$first$second"
            }
            "d${bencode}e"
        }
        else -> throw BTypeException()
    }
}
