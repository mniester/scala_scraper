package Inputs

import Config._



case class User(name: String)

case class Project(name: String, userName: String, startTime: String)

case class Task(start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String])


object CheckISOTimeFormat {
  def apply (string: String): Boolean =
    ???
}

object UserFactory {
  def apply (name: String): Option[User] =
    if (name.length <= CommonSetings.maxUserNameLength) {
      Some(User(name))
    } else {
      None
    }
}

object ProjectFactory {
  def apply (name: String, userName: String, startTime: String): Option[Project] =
    if ((name.length <= CommonSetings.maxProjectNameLength) || (userName.length <= CommonSetings.maxUserNameLength) || CheckISOTimeFormat(startTime)) {
      Some(Project(name, userName, startTime))
    } else {
      None
    }
}

object TaskFactory {
  def apply (start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String]): Option[Task] =
    if ((!comment.isEmpty) && (comment.head.length <= CommonSetings.maxTaskCommentLength)) {
      Some(Task(start, project, time,  volume, comment))
    } else {
      None
    }
}