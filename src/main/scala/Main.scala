import scala.io.Source
import org.jsoup.Jsoup
import os.proc
import os.pwd
import java.io.PrintWriter
import java.io.File
import scala.collection.mutable
import opennlp.tools.postag._
import opennlp.tools.tokenize.WhitespaceTokenizer
import java.io.FileInputStream
import scala.util.matching.Regex


@main 
def main(fileName: String): Unit =
  for
    code <- IOSingleton.readInput(fileName)
  do
    val rawText = scrapSubtitles(code)
    if 
      rawText != "TranscriptsDisabled"
    then
      val textStyled = TextFormatter.StyleOne(rawText)
      for 
        noun <- PartsOfSpeechFinder.nouns(textStyled)
      do
        println(noun)
    else
      println("TODO - log entry")
    //IOSingleton.writeOutput(code, scrapSubtitles(code)) 

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

  def StyleOne(text: String): String =
    val lines = text.split("\n")
    val formattedText = WidthFormatter(lines)
    if 
      findChevrons.findFirstIn(formattedText) != None
    then
      BigLettersStyleFormatter(formattedText)
    else
      SmallLetterStyleFormatter(formattedText)



object PartsOfSpeechFinder:
  val inputStream = new FileInputStream("models/opennlp-en-ud-ewt-pos-1.0-1.9.3.bin") 
  val modelPOS = new POSModel(inputStream)
  val taggerPOS = new POSTaggerME(modelPOS)
  val whitespaceTokenizer = WhitespaceTokenizer.INSTANCE

  private def removePunctuation(text: String): String =
    text
  
  def nouns(text: String): Array[String] =
    val tokens: Array[String] = whitespaceTokenizer.tokenize(text)
    val tags = taggerPOS.tag(tokens)
    val sample = new POSSample(tokens, tags)
    val result = sample.toString.split(" ")
    for 
      word <- result
      if
        word.takeRight(4) equals "NOUN" 
    yield 
      word.stripSuffix("_NOUN")


def scrapSubtitles(code: String): String =
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