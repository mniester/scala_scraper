package DBs

import os.pwd
import com.typesafe.config.{Config, ConfigFactory}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.io.File
import slick.jdbc.SQLiteProfile.api._
import Schemas._
import Inputs.Input


abstract class DB {
  val configFile: Config
  val cursor: Any
  def setup (): Unit
}


object SQLite extends DB { 
  val configFile = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
  val cursor = Database.forConfig(path = "", config = configFile.getConfig("db.sqlite3"))
  lazy val users = TableQuery[User]
  lazy val projects = TableQuery[Project]
  lazy val tasks = TableQuery[Task]
  
  def setup (): Unit =
    {val createDB = DBIO.seq((users.schema ++ projects.schema ++ tasks.schema).createIfNotExists)
    Await.result(this.cursor.run(createDB), Duration(20, "seconds"))}
}