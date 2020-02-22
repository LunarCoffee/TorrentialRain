package dev.lunarcoffee.torrentialrain.torrent

import dev.lunarcoffee.torrentialrain.metainfo.TorrentMetaInfo
import dev.lunarcoffee.torrentialrain.torrent.tracker.TrackerRequester
import java.io.File

class DownloadManager(private val metaInfo: TorrentMetaInfo, private val outputDir: File) {
    private val tracker = TrackerRequester(metaInfo)

    suspend fun start() {

    }

    suspend fun stop() {

    }
}
