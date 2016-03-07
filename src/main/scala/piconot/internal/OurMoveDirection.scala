package piconot.internal

abstract class OurMoveDirection {
  def move(dir: OurMoveDirection) = N
}

case class N() extends OurMoveDirection
case class E() extends OurMoveDirection
case class W() extends OurMoveDirection
case class S() extends OurMoveDirection