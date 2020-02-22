package dev.lunarcoffee.torrentialrain.metainfo

import dev.lunarcoffee.torrentialrain.bencode.BencodeReader
import dev.lunarcoffee.torrentialrain.errorAndExit

@Suppress("UNCHECKED_CAST", "MapGetWithNotNullAssertionOperator")
class MetaInfoReader(private val bencodeReader: BencodeReader, private val infoHash: TorrentInfoHash) {
    fun read() = runCatching { readMetaInfo() }.getOrNull() ?: "Invalid torrent metainfo file!".errorAndExit()

    private fun readMetaInfo(): TorrentMetaInfo? {
        val tree = bencodeReader.read<Map<String, Any>>() ?: return null

        val transferInfo = readTransferInfo(tree)
        val announceUrl = tree["announce"]!! as String
        val announceList = tree["announce-list"] as List<List<String>>?
        val creationTime = tree["creation date"] as Long?
        val comment = tree["comment"] as String?
        val createdBy = tree["createdBy"] as String?
        val encoding = tree["encoding"] as String?

        return TorrentMetaInfo(
            transferInfo,
            announceUrl,
            announceList ?: emptyList(),
            creationTime ?: -1L,
            comment ?: "",
            createdBy ?: "",
            encoding
        )
    }

    private fun readTransferInfo(tree: Map<String, Any>): TorrentTransferInfo {
        val infoRaw = tree["info"] as Map<String, Any>
        val infoHash = infoHash.getInfoHash()

        val pieceSize = infoRaw["piece length"]!! as Long
        val pieces = infoRaw["pieces"]!! as String
        val private = infoRaw["private"] as Long? ?: 0

        val name = infoRaw["name"] as String? ?: "file"
        val isSingleFile = infoRaw["length"] != null

        return if (isSingleFile) {
            val length = infoRaw["length"]!! as Long
            val checksum = infoRaw["md5sum"] as String?

            TorrentTransferInfo(
                pieceSize,
                pieces.chunked(20),
                private == 1L,
                TorrentFileMode.SINGLE_FILE,
                name,
                listOf(TorrentFileInfo(length, checksum, name)),
                infoHash
            )
        } else {
            val filesRaw = infoRaw["files"]!! as List<Map<String, Any>>
            val files = filesRaw.map { file ->
                val length = file["length"]!! as Long
                val checksum = file["md5sum"] as String?
                val path = file["path"]!! as List<String>

                TorrentFileInfo(length, checksum, path.joinToString("") { "$it/" })
            }

            TorrentTransferInfo(
                pieceSize,
                pieces.chunked(20),
                private == 1L,
                TorrentFileMode.MULTIPLE_FILE,
                name,
                files,
                infoHash
            )
        }
    }
}
