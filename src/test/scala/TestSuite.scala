import org.scalatest.funsuite.AnyFunSuite
import org.jsoup.Jsoup
import scala.io.Source


class TestSuite extends AnyFunSuite:
  test("test ScalaTest") {assert (true == true)}
  test("Jsoup scrapping") {assert(Jsoup.connect("http://en.wikipedia.org/").get().title() == "Wikipedia, the free encyclopedia")}
  test("Code Reader") {val lines = Source.fromFile("fakemoviecodes.txt").getLines.toList == List("aaa", "bbb", "ccc")}
