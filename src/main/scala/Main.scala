import scala.io.Source
import org.jsoup.Jsoup
import os.proc
import os.pwd
import java.io.PrintWriter
import java.io.File
import scala.collection.mutable


@main 
def main(fileName: String): Unit =
  for
    code <- IOSingleton.readInput(fileName)
  do
    val rawText = scrapSubProcess(code)
    TextFormatter.forHumans(rawText)
    //IOSingleton.writeOutput(code, scrapSubProcess(code)) 

object TextFormatter:
  val maxLineLength = 150
  
  private def lineTrimmer(lines: Array[String]): String =
    val output = StringBuilder()
    for
      line <- lines
    do
      if 
        line.length > maxLineLength
      then
        output ++= inner(line)
      else
        output ++= line
    return output.toString

  def forHumans(text: String) =
    val lines = text.replace('\n', ' ')
    println(lines.replaceAll(">>", "\n-").toLowerCase())

    


def scrapSubProcess(code: String): String =
  os.proc((pwd.toString() +  "/pyve/bin/python3"), "scraper.py").call(cwd = null, stdin = code).out.toString().drop(12).dropRight(2)

object UrlFactory:
  def wikipedia(suffix: String) = 
    s"https://en.wikipedia.org/wiki/${suffix}"


trait FileReader:
  def readInput(fileName: String): Iterator[String] = 
    for 
      line <- Source.fromFile(fileName).getLines()
    yield
      line

trait WriterToFile:
  def writeOutput(code: String, text: String): Unit = 
    val pw = new PrintWriter(new File(code + ".txt"))
    pw.write(text)
    pw.close

object IOSingleton extends FileReader, WriterToFile