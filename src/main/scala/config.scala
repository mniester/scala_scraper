package config

object Config:
  var interval: Int = _

  def setInterval(nr: Int): Unit =
    var interval = nr.toInt.abs * 1000
