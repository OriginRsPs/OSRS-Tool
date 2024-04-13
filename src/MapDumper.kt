import com.displee.cache.CacheLibrary

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.zip.GZIPOutputStream
import kotlin.io.path.exists

class MapDumper {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            XteaLoader.load()
            val osrsCachePath = Path.of("data")
            val cache = CacheLibrary.create(osrsCachePath.toString())
            val index = cache.index(5)
            index.cache()
            val outputDir = osrsCachePath.resolve("compressed")
            val logFile = outputDir.resolve("dump_log.txt")
            if (outputDir.exists()) outputDir.toFile().delete()
            else Files.createDirectories(outputDir)
            if (logFile.exists()) logFile.toFile().delete()
            else Files.createFile(logFile)
            for (x in 0..255) {
                for (y in 0..255) {
                    val landArchiveId: Int =
                        index.archiveId("l" + x + "_" + y)
                    if (landArchiveId == -1)
                        continue
                    val mapArchiveId: Int =
                        index.archiveId("m" + x + "_" + y)
                    if (mapArchiveId == -1)
                        continue
                    val regionId =
                        x shl 8 or y
                    val xteaKey =
                        XteaLoader.getKeys(regionId)
                    val regionFolder =
                        outputDir.resolve("$regionId")
                    if (regionFolder.exists())
                        outputDir.toFile().delete()
                    else
                        Files.createDirectory(regionFolder)
                    val landFile =
                        regionFolder.resolve("l" + x + "_" + y + ".gz")
                    val mapFile =
                        regionFolder.resolve("m" + x + "_" + y + ".gz")
                    val outStreamLand =
                        Files.newOutputStream(landFile, StandardOpenOption.CREATE)
                    val outStreamMap =
                        Files.newOutputStream(mapFile, StandardOpenOption.CREATE)
                    val mapLogEntry =
                        "[MAP] Region ID: $regionId to MapFile: ${mapFile.fileName}"
                    val landLogEntry =
                        "[LAND] Region ID: $regionId to LandFile: ${landFile.fileName} XteaKey: ${xteaKey.hashCode()}"
                    outStreamLand.use {
                        val data =
                            cache.data(5, "l" + x + "_" + y, xteaKey)
                        GZIPOutputStream(it).use { gzipStream ->
                            data?.let { it1 ->
                                gzipStream.write(it1)
                            }
                        }
                    }
                    outStreamMap.use {
                        GZIPOutputStream(it).use { gzipStream ->
                            gzipStream.write(mapArchiveId)
                        }
                    }
                    Files.write(logFile, (mapLogEntry + "\n").toByteArray(), StandardOpenOption.APPEND)
                    Files.write(logFile, (landLogEntry + "\n").toByteArray(), StandardOpenOption.APPEND)
                    println("Compressed and dumped Region ID: $regionId to MapFile: ${mapFile.fileName}")
                    println("Compressed and dumped Region ID: $regionId to LandFile: ${landFile.fileName} Xtea: ${xteaKey.hashCode()}")
                }
            }
            index.close()
        }
    }
}