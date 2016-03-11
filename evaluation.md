# Evaluation: running commentary

## Internal DSL
_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

Instead of having the keyword 'if' in our syntax, we had to change it to 'If' so that it was not a
Scala keyword

For all of our user defined names we had to change them to strings that could be recognized by
Scala. 

Our indentation and newlines were an immediate problem. We needed a way of indicating, other than
indentation, which parts of the syntax were arguments we wanted to pass to another method. To fix
this issue we tried several approaches. First we added parentheses or brackets at almost every point
in our code. This made the code unreadable and very difficult to follow so we wanted to come up with
a better way. We tried chaining every line together by adding infix operators at each step. We
relized however that a method call (aka infix operator) cannot be on a different line than it's
object. So this would not work to chain all of our lines together unless we added some sort of
method call at the end of every line. We experimented with adding : at the end of every line but
that also looked bad and additionally requiring us to write a `:` method for all of our objects.
This seemed like a bad approach so eventually we just decided to go back to the parentheses.

Another issue we had was that for the rules defined under each task and the conditionals defined
under each rule, we could not evaluate them seperately from their initial state. This meant that we
could not fully evaluate the code inside an indentation without reading what was outside of the
indentation. This proved difficult for translating our syntax into Scala functions because the
'arguments' could not be evaluated first.

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

We would say that our level of change was about an 8. We tried many different methods of linking together our lines of code but didn't find any that satisfied an acceptable level of change for us.

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

It was probably impossible to implement our original syntax in Scala as an internal DSL so a 10 difficulty level.

## External DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**
