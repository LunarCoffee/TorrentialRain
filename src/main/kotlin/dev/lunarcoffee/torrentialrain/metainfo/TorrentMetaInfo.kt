package dev.lunarcoffee.torrentialrain.metainfo

class TorrentMetaInfo(
    val info: TorrentTransferInfo,
    val announceUrl: String,
    val announceList: List<List<String>>,
    val creationTime: Long,
    val comment: String,
    val createdBy: String,
    val encoding: String?
)
