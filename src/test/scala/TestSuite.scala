import org.scalatest.funsuite.AnyFunSuite
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