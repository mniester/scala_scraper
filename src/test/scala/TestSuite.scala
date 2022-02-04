import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.PrivateMethodTester._
import org.jsoup.Jsoup
import scala.io.Source


class TestSuite extends AnyFunSuite:
  test("test ScalaTest") {assert (true == true)}
  // test("Jsoup scrapping") {assert(Jsoup.connect("http://en.wikipedia.org/").get().title() == "Wikipedia, the free encyclopedia")}
  test("Code Reader") {
    for 
      (code, sample) <- IOSingleton.readInput("letters.txt").zip(List("aaa", "bbb", "ccc"))
    do
      assert(code == sample)
    }

  test("Wikipedia URLs Factory") {
    for 
      (code, sample) <- IOSingleton.readInput("letters.txt").zip(List("aaa", "bbb", "ccc"))
    do
      assert(s"https://en.wikipedia.org/wiki/${code}" == s"https://en.wikipedia.org/wiki/${sample}")
    }
  
    test("Test part of speech finder") {
      val matrix = """The Matrix is a 1999 science fiction action film[5][6] 
      written and directed by the Wachowskis.[a] It is the first installment in The 
      Matrix film series, starring Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, 
      Hugo Weaving, and Joe Pantoliano. It depicts a dystopian future in which humanity is 
      unknowingly trapped inside a simulated reality, the Matrix, which intelligent machines 
      have created to distract humans while using their bodies as an energy source.[7] 
      When computer programmer Thomas Anderson, under the hacker alias "Neo", 
      uncovers the truth, he "is drawn into a rebellion against the machines"[7] 
      along with other people who have been freed from the Matrix."""
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
    test("SmallLetterStyleFormatter - first test of private method") {
      val formatter = TextFormatter
      val func = PrivateMethod[String](Symbol("SmallLetterStyleFormatter"))
      assert (formatter invokePrivate func("abc") equals "abc")
    }
