
package IO

import os._

import scrapers.Scraper
import strings.{UrlFactory, TextFormatter}
import temps.FinalOutput

trait FileReader:
  def readFile(filePath: String): Option[String] =
    try
      Some(os.read(pwd / RelPath(filePath)).stripTrailing)
    catch
      case e: Exception => None


trait WriterToFile:
  def writeToFile(path: String, text: String): Unit =
    os.write.append(pwd/RelPath(path), text)



trait checkPresence:
  def checkPresence(searched: String): Boolean =
    os.exists(pwd / RelPath(searched))



trait mkDir:
  def mkDir(dirName: String): Unit =
    os.makeDir(pwd/dirName)



object IOSingleton extends FileReader, WriterToFile, checkPresence, mkDir:

  def fetchArticle(noun: String): Boolean =
    if
      !checkPresence("articles" ++ "/" ++ noun ++ ".txt")
    then
      val document = Scraper.scrapSite(UrlFactory.wikipedia(noun))
      if
        document ne None 
      then 
        writeToFile(path = "articles/" ++ noun ++ ".txt", text = document.get)
        true
      else
        false
    else
      true
  
  def xmlsPipe(finalOutput: FinalOutput): Unit =
    val endPath = "outputs/" ++ s"${finalOutput.code}" ++ ".xml"
    IOSingleton.writeToFile(endPath, TextFormatter.toCaptionsXML(finalOutput.rawCaptions).toString)
    finalOutput.wikiEntries
        .filter(entry => entry.hasArticle)
        .foreach(entry => IOSingleton.writeToFile(endPath, TextFormatter.toPageXML(entry.noun, entry.link, IOSingleton.readFile(s"articles/${entry.noun}.txt").getOrElse("Not Found")).mkString))
