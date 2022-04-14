
package Queries


class MyQuery

case class UserQuery(name: String) extends MyQuery

case class ProjectQuery (name: String) extends MyQuery

case class FullProjectQuery(name: String) extends MyQuery

case class TaskQuery(name: String) extends MyQuery

