package piconot.internal

import scala.language.postfixOps

object RightHand extends Piconot("resources" / "maze.txt") {

  (State(0)(east=x))(go=E, newState=1)
  (State(0)(north=x, east=w))(go=N)
  (State(0)(north=w, east=w))(newState=3)

  (State(1)(south=x))(go=S, newState=3)
  (State(1)(east=x, south=w))(go=E)
  (State(1)(east=w, south=w))(newState=2)

  (State(2)(north=x))(go=N, newState=0)
  (State(2)(north=w, west=x))(go=W)
  (State(2)(north=w, west=w))(newState=1)

  (State(3)(west=x))(go=W, newState=2)
  (State(3)(west=w, south=x))(go=S)
  (State(3)(west=w, south=w))(newState=0)

  run
}
