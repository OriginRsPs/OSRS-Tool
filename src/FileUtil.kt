import java.io.File
import java.nio.file.Path

object FileUtil {
    fun getBase(): File {
        val path = Path.of("data", "keys")
        val file = path.toFile();
        if (!file.exists()) file.mkdirs()
        return file
    }
}