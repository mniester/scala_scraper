package Schemas

import slick.jdbc.SQLiteProfile.api._



class UserSchema (tag: Tag) extends Table [(Int, String)](tag, "users") {
    def key = column[Int]("key", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (key, name)
}

class ProjectSchema (tag: Tag) extends Table [(Int, String, String, String)] (tag, "projects") {
  def key = column[Int]("key", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.Unique)
  def userName = column[String]("user_name")
  def startTime = column[String]("start_time")
  def * = (key, name, userName, startTime)
}

class TaskSchema (tag: Tag) extends Table [(Int, String, String, String, Int, Int, String)] (tag, "tasks") {
  def key = column[Int]("key", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def start = column[String]("start")
  def project = column[String]("project")
  def time = column[Int]("time")
  def volume = column[Int] ("volume", O.Default(-1))
  def comment = column[String] ("comment", O.Default(null))
  def * = (key, name, start, project, time, volume, comment)
}