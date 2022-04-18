import java.time.LocalDate

object Dev extends App {
  //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val time1 = LocalDate.parse("2000-01-01T00:01:01")
  val time2 = LocalDate.parse("2000-02-02T00:01:01")
}