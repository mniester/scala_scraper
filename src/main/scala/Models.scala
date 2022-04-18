package  Models

import java.time.LocalDateTime

import Settings._
import Pencilcase._


abstract class Model {
  def toInputTuple (): Product
}

case class UserModel(key: Int, name: String) extends  Model {
  def toInputTuple(): Tuple2[Int, String] =
    (key, name)
}

case class ProjectModel(key: Int, name: String, userName: String, startTime: LocalDateTime, deleteTime: String = "") extends Model {
  def toInputTuple(): Tuple5[Int, String, String, String, String] =
    (key, name, userName, startTime.toString(), deleteTime)
}

case class TaskModel(key: Int, name: String, author: String, startTime: LocalDateTime, endTime: LocalDateTime, project: String, time: Int,  volume: Int, comment: String, deleteTime: String) extends  Model {
  def toInputTuple(): Tuple10[Int, String, String, String, String, String, Int, Int, String, String] = 
    (key, name, author, startTime.toString(), endTime.toString(), project, time, volume, comment, deleteTime)
  
  def checkLocalTimeDateOverlap (otherTask: TaskModel): Boolean =
    !Pencilcase.isEarlier(this.endTime, otherTask.startTime)
}