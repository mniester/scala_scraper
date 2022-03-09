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
def main(fileName: String, interval: Int): Unit =
  if
    interval.toInt == 0
  then
    throw new Exception("Interval number must be other than zero")
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
    .map((code, rawCaptions, nouns) => (code, 
                                        rawCaptions, for
                                                        noun <- nouns
                                                      yield
                                                        val newLink = UrlFactory.wikipedia(noun)
                                                        val hasArticle = IOSingleton.fetchArticle(noun)                                                       
                                                        new WikiEntry (noun, link = newLink, hasArticle = hasArticle)))
    .map((code, rawCaptions, entries) => FinalOutput(code, rawCaptions, entries))
    .map(finalOutput => IOSingleton.xmlsPipe(finalOutput))


