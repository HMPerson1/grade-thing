import java.io.File

import com.github.tototoshi.csv.CSVReader

import scala.collection.JavaConverters._
import scala.collection.SortedSet

object Data {

  val popDataFile = new File("popdata.csv")
  val locDataFile = new File("locdata.csv")

  val citiesByLat: SortedSet[City] = loadData()

  def lookupCity(lat: Double): City = {
    ???
  }

  private case class PopEntry(geoid: Int, name: String, pop: Int)
  private case class LocEntry(geoid: Int, lat: Double, lon: Double)
  def loadData(): SortedSet[City] = {
    val popDataRaw = CSVReader.open(popDataFile).all
    val locDataRaw = CSVReader.open(locDataFile).all

    val popData = popDataRaw map { l => PopEntry(l(0).toInt, l(1), l(2).toInt) }
    val locData = locDataRaw map { l => LocEntry(l(0).toInt, l(1).toDouble, l(2).toDouble) }

    val locMap = (locData map { e => (e.geoid, e) }).toMap

    val data = popData map { e =>
      val loc = locMap(e.geoid)
      City(e.name, e.pop, loc.lat, loc.lon)
    }

    SortedSet(data: _*)(Ordering.by(_.lat))
  }

  case class City(name: String, pop: Int, lat: Double, lon: Double)
}
