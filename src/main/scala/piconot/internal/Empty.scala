package piconot.internal

//import piconot.internal.API
import scala.language.postfixOps

object Empty extends API("resources" / "empty.txt") {

  state("0") +
  ("","←") -> ("←","0") +
  ("←","") -> (".","1")
  
  state("1") +
  ("","↑")  -> ("↑","1") +
  ("↑","↓") -> ("↓","2")
  
  state("2") +
  ("","↓")  -> ("↓","2") +
  ("↓","→") -> ("→","3")
  
  state("3") +
  ("","↑")  -> ("↑","3") +
  ("↑","→") -> ("→","2")
  
  run
}