import org.jsoup.Jsoup
import scala.io.Source

@main def main(args: String*): Unit = 
  val lang = if (args.length != 2) then "en" else args(0).toLowerCase()

  
trait FileReader:
  def readInput(fileName: String): Iterator[String] = 
    for 
      code <- Source.fromFile(fileName).getLines()
    yield
      code

object IOSingleton extends FileReader