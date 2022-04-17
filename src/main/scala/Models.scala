package  Models

import org.joda.time.DateTime
import Settings.CommonSettings


abstract class Model {
  def toInputTuple (): Product
}

case class UserModel(key: Int, name: String) extends  Model {
  def toInputTuple(): Tuple2[Int, String] =
    (key, name)
}

case class ProjectModel(key: Int, name: String, userName: String, startTime: String) extends  Model {
  def toInputTuple(): Tuple4[Int, String, String, String] =
    (key, name, userName, startTime)
}

case class TaskModel(key: Int, name: String, start: String, project: String, time: Int,  volume: Int, comment: String) extends  Model {
  def toInputTuple(): Tuple7[Int, String, String, String, Int, Int, String] = 
    (key, name, start, project, time, volume, comment)
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
  def apply (key: Int = -1, name: String): Option[UserModel] = // this fake key is used only in new inputs, because schemas demand any. 
    if ((name.length <= CommonSettings.maxUserNameLength) && (name.length >= CommonSettings.minUserNameLength)) {
      Some(UserModel(key, name))
    } else {
      None
    }
}

object ProjectFactory {
  def apply (key: Int = -1, name: String, userName: String, startTime: String): Option[ProjectModel] =
    if ((name.length <= CommonSettings.maxProjectNameLength) && (userName.length <= CommonSettings.maxUserNameLength) && CheckISOTimeFormat(startTime)) {
      Some(ProjectModel(key, name, userName, startTime))
    } else {
      None
    }
}

object TaskFactory {
  def apply (key: Int = -1, name: String, start: String, project: String, time: Int, volume: Int, comment: String): Option[TaskModel] =
    if ((!comment.isEmpty) && (comment.length <= CommonSettings.maxTaskCommentLength)) {
      Some(TaskModel(key, name, start, project, time, volume, comment))
    } else {
      None
    }
}