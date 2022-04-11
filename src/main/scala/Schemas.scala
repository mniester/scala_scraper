package Schemas

import slick.jdbc.SQLiteProfile.api._



class User (tag: Tag) extends Table [(Int, String)](tag, "users") {
    def key = column[Int]("key", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (key, name)
}

class Project (tag: Tag) extends Table [(Int, String, String, String)] (tag, "PROJECTS") {
  def key = column[Int]("KEY", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", O.Unique)
  def userName = column[String]("USER_NAME")
  def startTime = column[String]("START_TIME")
  def * = (key, name, userName, startTime)
}

class Task (tag: Tag) extends Table [(Int, String, String, String, Option[Int])] (tag, "tasks") {
  def key = column[Int]("KEY", O.PrimaryKey, O.AutoInc)
  def start = column[String]("START", O.PrimaryKey)
  def project = column[String]("PROJECT")
  def time = column[String]("TIME")
  def volume = column[Option[Int]]("VOLUME", O.Default(None))
  def * = (key, start, project, time, volume)
}