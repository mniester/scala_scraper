package config

object Config:
  val maxLineLength = 120
  var interval: Int = _

  def setInterval(nr: Int): Unit =
    this.interval = nr.toInt.abs * 1000
