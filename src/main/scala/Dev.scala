import java.time.LocalDateTime
import Pencilcase._
import Models._
import slick.dbio.DBIOAction

object Dev extends App {
  //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  //println(Pencilcase.isEarlier("2000-02-01T00:01:01", "2000-02-01T00:01:01"))
  println(LocalDateTime.parse("2000-02-01T00:01:01").compareTo(LocalDateTime.parse("2000-02-01T00:01:01")))
}