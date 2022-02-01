import scala.io.Source
import org.jsoup.Jsoup
import os.proc
import os.pwd

@main 
def main(fileName: String): Unit =
  for
    code <- IOSingleton.readInput(fileName)
  do
    val result = scrapSubProcess(code)

def scrapSubProcess(code: String) =
  val text: String  = os.proc((pwd.toString() +  "/pyve/bin/python3"), "scraper.py").call(cwd = null, stdin = code).toString()
  print(text)

object UrlFactory:
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