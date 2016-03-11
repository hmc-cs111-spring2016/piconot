package piconot.external.ir

sealed abstract class Expr

case class Then(l: Expr, r: Expr) extends Expr
case class ThenLoop(l: Expr, r: Expr) extends Expr
case class Go(d: Expr) extends Expr
case class GoUntil(d: Expr, s: Expr) extends Expr
case class ListOfDirections(dl: Expr) extends Expr
case class EmptyDirectionList() extends Expr
case class Cons(d: Expr, dl:Expr) extends Expr
case class SingleDirection(d: Expr) extends Expr
case class Up() extends Expr
case class Left() extends Expr
case class Right() extends Expr
case class Down() extends Expr
case class Back() extends Expr
