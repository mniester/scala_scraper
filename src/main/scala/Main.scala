import scala.io.Source
import scala.collection.mutable
import scala.util.control.Breaks._

import os.proc
import os.pwd

import java.io.PrintWriter
import java.io.File
import java.io.FileInputStream

import org.jsoup.Jsoup
import opennlp.tools.postag._
import opennlp.tools.tokenize.WhitespaceTokenizer
import scala.util.matching.Regex


@main 
def main(fileName: String): Unit =
  for
    code <- IOSingleton.readInput(fileName)
  do
    val rawText = Scraper.scrapSubtitles(code)
    if 
      rawText != "TranscriptsDisabled"
    then
      val textStyled = TextFormatter.captionFormatting(rawText)
      for 
        noun <- PartsOfSpeechFinder.nouns(textStyled)
      do
        println(noun)
        //println(TextFormatter.removeBoth(Scraper.scrapSite(UrlFactory.wikipedia(noun))))
        break
        //Scraper.scrapSite(UrlFactory.wikipedia(noun))
    else
      println("TODO - log entry")
    //IOSingleton.writeOutput(code, scrapSubtitles(code)) 


trait RegexRemover:

  private val core = "(.+?)"
  private val htmlTags = Seq("<", ">")
  private val squareBrackets = Seq("\\[", "\\]")
  private val htmlTagsRegex = (htmlTags.head ++ core ++ htmlTags.last)
  private val squareBracketsRegex = (squareBrackets.head ++ core ++ squareBrackets.last)
  
  def removeHTMLtags(text: String): String =
    Regex(htmlTagsRegex).replaceAllIn(text, "")
  
  def removeSquareBrackets(text: String): String =
    Regex(squareBracketsRegex).replaceAllIn(text, "")
  
  def removeBoth(text: String): String =
    val tags = Regex(htmlTagsRegex + "|" + squareBracketsRegex)
    tags.replaceAllIn(text, "")



object TextFormatter extends RegexRemover:
  val maxLineLength = 200
  
  private def widthFormatter(lines: Array[String]): String =
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
  
  private def capitalizeSentences(text: String): String =
    var x = true
    val sentenceEnds = List('.', '?', '!')
    val result = StringBuilder()
    for
      char <- text
    do
      if                                       
        char.isLetter && x == true
      then
        result.addOne(char.toUpper)
        x = false
      else 
        if
          sentenceEnds.contains(char)
        then 
          x = true
          result.addOne(char)
        else
          result.addOne(char)
    result.toString
  
  private def smallLetterStyleFormatter(text: String): String =
    text
  
  private def bigLettersStyleFormatter(text: String): String =
    capitalizeSentences(text
        .replaceAll(">>", "\n-")
        .toLowerCase()
        .replaceAll(" i ", " I ")
        .replaceAll("  ", " ")
        .stripLeading())
    
  private val chevrons = Regex(">>")

  private def splitToSentences(text: String): Array[String] = text.split("\n")

  private def findPTagged(text: String): String =
    text 

  def captionFormatting(text: String): String =
    val formattedText = widthFormatter(splitToSentences(text))
    if 
      chevrons.findFirstIn(formattedText) != None
    then
      bigLettersStyleFormatter(formattedText)
    else
      smallLetterStyleFormatter(formattedText)
  
  def pageFormatting(text: String): String =
    widthFormatter(splitToSentences(text))



object PartsOfSpeechFinder:
  private val inputStream = new FileInputStream("models/opennlp-en-ud-ewt-pos-1.0-1.9.3.bin") 
  private val modelPOS = new POSModel(inputStream)
  private val taggerPOS = new POSTaggerME(modelPOS)
  private val whitespaceTokenizer = WhitespaceTokenizer.INSTANCE

  private def removePunctuation(text: String): String =
    val punctuation = List(',', '.', '?', '!', '"', ';', ':')
    text.takeWhile(!punctuation.contains(_))
  
  def nouns(text: String): Array[String] =
    val tokens: Array[String] = whitespaceTokenizer.tokenize(text)
    val tags = taggerPOS.tag(tokens)
    val sample = new POSSample(tokens, tags)
    val result = sample.toString.split(" ")
    result
        .filter(_.takeRight(4) equals "NOUN")
         .mapInPlace(_.stripSuffix("_NOUN"))
         .mapInPlace(removePunctuation(_))


object Scraper:
  
  def scrapSubtitles(code: String): String =
    os.proc((pwd.toString() +  "/pyve/bin/python3"), "scraper.py").call(cwd = null, stdin = code).out.toString().drop(12).dropRight(2)
  
  def scrapSite(url: String): String =
    Jsoup.connect(url).get().select("p").toString




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