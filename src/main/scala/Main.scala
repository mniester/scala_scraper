import os._
import com.typesafe.config.{Config, ConfigFactory}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.io.File
import slick.jdbc.SQLiteProfile.api._
import Schemas._


object Main extends App {
  val configFile = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
  val db = Database.forConfig(path = "", config = configFile.getConfig("db.sqlite3"))
  val users = TableQuery[User]
  val projects = TableQuery[Project]
  val tasks = TableQuery[Task]
  val createDB = DBIO.seq((users.schema ++ projects.schema ++ tasks.schema).createIfNotExists) // Int in first position is required by schema, it will not appear in DB
}