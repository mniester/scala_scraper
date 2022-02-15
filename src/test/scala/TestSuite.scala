import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.PrivateMethodTester._
import org.jsoup.Jsoup
import scala.io.Source


class TestSuite extends AnyFunSuite:

  val matrix = """The Matrix is a 1999 science fiction action film[5][6] 
      |written and directed by the Wachowskis.[a] It is the first installment in The 
      |Matrix film series, starring Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, 
      |Hugo Weaving, and Joe Pantoliano. It depicts a dystopian future in which humanity is 
      |unknowingly trapped inside a simulated reality, the Matrix, which intelligent machines 
      |have created to distract humans while using their bodies as an energy source.[7] 
      |When computer programmer Thomas Anderson, under the hacker alias "Neo", 
      |uncovers the truth, he "is drawn into a rebellion against the machines"[7] 
      |along with other people who have been freed from the Matrix.""".stripMargin

  test("test ScalaTest") {assert (true == true)}
  // test("Jsoup scrapping") {assert(Jsoup.connect("http://en.wikipedia.org/").get().title() == "Wikipedia, the free encyclopedia")}
  test("IOSingleton.readFile") {
    val result = IOSingleton.readFile("letters.txt").get
    val pattern = "aaa\nbbb\nccc"
    assert(result == pattern)
  }

  test("UrlFactory.wikipedia") {
    val code = "aaa"
    assert(s"https://en.wikipedia.org/wiki/${code}" == UrlFactory.wikipedia(code))
  }  
  
  test("PartsOfSpeechFinder.nouns") {
    val nounsInSample = List("science", "fiction", "action", "film", "series", "future", "humanity", "reality", 
                      "machines","humans", "bodies", "energy", "source", "computer", "programmer",
                      "hacker", "truth", "rebellion", "people")
    val nounsFound = PartsOfSpeechFinder.nouns(matrix)
    var counter: Int = 0
    for 
      s <- nounsInSample 
      n <- nounsFound
      if 
        s equals n
    do
      counter += 1
    assert(counter > ((nounsInSample.length / 2) + 1))
    }
    test("TextFormatter.capitalizeSentences"){
      val sample = "hello! hello. hello? hello."
      val pattern = "Hello! Hello. Hello? Hello."
      val formatter = TextFormatter
      val func = PrivateMethod[String](Symbol("capitalizeSentences"))
      val result = formatter invokePrivate func(sample)
      assert (result equals pattern)
    }
    test("TextFormatter.bigLettersStyleFormatter") {
      val sample = ">> HELLO!\n>> HE WHISHES I  WOULD BE HERE."
      val pattern = "- Hello!\n- He whishes I would be here."
      val func = PrivateMethod[String](Symbol("bigLettersStyleFormatter"))
      val formatter = TextFormatter
      val result = formatter invokePrivate func(sample)
      assert {result equals pattern}
    }
    test ("TextFormatter.removePunctuation") {
      val finder = PartsOfSpeechFinder
      val func = PrivateMethod[String](Symbol("removePunctuation"))
      assert (finder invokePrivate func("word,") equals "word")
      assert (finder invokePrivate func("word.") equals "word")
      assert (finder invokePrivate func("word?") equals "word")
      assert (finder invokePrivate func("word!") equals "word")
      assert (finder invokePrivate func("word\"") equals "word")
      assert (finder invokePrivate func("word;") equals "word")
      assert (finder invokePrivate func("word:") equals "word")
    }
   test("TextFormatter.captionsXML") {
      val formatter = TextFormatter
      val func =  PrivateMethod[xml.Elem](Symbol("paragraphsFormatting"))
      val pattern = <captions>
          <raw>{ matrix }</raw>
          <plain>{ formatter invokePrivate func(matrix) }</plain>
          </captions>
      val result = xml.XML.loadString(TextFormatter.toCaptionsXML(matrix))
      assert ((pattern \\ "raw").text equals (result \\ "raw").text)
      assert ((pattern \\ "plain").text equals (result \\ "plain").text)
   }
   test("TextFormatter.pageXML") {
      val formatter = TextFormatter
      val func =  PrivateMethod[String](Symbol("paragraphsFormatting"))
      val noun = "matrix"
      val link = "matrix.com"
      val pattern = <page noun = { noun }>
          <link>{ link }</link>
          <raw>{ matrix }</raw>
          <plain>{ formatter invokePrivate func(matrix) }</plain>
          </page>
      val result = scala.xml.XML.loadString(TextFormatter.toPageXML(noun, link, matrix))
      assert (noun equals (result \@ "noun"))
      assert (link equals (result \\ "link").text)
      assert (matrix equals (result \\ "raw").text)
      assert (formatter invokePrivate func(matrix) equals (result \\ "plain").text)
   }
   test("IOSingleton.checkPresence"){
     assert (IOSingleton.checkPresence("src") == true)
     assert (IOSingleton.checkPresence("NOT_EXISTS") == false)
   }