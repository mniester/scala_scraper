package Settings

import scala.concurrent.duration.Duration
import java.io.File
import com.typesafe.config.{Config, ConfigFactory}

import os._

object CommonSettings {
  val source: Config = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
  val maxUserNameLength = source.getConfig("maxlen").getInt("maxUserNameLength")
  val maxProjectNameLength = source.getConfig("maxlen").getInt("maxProjectNameLength")
  val maxTaskCommentLength = source.getConfig("maxlen").getInt("maxTaskCommentLength")
  val JWTKey = source.getConfig("secrets").getString("JWTKey")
  val dbWaitingDuration = Duration(source.getConfig("wait").getInt("db.quantity"), source.getConfig("wait").getString("db.unit"))
}