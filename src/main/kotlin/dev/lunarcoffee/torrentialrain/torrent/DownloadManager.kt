package dev.lunarcoffee.torrentialrain.torrent

import dev.lunarcoffee.torrentialrain.torrent.tracker.TrackerRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DownloadManager(
    private val trackerRequester: TrackerRequester
) : CoroutineScope by CoroutineScope(Dispatchers.IO) {

}
