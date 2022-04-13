import DBs.SQLite
import slick.jdbc.SQLiteProfile.api._
import Inputs._


object Main extends App {
  val db = SQLite
  db.setup()
  val x = User("f").toInputTuple
  db.cursor.run(db.users += x)
}