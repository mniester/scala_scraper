import java.time.LocalDateTime
import Pencilcase._

object Dev extends App {
  //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val time1 = LocalDateTime.parse("2000-01-01T00:01:01")
  val time2 = LocalDateTime.parse("2000-02-02T00:01:01")
  println(time1.compareTo(time2))
}