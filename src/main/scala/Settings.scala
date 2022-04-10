package Settings

import os._
import com.typesafe.config.{Config, ConfigFactory}
import java.io.File

object CommonSettings {
    val source: Config = ConfigFactory.parseFile(new File(s"${os.pwd}/src/resources/application.conf"))
    val maxUserNameLength = source.getConfig("maxlen").getInt("maxUserNameLength")
    val maxProjectNameLength = source.getConfig("maxlen").getInt("maxProjectNameLength")
    val maxTaskCommentLength = source.getConfig("maxlen").getInt("maxTaskCommentLength")
}