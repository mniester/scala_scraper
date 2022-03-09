import os._
import org.scalatest.funsuite.AnyFunSuite


class TestPrint extends AnyFunSuite:
  
  val sample = os.read(pwd / RelPath("src/test/scala/sample.html"))
  println(TextFormatter.toCaptionsXML(sample))