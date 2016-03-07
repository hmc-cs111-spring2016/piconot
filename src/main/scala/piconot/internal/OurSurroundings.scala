package piconot.internal

import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._

abstract class OurSurroundings {
    def N(surr: OurSurroundings = noSurroundings) = North(surr)
    def W(surr: OurSurroundings = noSurroundings) = West(surr)
    def S(surr: OurSurroundings = noSurroundings) = South(surr)
    def E(surr: OurSurroundings = noSurroundings) = East(surr)
    def ~(surr: OurSurroundings) = Not(surr)
}


case class North(val surr: OurSurroundings ) extends OurSurroundings
case class West(val surr: OurSurroundings ) extends OurSurroundings
case class South(val surr: OurSurroundings ) extends OurSurroundings
case class East(val surr: OurSurroundings ) extends OurSurroundings
case class Not(val surr: OurSurroundings ) extends OurSurroundings

object noSurroundings extends OurSurroundings
