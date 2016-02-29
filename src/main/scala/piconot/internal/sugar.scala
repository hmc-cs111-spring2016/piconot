package piconot

import scala.language.implicitConversions 

package object internal {
  import java.io.File
  implicit class fileJoiner(prefix: String) {
      def /(postfix: String) = prefix + File.separator + postfix    
  }
}