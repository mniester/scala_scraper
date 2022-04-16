import org.scalatest.funsuite.AnyFunSuite
import Settings._
import Models._
import Strings._
import DBs.SQLite
import Queries._

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
  
  test("DB - add, get and remove user") {db.purge;
                                        val user = UserFactory(key = 1, name = "Test").get;
                                        val userQuery = UserQueryByName("Test")
                                        db.addUser(user);
                                        var dbResult = db.getUserByName(userQuery).last; 
                                        assert (user == dbResult);
                                        db.delUserByName(userQuery);
                                        var dbResult2 = db.getUserByName(userQuery);
                                        assert (dbResult2.length == 0);}
  test("DB - add, get and remove project") {db.purge;
                                        val project = ProjectFactory(key = 1, name = "Test", userName = "Test", startTime = "2000-01-01T00:01:01").get;
                                        val projectQuery = ProjectQueryByName("Test")
                                        db.addProject(project);
                                        val dbResult = db.getProjectByName(projectQuery).last; 
                                        assert (project == dbResult);
                                        db.delProjectByName(projectQuery);
                                        var dbResult2 = db.getProjectByName(projectQuery);
                                        assert (dbResult2.length == 0);}
  test("DB - add, get and remove task") {db.purge;
                                        val task = TaskFactory(key = 1, name = "Test", start = "2000-01-01T00:01:01", project = "abc", time = 1, volume = Option(1), comment = Option("abc")).get;
                                        val taskQuery = TaskQueryByName("Test")
                                        db.addTask(task);
                                        val dbResult = db.getTaskByName(taskQuery).last; 
                                        assert (task == dbResult);
                                        db.delTaskByName(taskQuery);
                                        var dbResult2 = db.getTaskByName(taskQuery);
                                        assert (dbResult2.length == 0);}
}                                      