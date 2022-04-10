import os._
import com.typesafe.config.{Config, ConfigFactory}
import java.io.File

object Main extends App {
  val conf = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/app.conf"))
  println(conf.getConfig("maxlen"))
}