
package Queries

class MyQuery

case class UserQueryByName(name: String) extends MyQuery

case class ProjectQueryByName (name: String) extends MyQuery

case class FullProjectQuery(name: String) extends MyQuery

case class TaskQueryByName(name: String) extends MyQuery

