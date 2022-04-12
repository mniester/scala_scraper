package DBs

import com.typesafe.config.{Config, ConfigFactory}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.io.File
import slick.jdbc.SQLiteProfile.api._
import Schemas._

object SQLite {
  val configFile = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
  val api = Database.forConfig(path = "", config = configFile.getConfig("db.sqlite3"))
  val users = TableQuery[User]
  val projects = TableQuery[Project]
  val tasks = TableQuery[Task]
  val createDB = DBIO.seq((users.schema ++ projects.schema ++ tasks.schema).createIfNotExists)
  def setup (): Unit =
    Await.result(this.api.run(createDB), Duration(20, "seconds"))
}
