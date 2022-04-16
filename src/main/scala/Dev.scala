import DBs.SQLite
import Models.UserFactory
import Queries.UserQueryByName

object Dev extends App {
  //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val db = SQLite
  db.setup()
  db.purge()
}