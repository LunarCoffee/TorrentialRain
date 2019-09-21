package dev.lunarcoffee.torrentialrain.metainfo

import dev.lunarcoffee.torrentialrain.bencoding.*
import dev.lunarcoffee.torrentialrain.errorAndExit

class MetaInfoReader(
    private val bencodeReader: BencodeReader,
    private val infoHash: TorrentInfoHash
) {
    fun read(): TorrentMetaInfo {
        return try {
            readMetaInfo()
        } catch (t: Throwable) {
            t.printStackTrace()
            "Invalid torrent metainfo file!".errorAndExit()
        }
    }

    private fun readMetaInfo(): TorrentMetaInfo {
        val tree = bencodeReader.parse() as BDict

        val transferInfo = readTransferInfo(tree)
        val announceUrl = tree["announce"]!! as BString
        val announceList = tree["announce-list"] as BList?
        val creationTime = tree["creation date"] as BInt?
        val comment = tree["comment"] as BString?
        val createdBy = tree["createdBy"] as BString?
        val encoding = tree["encoding"] as BString?

        return TorrentMetaInfo(
            transferInfo,
            announceUrl.string,
            announceList
                ?.contents
                ?.map { list -> (list as BList).contents.map { (it as BString).string } }
                ?: emptyList(),
            creationTime?.value ?: -1,
            comment?.string ?: "",
            createdBy?.string ?: "",
            encoding?.string
        )
    }

    private fun readTransferInfo(tree: BDict): TorrentTransferInfo {
        val infoRaw = tree["info"]!! as BDict
        val infoHash = infoHash.getInfoHash()

        val pieceSize = infoRaw["piece length"]!! as BInt
        val pieces = infoRaw["pieces"]!! as BString
        val private = infoRaw["private"] as BInt? ?: BInt(0)

        val name = infoRaw["name"] as BString? ?: BString("file")
        val isSingleFile = infoRaw["length"] != null

        return if (isSingleFile) {
            val length = infoRaw["length"]!! as BInt
            val checksum = infoRaw["md5sum"] as BString?

            TorrentTransferInfo(
                pieceSize.value,
                pieces.string.chunked(20),
                private.value == 1L,
                TorrentFileMode.SINGLE_FILE,
                name.string,
                listOf(TorrentFileInfo(length.value, checksum?.string, name.string)),
                infoHash
            )
        } else {
            val filesRaw = infoRaw["files"]!! as BList
            val files = filesRaw.contents.map { it as BDict }.map { file ->
                val length = file["length"]!! as BInt
                val checksum = file["md5sum"] as BString?
                val path = file["path"]!! as BList

                TorrentFileInfo(
                    length.value,
                    checksum?.string,
                    path.contents.joinToString("") { "${(it as BString).string}/" }
                )
            }

            TorrentTransferInfo(
                pieceSize.value,
                pieces.string.chunked(20),
                private.value == 1L,
                TorrentFileMode.MULTIPLE_FILE,
                name.string,
                files,
                infoHash
            )
        }
    }
}
