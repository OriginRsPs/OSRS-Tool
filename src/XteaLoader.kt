import FileUtil.getBase
import com.google.gson.Gson
import java.io.File

data class Xtea(
    val mapsquare : Int,
    val key : IntArray
)

object XteaLoader {

    var xteas : MutableMap<Int, Xtea> = emptyMap<Int, Xtea>().toMutableMap()
    val xteasList: MutableMap<Int, IntArray> = HashMap<Int, IntArray>().toMutableMap()

    fun load() {
        val file = File(getBase(), "keys.json")
        val data = Gson().fromJson(file.readText(), Array<Xtea>::class.java)
        data.forEach {
            xteas[it.mapsquare] = it
            xteasList[it.mapsquare] = it.key
        }
        println("Keys Loaded: ${xteasList.size}")
    }

    fun getKeys(region: Int): IntArray? {
        if (xteasList.containsKey(region)) {
            return xteasList[region]!!
        }
        return null
    }

}