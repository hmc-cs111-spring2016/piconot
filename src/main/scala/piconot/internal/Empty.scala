package piconot.internal

import scala.language.postfixOps

object EmptyNot extends Piconot("resources" / "empty.txt") {

  (State(0)(west=x))(go=W)
  (State(0)(west=w))(newState=1)

  (State(1)(north=x))(go=N)
  (State(1)(north=w, south=x))(go=S, newState=2)

  (State(2)(south=x))(go=S)
  (State(2)(east=x, south=w))(go=E, newState=3)

  (State(3)(north=x))(go=N)
  (State(3)(north=w, east=x))(go=E, newState=2)

  run
}
