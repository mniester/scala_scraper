import java.time.LocalDateTime
import java.time.Clock

package object Pencilcase { // I use pencilcase file for small snippets, which do not fit to other files and can be useful in many projects
  
  def toBoolean[Int] (nr: Int) =
    if (nr == -1) {false} else {true}

  def isEarlier(alpha: LocalDateTime, beta: LocalDateTime): Boolean =
    !toBoolean(alpha.compareTo(beta))
  
  def isEarlierWithParse(alpha: String, beta: String): Boolean =
    isEarlier(LocalDateTime.parse(alpha), LocalDateTime.parse(beta))
  
  def stringUTCNow (): String =
    Clock.systemUTC().instant().toString()
  
  def stringTimeZonedNow (): String =
    Clock.systemDefaultZone().instant.toString()
}
