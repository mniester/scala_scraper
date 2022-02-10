import scala.io.Source
import scala.collection.mutable
import scala.xml._

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
  ???
  // for
  //   code <- IOSingleton.readInput(fileName)
  // do
  //   val rawText = Scraper.scrapSubtitles(code)
  //   if 
  //     rawText != "TranscriptsDisabled"
  //   then
  //     TextFormatter.captionFormatting(rawText)
  //     for
  //       noun <- PartsOfSpeechFinder.nouns(rawText)
  //     do
  //       val wikiSite = Scraper.scrapSite(UrlFactory.wikipedia(noun))
  //       if
  //         wikiSite != "Error 40x"
  //       then
  //         TextFormatter.pageFormatting(wikiSite)



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
  
  val maxLineLength = 120

  private def paragraphsFormatting(text: String): String =

    def narrowingText(text: String, builder: StringBuilder = StringBuilder()): StringBuilder =
      if
        text.length < maxLineLength
      then
        builder.addOne('\n').append(text)
      else
        val cutPoint = text.slice(0, maxLineLength).lastIndexOf(' ')
        val (alpha, beta) = text.splitAt(cutPoint)
        builder.addOne('\n').append(alpha.stripLeading)
        narrowingText(beta, builder)
    
    def lineSplitting(text: String): Array[String] = 
      text.replaceAll("\n", " ")
          .replaceAll(" -", "\n- ")
          .split("\n")

    val result = StringBuilder()
    val lines = lineSplitting(text)
    for
      line <- lines
    do
      result.append(narrowingText(line))
    result.toString.replace("  ", " ").stripLeading
    
  
  private def capitalizeSentences(text: String): String =
    val start = text.slice(0,2)
    val result = StringBuilder().addOne(start.head.toUpper).addOne(start.last)
    for 
      part <- text.sliding(3)
    do
      if
        "\\?!.-".contains(part.head) && (part(1) == ' ') || part(1) == '\"'
      then
        result.addOne(part.last.toUpper)
      else
        result.addOne(part.last)
    result.toString

  private def bigLettersStyleFormatter(text: String): String =
    capitalizeSentences(paragraphsFormatting((text
        .replaceAll(">>", "-")
        .toLowerCase()
        .replaceAll(" i ", " I ")
        .stripLeading())))
    
  private val chevrons = Regex(">>")

  def captionFormatting(text: String): String =
    if 
      chevrons.findFirstIn(text) != None
    then
      bigLettersStyleFormatter(text)
    else
      paragraphsFormatting(text)
  
  def pageXML(noun: String, linkToArticle: String, rawWikiArticle: String): xml.Elem =
    <page noun = { noun }>
      <link>{ linkToArticle }</link>
      <raw>{ rawWikiArticle }</raw>
      <plain>{ paragraphsFormatting(rawWikiArticle) }</plain>
    </page>
  
  def captionsXML(rawCaptions: String): xml.Elem =
    <captions>
      <raw>{ rawCaptions }</raw>
      <plain>{ paragraphsFormatting(rawCaptions) }</plain>
    </captions>
  
  // def xmlMerging(code: String, captionsXML: xml.Elem, articlesXMLs: Array[xml.Elem]) =
  //   <header code = { code }> ++ captionsXML


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
    os.proc((pwd.toString() +  "/pyve/bin/python3"), "scraper.py")
      .call(cwd = null, stdin = code).out.toString().drop(12).dropRight(2)
  
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


// trait articlesControl:
//   def checkPresentFiles(title: String): Boolean =
//     ???
  

object IOSingleton extends FileReader, WriterToFile