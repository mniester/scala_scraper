import os._
import org.scalatest.funsuite.AnyFunSuite


class TestPrint extends AnyFunSuite:
  
  val matrix = """The Matrix is a 1999 science fiction action film[5][6] 
                 |written and directed by the Wachowskis.[a] It is the first installment in The 
                 |Matrix film series, starring Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, 
                 |Hugo Weaving, and Joe Pantoliano. It depicts a dystopian future in which humanity is 
                 |unknowingly trapped inside a simulated reality, the Matrix, which intelligent machines 
                 |have created to distract humans while using their bodies as an energy source.[7] 
                 |When computer programmer Thomas Anderson, under the hacker alias "Neo", 
                 |uncovers the truth, he "is drawn into a rebellion against the machines"[7] 
                 |along with other people who have been freed from the Matrix.""".stripMargin
  
  val human = os.read(pwd / "humanhead.html")
  println(TextFormatter.toCaptionsXML(human))