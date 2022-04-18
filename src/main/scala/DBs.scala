package DBs

import os.pwd
import com.typesafe.config.{Config, ConfigFactory}
import scala.concurrent.Await
import java.io.File
import slick.basic.BasicBackend
import slick.jdbc.SQLiteProfile.api._

import Settings.CommonSettings
import Schemas._
import Models._
import Queries._ 
import java.time.Clock



abstract class DB {
  val configFile: Config
  val cursor: Database
  lazy val users = TableQuery[UserSchema]
  lazy val projects = TableQuery[ProjectSchema]
  lazy val tasks = TableQuery[TaskSchema]
  def resetSequences: Unit

  def purge (): Unit = {
    cursor.run(users.filter(_.name.length > 0).delete)
    cursor.run(projects.filter(_.name.length > 0).delete)
    cursor.run(tasks.filter(_.name.length > 0).delete)
    resetSequences
  }
  
  def setup (): Unit =
    {val createDB = DBIO.seq((users.schema ++ projects.schema ++ tasks.schema).createIfNotExists)
    Await.result(cursor.run(createDB), CommonSettings.dbWaitingDuration)}
  
  def addUser (user: UserModel): Unit =
    Await.result(cursor.run(users += user.toInputTuple), CommonSettings.dbWaitingDuration) 
     
  def addProject (project: ProjectModel): Unit =
    Await.result(cursor.run(projects += project.toInputTuple), CommonSettings.dbWaitingDuration)
  
  def addTask (task: TaskModel): Unit =
    Await.result(cursor.run(tasks += task.toInputTuple), CommonSettings.dbWaitingDuration)

  // TODO (FAILURE) generalize gets

  def getUserByName(query: UserQueryByName): Seq[UserModel] = {
    val action = cursor.run(users.filter(_.name === query.name).result)
    Await.result(action, CommonSettings.dbWaitingDuration).map(x => UserModel(x._1, x._2))
  }

  def delUserByName(query: UserQueryByName): Unit = {
    Await.result(cursor.run(users.filter(_.name === query.name).delete), CommonSettings.dbWaitingDuration)
  }

  def getProjectByName(query: ProjectQueryByName) = {
    val action = cursor.run(projects.filter(_.name === query.name).filter(_.deleteTime.length === 0).result)
    Await.result(action, CommonSettings.dbWaitingDuration).map(x => ProjectModel(x._1, x._2, x._3, x._4, x._5))
  }

  def delProjectByName(query: ProjectQueryByName): Unit = {
    val removeProject = cursor.run(projects.filter(_.name === query.name).map(_.deleteTime).update("aaa"))
    val removeTasks = cursor.run(tasks.filter(_.project === query.name).map(_.deleteTime).update("aaa"))
    Await.result(removeProject, CommonSettings.dbWaitingDuration)
    Await.result(removeTasks, CommonSettings.dbWaitingDuration)
  }

  def getTaskByName(query: TaskQueryByName) = {
    val action = cursor.run(tasks.filter(_.name === query.name).filter(_.deleteTime.length === 0).result)
    Await.result(action, CommonSettings.dbWaitingDuration).map(x => TaskModel(x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10))
  }

  def delTaskByName(query: TaskQueryByName): Unit = {
    Await.result(cursor.run(tasks.filter(_.name === query.name).map(_.deleteTime).update("aaa")), CommonSettings.dbWaitingDuration)
  }
}

object SQLite extends DB {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val configFile = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
  val cursor = Database.forConfig(path = "", config = configFile.getConfig("db.sqlite3"))

  def resetSequences: Unit = {
    val resetSequences = sqlu"""UPDATE sqlite_sequence SET seq = 0; VACUUM;"""
    Await.result(cursor.run(resetSequences), CommonSettings.dbWaitingDuration)
  }
}