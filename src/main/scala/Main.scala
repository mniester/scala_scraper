//import org.jsoup.Jsoup
import scala.io.Source

@main 
def main(fileName: String): Unit = 
  for
    x <- IOSingleton.readInput(fileName)
  do 
    println(URLFactory.youtube(x))
    println(URLFactory.wikipedia(x))



object URLFactory:
  def youtube(suffix: String) =
    s"https://www.youtube.com/watch?v=${suffix}"
  def wikipedia(suffix: String) = 
    s"https://en.wikipedia.org/wiki/${suffix}"


trait FileReader:
  def readInput(fileName: String): Iterator[String] = 
    for 
      line <- Source.fromFile(fileName).getLines()
    yield
      line

object IOSingleton extends FileReader