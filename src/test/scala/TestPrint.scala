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

  test("TextForrmatter.mergeXML") {
     val code = "FakeCode"
     val entryOne = new WikiEntry(noun = "entryOne", link = "entry.one", rawArticle = matrix)
     val entryTwo = new WikiEntry(noun = "entryTwo", link = "entry.two", rawArticle = matrix)
     val finalOutput = FinalOutput(code, matrix, List(entryOne, entryTwo))
     println(TextFormatter.convertFinalOutputToXML(finalOutput))
   }