import scala.language.implicitConversions 

package object piconot {
  import java.io.File
  implicit class fileJoiner(prefix: String) {
      def /(postfix: String) = prefix + File.separator + postfix    
  }
}