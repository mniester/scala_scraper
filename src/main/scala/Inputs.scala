package Inputs

import Config._



case class User(name: String)

case class Project(name: String, author: String, time: String)

case class Task(start: String, projec: String, time: Int,  volume: Option[Int], comment: Option[String])



object UserFactory {
  def apply (name: String): Option[User] =
    if (name.length <= CommonSetings.maxUserName) {
      Some(User(name))
    } else {
      None
    }
}