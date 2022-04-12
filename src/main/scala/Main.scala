import DBs.SQLite
import slick.jdbc.SQLiteProfile.api._


object Main extends App {
  val db = SQLite
  val x = db.setup()
  //DBIO.seq(db.users += (1, "Beata"))
  db.api.run(db.users += (1, "Beata"))
}
