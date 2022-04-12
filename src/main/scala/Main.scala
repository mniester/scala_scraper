import DBs.SQLite
import slick.dbio._


object Main extends App {
  val db = SQLite
  val x = db.setup()
  //db.api.run(db.users += (1, "Beata"))
}
