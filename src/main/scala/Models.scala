package  Models

import org.joda.time.DateTime
import Settings.CommonSettings


abstract class Model {
  def toInputTuple (): Product
}

case class UserModel(name: String) extends  Model {
  def toInputTuple(): Tuple2[Int, String] =
    (-1, name)
}

case class ProjectModel(name: String, userName: String, startTime: String) extends  Model {
  def toInputTuple(): Tuple4[Int, String, String, String] =
    (-1, name, userName, startTime)
}

case class TaskModel(name: String, start: String, project: String, time: Int,  volume: Option[Int], comment: Option[String]) extends  Model {
  def toInputTuple(): Tuple7[Int, String, String, String, Int, Option[Int], Option[String]] = 
    (-1, name, start, project, time, volume, comment)
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
  def apply (name: String): Option[UserModel] =
    if (name.length <= CommonSettings.maxUserNameLength) {
      Some(UserModel(name))
    } else {
      None
    }
}

object ProjectFactory {
  def apply (name: String, userName: String, startTime: String): Option[ProjectModel] =
    if ((name.length <= CommonSettings.maxProjectNameLength) && (userName.length <= CommonSettings.maxUserNameLength) && CheckISOTimeFormat(startTime)) {
      Some(ProjectModel(name, userName, startTime))
    } else {
      None
    }
}

object TaskFactory {
  def apply (name: String, start: String, project: String, time: Int, volume: Option[Int], comment: Option[String]): Option[TaskModel] =
    if ((!comment.isEmpty) && (comment.head.length <= CommonSettings.maxTaskCommentLength)) {
      Some(TaskModel(name,start, project, time,  volume, comment))
    } else {
      None
    }
}