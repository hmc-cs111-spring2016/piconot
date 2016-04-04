package piconot.internal

object RightHand extends API("resources" / "maze.txt") {
  // State 0: moving north
  state("0") +
  ("","→")  -> ("→","1") +
  ("→","↑") -> ("↑","0") +
  ("↑→","") -> (".","3")
  
  // State 1: moving east
  state("1") +
  ("","↓")  -> ("↓","3") +
  ("↓","→") -> ("→","1") +
  ("→↓","") -> (".","2")
  
  // State 2: moving west
  state("2") +
  ("","↑")  -> ("↑","0") +
  ("↑","←") -> ("←","2") +
  ("↑←","") -> (".","1")
  
  // State 3: moving south
  state("3") +
  ("","←")  -> ("←","2") +
  ("←","↓") -> ("↓","3") +
  ("←↓","") -> (".","0")
  
  run
}