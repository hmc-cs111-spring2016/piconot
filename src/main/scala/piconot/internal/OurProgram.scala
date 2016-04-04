package piconot.internal

class OurProgram(newRules: List[picolib.semantics.Rule]) {
  var rules = List[picolib.semantics.Rule]()
  def apply(state: OurState): OurProgram = {
    var newRules = this.rules ++ state.rules
    this.rules = newRules
    this
  }
}

object OurProgram {
  var rules = List[picolib.semantics.Rule]()
  def apply(state: OurState): OurProgram = {
    new OurProgram(state.rules)
  }
}