package Inputs

import org.joda.time.DateTime
import Settings.CommonSettings


case class User(name: String)

case class Project(name: String, userName: String, startTime: String)

case class Task(start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String])


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