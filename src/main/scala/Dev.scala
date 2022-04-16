
import java.time.Clock
import Strings._


object Dev extends App {
  //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  implicit val now = Clock.systemDefaultZone()
  val x = JwtCoder.encode("""{"abc": "123"}""")
  println(x)
  println(JwtCoder.decode(x))
}