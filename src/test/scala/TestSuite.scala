import org.scalatest.funsuite.AnyFunSuite
import org.jsoup.Jsoup
import scala.io.Source


class TestSuite extends AnyFunSuite:
  test("test ScalaTest") {assert (true == true)}
  // test("Jsoup scrapping") {assert(Jsoup.connect("http://en.wikipedia.org/").get().title() == "Wikipedia, the free encyclopedia")}
  test("Code Reader")
    {
    for 
      (code, sample) <- IOSingleton.readInput("testcodes.txt").zip(List("aaa", "bbb", "ccc"))
    do
      assert(code == sample)
    }
  test("URLs Factory") {
    for 
      (code, sample) <- IOSingleton.readInput("testcodes.txt").zip(List("aaa", "bbb", "ccc"))
    do
      assert(s"https://www.youtube.com/watch?v=${code}" == s"https://www.youtube.com/watch?v=${sample}")
    }