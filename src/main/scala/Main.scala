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
    if 
      rawText != "TranscriptsDisabled"
    then
      println(TextFormatter.forHumans(rawText))
    //IOSingleton.writeOutput(code, scrapSubProcess(code)) 

object TextFormatter:
  val maxLineLength = 150
  
  private def WidthFormatter(lines: Array[String]): String =

    val result = StringBuilder()
    val currentLine = StringBuilder()
    for
      line <- lines
    do
      for word <- line.split(" ")
        do
          if 
            word == ">>"
          then
            result ++= currentLine
            currentLine.clear
          else if
            word.head == '-'
          then
            result ++= currentLine
            result ++= "\n"
            currentLine.clear
          if 
            (word.length + currentLine.length) >= maxLineLength
          then
            result ++= currentLine
            result ++= "\n"
            currentLine.clear
          currentLine ++= word
          currentLine ++= " "
    result.toString
  
  private def BigLettersStyleFormatter(text: String): String =
    text.toString.replaceAll("  ", " ")
        .replaceAll(">>", "\n-")
        .toLowerCase()
        .replaceAll(" i ", " I ")
  
  private def SmallLetterStyleFormatter(text: String): String =
    text
  
  private val findChevrons = ">>".r

  def forHumans(text: String) =
    val lines = text.split("\n")
    val formattedText = WidthFormatter(lines)
    if 
      findChevrons.findFirstIn(formattedText) != None
    then
      BigLettersStyleFormatter(formattedText)
    else
      SmallLetterStyleFormatter(formattedText)
    
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