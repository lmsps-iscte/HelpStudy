package classes

import java.io.{FileNotFoundException, PrintWriter}
import java.nio.file.Paths
import scala.io.Source

object Util {

  def saveToFile(str: String, filename: String): Unit = {
    val file = Paths.get(System.getProperty("user.dir"), filename).toFile
    file.getParentFile.mkdirs()
    val pw = new PrintWriter(file)
    pw.write(str)
    pw.close()
  }

  @throws(classOf[FileNotFoundException])
  def readFromFile(filename: String): String = {
    val file = Paths.get(System.getProperty("user.dir"), filename).toFile
    val source = Source.fromFile(file)
    source.getLines.mkString
  }

}
