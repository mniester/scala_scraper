package  Models

import java.time.LocalDateTime
import Settings.CommonSettings


abstract class Model {
  def toInputTuple (): Product
}

case class UserModel(key: Int, name: String) extends  Model {
  def toInputTuple(): Tuple2[Int, String] =
    (key, name)
}

case class ProjectModel(key: Int, name: String, userName: String, startTime: LocalDateTime, deleteTime: String = "") extends  Model {
  def toInputTuple(): Tuple5[Int, String, String, String, String] =
    (key, name, userName, startTime.toString(), deleteTime)
}

case class TaskModel(key: Int, name: String, author: String, startTime: LocalDateTime, endTime: LocalDateTime, project: String, time: Int,  volume: Int, comment: String, deleteTime: String) extends  Model {
  def toInputTuple(): Tuple10[Int, String, String, String, String, String, Int, Int, String, String] = 
    (key, name, author, startTime.toString(), endTime.toString(), project, time, volume, comment, deleteTime)
}


object CheckISOTimeFormat {
  def apply (string: String): Boolean =
    try {
      LocalDateTime.parse(string)
      true
    }
    catch {
      case _: Throwable => false
    }
}

object UserFactory {
  def apply (key: Int = -1, name: String): Option[UserModel] = // this fake key is used only in new inputs, because schemas demand any. 
    if ((name.length <= CommonSettings.maxUserNameLength) && (name.length >= CommonSettings.minUserNameLength)) {
      Some(UserModel(key, name))
    } else {
      None
    }
}

object ProjectFactory {
  def apply (key: Int = -1, name: String, author: String, startTime: String,  deleteTime: String = ""): Option[ProjectModel] =
    if ((name.length <= CommonSettings.maxProjectNameLength) && (author.length <= CommonSettings.maxUserNameLength) && CheckISOTimeFormat(startTime)) {
      Some(ProjectModel(key, name, author, LocalDateTime.parse(startTime), deleteTime))
    } else {
      None
    }
}

object TaskFactory {
  def apply (key: Int = -1, name: String, author: String, startTime: String, endTime: String, project: String, time: Int, volume: Int = -1, comment: String = "", deleteTime: String = ""): Option[TaskModel] =
    if (comment.length <= CommonSettings.maxTaskCommentLength) {
      Some(TaskModel(key, name, author, LocalDateTime.parse(startTime), LocalDateTime.parse(endTime), project, time, volume, comment, deleteTime))
    } else {
      None
    }
}