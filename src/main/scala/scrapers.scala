package scrapers

import os._

import config.Config
import org.jsoup.Jsoup


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
