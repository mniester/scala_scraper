package DataBases

import slick.jdbc.SQLiteProfile.api._
import os._
import java.util.UUID


object UUIDFactory {
  def apply (): String =
    UUID.randomUUID.toString
}

//case class Task(start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String])

object SQLiteDataBase {
  def checkPresence (fileName: String): Boolean =
    os.exists(os.pwd / fileName)
  def createNewBase (fileName: String): Unit =
    ???
}