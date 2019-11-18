package dev.lunarcoffee.torrentialrain.torrent.tracker

import dev.lunarcoffee.torrentialrain.metainfo.TorrentMetaInfo
import dev.lunarcoffee.torrentialrain.urlEncode

class TrackerRequester(private val metaInfo: TorrentMetaInfo) {
    private val infoHash = metaInfo.info.infoHash.urlEncode()

    private val announceUrl = metaInfo.announceUrl
    private val announceList = metaInfo.announceList

    private var uploaded = 0
    private var downloaded = 0
    private val left = metaInfo.info.files.map { it.length }.sum()
}
