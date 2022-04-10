package DataBases

import os.exists
import java.util.UUID


object UUIDFactory {
  def apply (): String =
    UUID.randomUUID.toString
}

//case class Task(start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String])

object SQLDataBase {
  def checkPresence (fileName: String): Boolean =
    os.exists(os.pwd / fileName)
  def createNewBase (fileName: String): Unit =
    s"""sqlite3 ${os.pwd} / ${fileName} "CREATE TABLE user (uuid text, name, text);
    CREATE TABLE project (author text, start_time text);
    CREATE TABLE task (start text, project text, time int, volume int, comment text);" 
    """
}