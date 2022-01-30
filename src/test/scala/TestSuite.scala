import org.scalatest.funsuite.AnyFunSuite
import org.jsoup.Jsoup


class TestSuite extends AnyFunSuite:
  test("test ScalaTest") {assert (true == true)}
  test("Jsoup scrapping") {assert(Jsoup.connect("http://en.wikipedia.org/").get().title() == "Wikipedia, the free encyclopedia")}
