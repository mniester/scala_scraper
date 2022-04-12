package Schemas

import slick.jdbc.SQLiteProfile.api._



class User (tag: Tag) extends Table [(Int, String)](tag, "users") {
    def key = column[Int]("key", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (key, name)
}

class Project (tag: Tag) extends Table [(Int, String, String, String)] (tag, "projects") {
  def key = column[Int]("key", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.Unique)
  def userName = column[String]("user_name")
  def startTime = column[String]("start_time")
  def * = (key, name, userName, startTime)
}

class Task (tag: Tag) extends Table [(Int, String, String, String, Option[Int])] (tag, "tasks") {
  def key = column[Int]("key", O.PrimaryKey, O.AutoInc)
  def start = column[String]("start")
  def project = column[String]("project")
  def time = column[String]("time")
  def volume = column[Option[Int]]("volume", O.Default(None))
  def * = (key, start, project, time, volume)
}