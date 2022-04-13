import DBs.SQLite
import slick.jdbc.SQLiteProfile.api._
import Inputs._


object Main extends App {
  val db = SQLite
  db.setup()
  val x = UserInput("h").toInputTuple
  db.addUser(UserInput("h"))
}