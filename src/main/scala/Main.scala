import org.jsoup.Jsoup

@main def main(args: String*): Unit = 
  val lang = if (args.length != 2) then "en" else args(0).toLowerCase()
  val doc = Jsoup.connect("http://en.wikipedia.org/").get()
  println(doc.title())