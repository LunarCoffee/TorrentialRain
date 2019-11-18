package dev.lunarcoffee.torrentialrain.bencoding

sealed class BType

data class BString(val string: String) : BType(), CharSequence by string
class BInt(val value: Long) : BType()
class BList(val contents: List<BType>) : BType(), List<BType> by contents

class BDict(private val contents: Map<BString, BType>) : BType(), Map<BString, BType> by contents {
    operator fun get(key: String) = get(BString(key))
}
