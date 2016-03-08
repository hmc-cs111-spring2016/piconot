package piconot.internal

import java.io.File
import scalafx.application.JFXApp

import picolib.maze.Maze
import picolib.semantics._

class OurSurroundings(newSurr: Surroundings) {
    var picoSurr =  newSurr
    def N(surr: OurSurroundings) = {
      this.picoSurr = Surroundings(Blocked, picoSurr.east, picoSurr.west, picoSurr.south)
      this
    }
    def E(surr: OurSurroundings) = {
      this.picoSurr = Surroundings(picoSurr.north, Blocked, picoSurr.west, picoSurr.south)
      this
    }
    def W(surr: OurSurroundings) = {
      this.picoSurr = Surroundings(picoSurr.north, picoSurr.east, Blocked, picoSurr.south)
      this
    }
    def S(surr: OurSurroundings) = {
      this.picoSurr = Surroundings(picoSurr.north, picoSurr.east, picoSurr.west, Blocked)
      this
    }
    def notN(surr: OurSurroundings) = {
      this.picoSurr = Surroundings(Open, picoSurr.east, picoSurr.west, picoSurr.south)
      this
    }
    def notE(surr: OurSurroundings) = {
      this.picoSurr = Surroundings(picoSurr.north, Open, picoSurr.west, picoSurr.south)
      this
    }
    def notW(surr: OurSurroundings) = {
      this.picoSurr = Surroundings(picoSurr.north, picoSurr.east, Open, picoSurr.south)
      this
    }
    def notS(surr: OurSurroundings) = {
      this.picoSurr = Surroundings(picoSurr.north, picoSurr.east, picoSurr.west, Open)
      this
    }
}

object OurSurroundings {
    def * = new OurSurroundings(Surroundings(Anything, Anything, Anything, Anything))
    def N(surr: OurSurroundings) = new OurSurroundings(Surroundings(Blocked, Anything, Anything, Anything))
    def E(surr: OurSurroundings) = new OurSurroundings(Surroundings(Anything, Blocked, Anything, Anything))
    def W(surr: OurSurroundings) = new OurSurroundings(Surroundings(Anything, Anything, Blocked, Anything))
    def S(surr: OurSurroundings) = new OurSurroundings(Surroundings(Anything, Anything, Anything, Blocked))
    def notN(surr: OurSurroundings) = new OurSurroundings(Surroundings(Open, Anything, Anything, Anything))
    def notE(surr: OurSurroundings) = new OurSurroundings(Surroundings(Anything, Open, Anything, Anything))
    def notW(surr: OurSurroundings) = new OurSurroundings(Surroundings(Anything, Anything, Open, Anything))
    def notS(surr: OurSurroundings) = new OurSurroundings(Surroundings(Anything, Anything, Anything, Open))
}

