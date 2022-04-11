package Schemas

import slick.jdbc.SQLiteProfile.api._
import Inputs._



class User (tag: Tag) extends Table [(String)](tag, "USERS") {
    def name = column[String]("NAME", O.PrimaryKey)
    def * = (name)
}

class Project (tag: Tag) extends Table [(String, String, String)] (tag, "PROJECTS") {
  def name = column[String]("NAME", O.PrimaryKey)
  def userName = column[String]("USER_NAME")
  def startTime = column[String]("START_TIME")
  def * = (name, userName, startTime)
}

class Task (tag: Tag) extends Table [(String, String, String, Option[Int])] (tag, "PROJECTS") {
  def start = column[String]("START", O.PrimaryKey)
  def project = column[String]("PROJECT")
  def time = column[String]("TIME")
  def volume = column[Option[Int]]("VOLUME", O.Default(None))
  def * = (start, project, time, volume)
}