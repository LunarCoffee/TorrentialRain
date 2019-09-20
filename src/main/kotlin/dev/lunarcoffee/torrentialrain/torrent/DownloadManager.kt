package dev.lunarcoffee.torrentialrain.torrent

import dev.lunarcoffee.torrentialrain.metainfo.TorrentMetaInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DownloadManager(
    private val metaInfo: TorrentMetaInfo
) : CoroutineScope by CoroutineScope(Dispatchers.IO) {


}
