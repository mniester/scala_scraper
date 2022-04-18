import scala.concurrent.Await

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

  test("UserFactory - fail; name too long") {assert (UserFactory(name = "ab" * Settings.maxUserNameLength) == None)}
  test("ProjectFactory - fail; name too long") {assert (ProjectFactory(name = "abc" * Settings.maxProjectNameLength, author = "abc", startTime = "2000-01-01T00:01:01") == None)}
  test("ProjectFactory - fail; user name too long") {assert (ProjectFactory(name = "abc", author = "abc" * Settings.maxProjectNameLength, startTime = "2000-01-01T00:01:01") == None)}
  test("ProjectFactory - fail; datetime not ok") {assert (ProjectFactory(name = "abc" * Settings.maxProjectNameLength, author = "abc", startTime = "2000-13-01T00:01:01") == None)}
  test("TaskFactory - fail; comment too long") {assert (TaskFactory(name = "Test",
                                                        author = "Test",
                                                        startTime = "2000-01-01T00:01:01",
                                                        endTime = "2000-02-01T00:01:01",
                                                        project = "project", time = 1,
                                                        volume = 1, 
                                                        comment = "abc" * Settings.maxTaskCommentLength) == None)}
  
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
                                        val project = ProjectFactory(key = 1, name = "Test", author = "Test", startTime = "2000-01-01T00:01:01").get;
                                        val projectQuery = ProjectQueryByName("Test")
                                        db.addProject(project);
                                        val dbResult = db.getProjectByName(projectQuery).last; 
                                        assert (project == dbResult);
                                        db.delProjectByName(projectQuery);
                                        var dbResult2 = db.getProjectByName(projectQuery);
                                        assert (dbResult2.length == 0);}

/* Upper tests sometimes returns this:

  DB - add, get and remove task *** FAILED ***
  java.lang.IndexOutOfBoundsException: 0 is out of bounds (empty vector)
  at scala.collection.immutable.Vector0$.ioob(Vector.scala:371)
  at scala.collection.immutable.Vector0$.apply(Vector.scala:336)
  at scala.collection.immutable.Vector0$.apply(Vector.scala:334)
  at UnitTests.$anonfun$new$12(Tests.scala:56)
  at org.scalatest.OutcomeOf.outcomeOf(OutcomeOf.scala:85)
  at org.scalatest.OutcomeOf.outcomeOf$(OutcomeOf.scala:83)
  at org.scalatest.OutcomeOf$.outcomeOf(OutcomeOf.scala:104)
  at org.scalatest.Transformer.apply(Transformer.scala:22)
  at org.scalatest.Transformer.apply(Transformer.scala:20)
  at org.scalatest.funsuite.AnyFunSuiteLike$$anon$1.apply(AnyFunSuiteLike.scala:226)*/
  
  test("DB - add, get and remove task") {db.purge;
                                        val task = TaskFactory(key = 1, name = "Test", author = "Test", startTime = "2000-01-01T00:01:01", endTime = "2000-02-01T00:01:01", project = "Test", time = 1, volume = -1, comment = "Test").get;
                                        val taskQuery = TaskQueryByName("Test")
                                        db.addTask(task);
                                        val dbResult = db.getTaskByName(taskQuery)(0);
                                        assert (task == dbResult);
                                        db.delTaskByName(taskQuery);
                                        var dbResult2 = db.getTaskByName(taskQuery);
                                        assert (dbResult2.isEmpty);
                                      }
  
  test("DB - remove Project with Tasks") {db.purge;
                                        val task = TaskFactory(key = 1, name = "Test", author = "Test", startTime = "2000-01-01T00:01:01", endTime = "2000-02-01T00:01:01", project = "Test", time = 1, volume = -1, comment = "Test").get;
                                        val project = ProjectFactory(key = 1, name = "Test", author = "Test", startTime = "2000-01-01T00:01:01").get;
                                        val taskQuery = TaskQueryByName("Test")
                                        val projectQuery = ProjectQueryByName("Test")
                                        db.addTask(task);
                                        db.addProject(project);
                                        db.delProjectByName(projectQuery);
                                        val projectResult = db.getProjectByName(projectQuery);
                                        assert (projectResult.isEmpty);
                                        var TaskResult = db.getTaskByName(taskQuery);
                                        assert (TaskResult.isEmpty);
                                      }
  
}                                      