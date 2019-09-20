package dev.lunarcoffee.torrentialrain.metainfo

class TorrentTransferInfo(
    val pieceSize: Long,
    val pieces: String,
    val private: Boolean,
    val mode: TorrentFileMode,
    val name: String,
    val files: List<TorrentFileInfo>,
    val infoHash: String
)
