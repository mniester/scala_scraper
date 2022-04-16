package DBs

import os.pwd
import com.typesafe.config.{Config, ConfigFactory}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.io.File
import slick.jdbc.SQLiteProfile.api._

import Schemas._
import Models._
import Queries._ 
import java.time.Clock



abstract class DB {
  val configFile: Config
  val cursor: Any
  def setup (): Unit
}

object SQLite extends DB {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val configFile = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
  val cursor = Database.forConfig(path = "", config = configFile.getConfig("db.sqlite3"))
  lazy val users = TableQuery[UserSchema]
  private lazy val projects = TableQuery[ProjectSchema]
  private lazy val tasks = TableQuery[TaskSchema]
  def setup (): Unit =
    {val createDB = DBIO.seq((users.schema ++ projects.schema ++ tasks.schema).createIfNotExists)
    Await.result(this.cursor.run(createDB), Duration(20, "seconds"))}
  
  def addUser (user: UserModel): Unit =
    cursor.run(users += user.toInputTuple)
  
  def addProject (project: ProjectModel): Unit =
    cursor.run(projects += project.toInputTuple)
  
  def addTask (task: TaskModel): Unit =
    cursor.run(tasks += task.toInputTuple)

  // TODO (FAILURE) generalize gets

  def getUserByName(query: UserQuery) = {
    val action = cursor.run(users.filter(_.name === query.name).result)
    Await.result(action, Duration(10, "seconds")).map(x => UserModel(x._1, x._2))
  }

  def getProjectByName(query: ProjectQuery) = {
    val action = cursor.run(projects.filter(_.name === query.name).result)
    Await.result(action, Duration(10, "seconds")).map(x => ProjectModel(x._1, x._2, x._3, x._4))
  }

   def getTaskByName(query: TaskQuery) = {
    val action = cursor.run(tasks.filter(_.name === query.name).result)
    Await.result(action, Duration(10, "seconds")).map(x => TaskModel(x._1, x._2, x._3, x._4, x._5, x._6, x._7))
  }

  def convertClockToString(clock: Clock): String = {
    clock.instant().toString()
  }

}