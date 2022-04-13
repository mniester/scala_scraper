package Inputs

import org.joda.time.DateTime
import Settings.CommonSettings


abstract class Input {
  def toInputTuple (): Product
}

case class User(name: String) extends Input {
  def toInputTuple(): Tuple2[Int, String] =
    (-1, name)
}

case class Project(name: String, userName: String, startTime: String) extends Input {
  def toInputTuple(): Tuple4[Int, String, String, String] =
    (-1, name, userName, startTime)
}

case class Task(start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String]) extends Input {
  def toInputTuple(): Tuple6[Int, String, String, Int, Option[Int], Option[String]] = 
    (-1, start, project, time, volume, comment)
}


object CheckISOTimeFormat {
  def apply (string: String): Boolean =
    try {
      new DateTime(string)
      true
    }
    catch {
      case _: Throwable => false
    }
}

object UserFactory {
  def apply (name: String): Option[User] =
    if (name.length <= CommonSettings.maxUserNameLength) {
      Some(User(name))
    } else {
      None
    }
}

object ProjectFactory {
  def apply (name: String, userName: String, startTime: String): Option[Project] =
    if ((name.length <= CommonSettings.maxProjectNameLength) && (userName.length <= CommonSettings.maxUserNameLength) && CheckISOTimeFormat(startTime)) {
      Some(Project(name, userName, startTime))
    } else {
      None
    }
}

object TaskFactory {
  def apply (start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String]): Option[Task] =
    if ((!comment.isEmpty) && (comment.head.length <= CommonSettings.maxTaskCommentLength)) {
      Some(Task(start, project, time,  volume, comment))
    } else {
      None
    }
}