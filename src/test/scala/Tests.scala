import org.scalatest.funsuite.AnyFunSuite
import Settings._
import Models._
import Strings._
import DBs.SQLite
import Queries.UserQueryByName

class UnitTests extends AnyFunSuite {
  val db = SQLite
  db.setup()
  test("test ScalaTest") {assert ((true == true) && (false ==  false))}

  test("CheckISOTimeFormat - ok") {assert (CheckISOTimeFormat("2222-02-02T22:22:22"))}
  test("CheckISOTimeFormat - fail; not proper datetime") {assert (!CheckISOTimeFormat("2222-33-02T22:22:22"))}
  test("CheckISOTimeFormat - fail; string is not a datetime") {assert (!CheckISOTimeFormat("abcd"))}

  test("UserFactory - fail; name too long") {assert (UserFactory(name = "ab" * CommonSettings.maxUserNameLength) == None)}
  test("ProjectFactory - fail; name too long") {assert (ProjectFactory(name = "abc" * CommonSettings.maxProjectNameLength, userName = "abc", startTime = "2000-01-01T00:01:01") == None)}
  test("ProjectFactory - fail; user name too long") {assert (ProjectFactory(name = "abc", userName = "abc" * CommonSettings.maxProjectNameLength, startTime = "2000-01-01T00:01:01") == None)}
  test("ProjectFactory - fail; datetime not ok") {assert (ProjectFactory(name = "abc" * CommonSettings.maxProjectNameLength, userName = "abc", startTime = "2000-13-01T00:01:01") == None)}
  test("TaskFactory - fail; comment too long") {assert (TaskFactory(name = "Test",
                                                        start = "2000-01-01T00:01:01", 
                                                        project = "project", time = 1,
                                                        volume = Option(1), 
                                                        comment = Option("abc" * CommonSettings.maxTaskCommentLength)) == None)}
  
  test("DB - add, get and remove user") {val user = UserFactory(name = "Test").get; db.addUser(user);
                                        val getResult = db.getUserByName(UserQueryByName("Test")).last; 
                                        assert (user == getResult)}
}