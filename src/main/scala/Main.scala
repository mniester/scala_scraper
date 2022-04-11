import os._
import com.typesafe.config.{Config, ConfigFactory}
import java.io.File
import slick.jdbc.SQLiteProfile.api._
import scala.concurrent.{Await}



object Main extends App {
  val configFile = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
  val db = Database.forConfig(path = "", config = configFile.getConfig("db.sqlite3"))
}