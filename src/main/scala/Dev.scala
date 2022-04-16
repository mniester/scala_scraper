import DBs.SQLite
import Models.UserFactory
import Queries.UserQueryByName

object Dev extends App {
  //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val db = SQLite
  db.setup()
  db.addUser(UserFactory(name = "Abc").get)
  val abc = db.getUserByName(UserQueryByName("Abc"))
  println(abc)
  db.delUserByName(UserQueryByName("Abc"))
  println(db.getUserByName(UserQueryByName("Abc")))
}