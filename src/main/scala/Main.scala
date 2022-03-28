import scala.io.Source
import scala.collection.mutable.ArrayBuilder

import os._

import org.jsoup.Jsoup


import config.Config
import scrapers.Scraper
import IO.IOSingleton
import strings.UrlFactory
import temps.{WikiEntry, FinalOutput}
import ML.PartsOfSpeechFinder


@main 
def main(filePath: String, interval: Int): Unit =
  if
    interval.toInt == 0
  then
    throw new Exception("Interval number must be other than zero")
    
    /* I know that exception are not well seen in FP, but in upper line it seems to be best option,
    because this error is for human eyes. */
  
  Config.setInterval(interval) // sets interval for scraper, to avoid denial of service
  if
    !IOSingleton.checkPresence("articles")
  then
    IOSingleton.mkDir("articles")
  if
    !IOSingleton.checkPresence("outputs")
  then
    IOSingleton.mkDir("outputs")
  
  /*UP^: checks if there are directories for articles and outputs, if not- creates them */
  
  def wikiEntryListFactory(nouns: List[String]): List[WikiEntry] =
    for
      noun <- nouns
    yield                                                   
      new WikiEntry(noun = noun, 
                    link = UrlFactory.wikipedia(noun), 
                    hasArticle = IOSingleton.fetchArticle(noun))

  IOSingleton.readFile(filePath)
    .getOrElse("")
    .split('\n')
    .map(code => (code, Scraper.scrapCaptions(code).get)) // takes code from file
    .filter((code, rawCaptions) => !(rawCaptions eq None)) // checks if captrions are not None
    .map((code, rawCaptions) => (code, rawCaptions, PartsOfSpeechFinder.nouns(rawCaptions).toList)) // searches for nouns
    .map((code, rawCaptions, nouns) => (code, rawCaptions, wikiEntryListFactory(nouns)))
    .map((code, rawCaptions, entries) => FinalOutput(code, rawCaptions, entries)) // form output
    .map(finalOutput => IOSingleton.xmlsPipe(finalOutput)) // saves output ot XML