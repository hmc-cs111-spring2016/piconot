package piconot

object EmptyBot extends Picobor("resources" / "empty.txt") {
  
  /////////////////////////////////////////////////////////
  // State 0: go West
  /////////////////////////////////////////////////////////

  // as long as West is unblocked, go West
  (0 `**x*`) → W (0)

  // can't go West anymore, so try to go North (by transitioning to State 1)
  (0 `**W*`) → X (1)

  /////////////////////////////////////////////////////////
  // State 1: go North
  /////////////////////////////////////////////////////////

  // as long as North is unblocked, go North
  (1 `x***`) → N (1)
  
  // can't go North any more, so try to go South (by transitioning to State 2)
  (1 `N**x`) → S (2)
  
  /////////////////////////////////////////////////////////
  // States 2 & 3: fill from North to South, West to East
  /////////////////////////////////////////////////////////

  // State 2: fill this column from North to South  
  (2 `***x`) → S (2)
  
  // can't go South anymore, move one column to the East and go North
  // (by transitioning to State 3)
  (2 `*x*S`) → E (3)
  
  // State 3: fill this column from South to North
  (3 `x***`) → N (3)
  
  // can't go North anymore, move one column to the East and go South
  // (by transitioning to State 2)
  (3 `Nx**`) → E (2)  
  
  run
} 


