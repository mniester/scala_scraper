package strings

import scala.util.matching.Regex
import config.Config


object UrlFactory:
  def wikipedia(suffix: String) = 
    s"https://en.wikipedia.org/wiki/${suffix}"



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
  private val chevrons = Regex(">>")

  private def paragraphsFormatting(text: String): String =

    def narrowingText(text: String, builder: StringBuilder = StringBuilder()): StringBuilder = // narrows text to length in Config
      if
        text.length < Config.maxLineLength
      then
        builder.addOne('\n').append(text)
      else
        var cutPoint = text.slice(0, Config.maxLineLength).lastIndexOf(' ')
        if 
          cutPoint < 1
        then
          cutPoint = Config.maxLineLength
        val (alpha, beta) = text.splitAt(cutPoint)
        builder.addOne('\n').append(alpha.stripLeading)
        narrowingText(beta, builder)
    
    def lineSplitting(text: String): Array[String] = // splits text into a array of shorter strings
      text.replaceAll("\n", " ")
          .replaceAll(" -", "\n- ")
          .split("\n")

    val result = StringBuilder()
    lineSplitting(removeBoth(text)).map(x => narrowingText(x)).map(y => result.append(y)) // add formatted line to Builder
    result.toString.replace("  ", " ").stripLeading // returns formatted string 

  private def capitalizeSentences(text: String): String = // That is the last for-lop, I have left in code, as I found regex-FP solution overcomplicated 
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

  private def bigLettersStyleFormatter(text: String): String = // Some captions have this style:
    capitalizeSentences(paragraphsFormatting(text              // >> HELLO!
        .replaceAll(">>", "-")                                 // this method changes it to:
        .toLowerCase()                                         // -Hello!
        .replaceAll(" i ", " I ")
        .stripLeading()))

  def run(text: String): String = // main method of object
    if 
      chevrons.findFirstIn(text) != None
    then
      bigLettersStyleFormatter(text)
    else
      paragraphsFormatting(text)
  
  def toCaptionsXML(rawCaptions: String): String =
    val formatted = run(rawCaptions)
    s"""<captions>
    <raw>${ rawCaptions }</raw>
    <plain>${ formatted }</plain>
    </captions>\n"""
  
  def toPageXML(noun: String, link: String, rawArticle: String): String = // fills XML file with data
    val formatted = run(rawArticle) 
    s"""\n<page noun = "${ noun }">
    <link>${ link }</link>
    <raw>${ rawArticle }</raw>
    <plain>${ formatted }</plain>
    </page>\n"""