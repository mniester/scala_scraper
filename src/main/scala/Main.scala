@main def main(args: String*): Unit = 
  val lang = if (args.length != 2) then "en" else args(0).toLowerCase()