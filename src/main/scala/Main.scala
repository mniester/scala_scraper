import DBs.SQLite
import slick.jdbc.SQLiteProfile.api._
import Models._
import Queries._
import scala.concurrent.duration.Duration
import scala.concurrent.Await


object Main extends App {
  //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val db = SQLite
  db.setup()
  val y = db.getUserByName(UserQuery("h"))
  println(y)
}