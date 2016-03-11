package piconot.external

//import scala.tools.nsc.EvalLoop

object Application extends App {
    val source = scala.io.Source.fromFile("syntax.txt")
    val lines = try source.mkString finally source.close()


    PicobotParser(lines) match {
        case PicobotParser.Success(t, _) ⇒ println(t)
        case e: PicobotParser.NoSuccess  ⇒ println(e)
    }
}