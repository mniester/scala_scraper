import DBs.SQLite

object Main extends App {
  //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val db = SQLite
  db.setup()
  println("Hello!")
}