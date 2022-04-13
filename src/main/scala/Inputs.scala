package Inputs

import org.joda.time.DateTime
import Settings.CommonSettings


abstract class Input {
  def toInputTuple (): Product
}

case class UserInput(name: String) extends Input {
  def toInputTuple(): Tuple2[Int, String] =
    (-1, name)
}

case class ProjectInput(name: String, userName: String, startTime: String) extends Input {
  def toInputTuple(): Tuple4[Int, String, String, String] =
    (-1, name, userName, startTime)
}

case class TaskInput(start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String]) extends Input {
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
  def apply (name: String): Option[UserInput] =
    if (name.length <= CommonSettings.maxUserNameLength) {
      Some(UserInput(name))
    } else {
      None
    }
}

object ProjectFactory {
  def apply (name: String, userName: String, startTime: String): Option[ProjectInput] =
    if ((name.length <= CommonSettings.maxProjectNameLength) && (userName.length <= CommonSettings.maxUserNameLength) && CheckISOTimeFormat(startTime)) {
      Some(ProjectInput(name, userName, startTime))
    } else {
      None
    }
}

object TaskFactory {
  def apply (start: String, project: String, time: Int, volume: Option[Int], comment: Option[String]): Option[TaskInput] =
    if ((!comment.isEmpty) && (comment.head.length <= CommonSettings.maxTaskCommentLength)) {
      Some(TaskInput(start, project, time,  volume, comment))
    } else {
      None
    }
}