import scala.io.Source
import scala.collection.mutable.ArrayBuilder
import scala.xml._

import os._

import java.io.PrintWriter
import java.io.File
import java.io.FileInputStream

import org.jsoup.Jsoup
import opennlp.tools.postag._
import opennlp.tools.tokenize.WhitespaceTokenizer
import scala.util.matching.Regex
import scala.collection.mutable.ArrayBuilder


@main 
def main(fileName: String, interval: Int): Unit =
  Config.setInterval(interval)
  if
    !IOSingleton.checkPresence("articles")
  then
    IOSingleton.mkDir("articles")
  if
    !IOSingleton.checkPresence("outputs")
  then
    IOSingleton.mkDir("outputs")
  IOSingleton.readFile(fileName)
    .map(code => (code, Scraper.scrapCaptions(code).get))
    .filter((code, rawCaptions) => !(rawCaptions eq None))
    .map((code, rawCaptions) => (code, rawCaptions, PartsOfSpeechFinder.nouns(rawCaptions).toList))
    .map((code, rawCaptions, nouns) => (code, rawCaptions, nouns.toList))
    //.foreach((code, rawCaptions, nouns) => (code, println(rawCaptions), nouns))
    .map((code, rawCaptions, nouns) => (code, rawCaptions, nouns))
   

    

case class WikiEntry(val noun: String, val link: String, val rawArticle: String)

object Config:
  var interval: Int = _

  def setInterval(nr: Int): Unit =
    var interval = nr.abs * 1000



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
  private val chevrons = Regex(">>")

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
    lineSplitting(text).map(x => narrowingText(x)).map(y => result.append(y))
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

  def run(text: String): String =
    if 
      chevrons.findFirstIn(text) != None
    then
      bigLettersStyleFormatter(text)
    else
      paragraphsFormatting(text)
  
  def toCaptionsXML(rawCaptions: String): xml.Elem =
    <captions>
    <raw>{ rawCaptions }</raw>
    <plain>{ paragraphsFormatting(rawCaptions) }</plain>
    </captions>
  
  def toPageXML(wikiEntry: WikiEntry): xml.Elem =
    <page noun = { wikiEntry.noun }>
    <link>{ wikiEntry.link }</link>
    <raw>{ wikiEntry.rawArticle }</raw>
    <plain>{ paragraphsFormatting(wikiEntry.rawArticle) }</plain>
    </page>
  
  def mergeXML(code: String, captionsXML: xml.Elem, pagesXMLs: Seq[xml.Elem]): String =
    (s"<movie code = ${code}>\n${captionsXML}\n${pagesXMLs.mkString}\n</movie>").format(PrettyPrinter(maxLineLength, 2))
  
  def convertFinalOutputToXML(output: FinalOutput): String =
    mergeXML(code = output.code, 
            captionsXML = toCaptionsXML(output.rawCaptions), 
            pagesXMLs = (for page <- output.wikiEntries yield toPageXML(page)))



class FinalOutput(val code: String, 
            val rawCaptions: String = null, 
            val wikiEntries: List[WikiEntry])
  



object PartsOfSpeechFinder:
  private val inputStream = new FileInputStream("models/opennlp-en-ud-ewt-pos-1.0-1.9.3.bin") 
  private val modelPOS = new POSModel(inputStream)
  private val taggerPOS = new POSTaggerME(modelPOS)
  private val whitespaceTokenizer = WhitespaceTokenizer.INSTANCE

  private def removePunctuation(text: String): String =
    val punctuation = List(',', '.', '?', '!', '"', ';', ':')
    text.takeWhile(!punctuation.contains(_))
  
  def nouns(text: String): Set[String] =

    val tokens: Array[String] = whitespaceTokenizer.tokenize(text)
    val tags = taggerPOS.tag(tokens)
    val sample = new POSSample(tokens, tags)
    val result = sample.toString.split(" ")
    result
        .filter(_.takeRight(4) equals "NOUN")
        .mapInPlace(_.stripSuffix("_NOUN"))
        .mapInPlace(removePunctuation(_))
        .mapInPlace(_.toLowerCase)
        .toSet



object Scraper:
  
  def scrapCaptions(code: String): Option[String] =
    Thread.sleep(Config.interval)
    val result = os.proc((pwd.toString() +  "/pyve/bin/python3"), "scraper.py").call(cwd = null, stdin = code).out.toString()
      if 
        result == "Error"
      then
        None
      else
        Some(result.drop(12).dropRight(2))
  
  def scrapSite(url: String): Option[String] =
    Thread.sleep(Config.interval)
    try
      Some(Jsoup.connect(url).timeout(1000*5).get.select("p").toString)
    catch
      case e: Exception => None



object UrlFactory:
  def wikipedia(suffix: String) = 
    s"https://en.wikipedia.org/wiki/${suffix}"




trait FileReader:
  def readFile(fileName: String): Option[String] =
    try Some { (for line <- Source.fromFile(fileName).getLines() yield line + "\n").mkString.stripTrailing }
    catch
      case e: Exception => None




trait WriterToFile:
  def writeToFile(path: String, text: String): Unit = 
    val pw = new PrintWriter(new File(path))
    pw.write(text)
    pw.close



trait checkPresence:
  def checkPresence(searched: String): Boolean =
    val (path, item) = searched.splitAt(searched.lastIndexOf('/') + 1)
    val paths = os.list(os.Path(pwd.toString ++ path)).map(_.toString)
    val lasts = paths.map(x => x.slice(x.lastIndexOf('/') + 1, x.length))
    lasts.contains(item)



trait mkDir:
  def mkDir(dirName: String): Unit =
    os.makeDir(pwd/dirName)



object IOSingleton extends FileReader, WriterToFile, checkPresence, mkDir:
  
  def getArticle(noun: String): String =
    if
      !checkPresence("/articles" ++ "/" ++ noun ++ ".txt")
    then
      Scraper.scrapSite(UrlFactory.wikipedia(noun)) match
        case Some(document) =>  writeToFile(path = "articles/" ++ noun ++ ".txt", text = document)
        case None => None
    else
        None
    readFile("articles" ++ "/" ++ noun ++ ".txt").mkString
      