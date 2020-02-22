package dev.lunarcoffee.torrentialrain

import dev.lunarcoffee.torrentialrain.bencode.BencodeReader
import dev.lunarcoffee.torrentialrain.metainfo.MetaInfoReader
import dev.lunarcoffee.torrentialrain.metainfo.TorrentInfoHash
import dev.lunarcoffee.torrentialrain.torrent.DownloadManager
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(args: Array<String>) {
    val inputFilePath = args.getOrElse(0) { "Missing required path argument.".errorAndExit() }
    val outputDirPath = args.getOrElse(1) { "Missing required output directory.".errorAndExit() }

    val torrentFile = File(inputFilePath)
    if (!torrentFile.isFile || !torrentFile.exists())
        "The input file is invalid!".errorAndExit()

    val outputDir = File(outputDirPath)
    if (!outputDir.isDirectory || !outputDir.exists())
        "The output directory is invalid!".errorAndExit()

    val torrentBytes = torrentFile.readBytes()
    val metaInfo = MetaInfoReader(BencodeReader(torrentBytes), TorrentInfoHash(torrentBytes)).read()
    val downloadManager = DownloadManager(metaInfo, outputDir)

    runBlocking { downloadManager.start() }
}
