package dev.lunarcoffee.torrentialrain.torrent.tracker

import dev.lunarcoffee.torrentialrain.metainfo.TorrentMetaInfo

class TrackerRequester(private val metaInfo: TorrentMetaInfo) {
    val infoHash = metaInfo
}
