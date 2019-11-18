package dev.lunarcoffee.torrentialrain

import dev.lunarcoffee.torrentialrain.bencoding.BencodeReader
import dev.lunarcoffee.torrentialrain.metainfo.MetaInfoReader
import dev.lunarcoffee.torrentialrain.metainfo.TorrentInfoHash
import dev.lunarcoffee.torrentialrain.torrent.DownloadManager
import dev.lunarcoffee.torrentialrain.torrent.tracker.TrackerRequester
import java.io.File

private fun main() {
//    try {
//        File(path)
//    } catch (e: IOException) {
//        "File at path '$path' does not exist!".errorAndExit()
//    }

    val torrent = "src/main/resources/example.torrent"
    val bytes = File(torrent).readBytes()
    val metaInfo = MetaInfoReader(BencodeReader(bytes), TorrentInfoHash(bytes)).read()

    val downloadManager = DownloadManager(TrackerRequester(metaInfo))
}
