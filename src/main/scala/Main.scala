import os._
import com.typesafe.config.{Config, ConfigFactory}
import java.io.File
import slick.jdbc.SQLiteProfile.api._
import javax.xml.crypto.Data
import javax.sql.DataSource

object Main extends App {
  val configFile = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
  val db = Database.forConfig(path = "", config = configFile.getConfig("db.sqlite3"))
}