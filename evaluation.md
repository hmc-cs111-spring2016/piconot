# Evaluation: running commentary

## Internal DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

The first change we made was the addition of some parentheses to our statements. This was necessary because we couldn't figure out how to tell scala to give one operator higher precedence than another, so it was simply looking at things from left-to-right which was not always what we wanted.

The second change was removing the "(search)" tag and "go to search" line, and replacing them with a thenLoop statement and a "go (back)" statement. We couldn't think of any way to implement arbitrary tags for lines without putting the loop tag (like "search") in quotes, which would lose a lot of the DSL-y-ness. We needed thenLoop in order to have access to both lines at once, so that we could keep track of where the "go back" statement would need to jump to.

We would have had to make more changes, most likely, in order to implement maze - we believe we would need either an or, or a not, for surroundings checking. However, in the interest of time, we did not expand our DSL to include these features. However, we don't believe it would be much more difficult to implement or after implementing and.

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

3.

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

7-8. Our DSL was very semantically different from the existing API, because we did not have a strong notion of states within our syntax. Because of this, we had to make file after file in order to provide wrappers for pieces of the provided API that had the functionality that we needed (for instance, we needed to make a wrapper for lists of rules that would be able to store a list of rules, and merge or combine with other lists of rules with modifications in a certain way).

## External DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._
This is essentially one change away from the ideal syntax; this was the second change we made for the internal DSL, of changing our looping strategy to using thenLoop/go back. We made this change because we actually liked the change more than the "ideal," and we wanted some level of consistency between our internal and external DSL.

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**
2: The only main change now is the thenLoop/go back syntax, which we actually think is better than before. As opposed to the internal DSL, the parens are no longer needed.

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**
5. Again, our syntax is quite semantically different. The lack of parentheses made this a bit simpler, but what really simplified it was the code layout in Parser.scala. We had to give types to everything as we went, so we knew exactly what corresponded to each piece of the API. Note that our syntax isn't quite complete - it has the same deficiencies as the internal DSL, and also doesn't work for "open", only for "blocked." It would be fairly easy to implement "open," but we didn't need to to get the empty room program working. 

It would be a bit more challenging to implement statements like "blocked on left and up and open on down" because we overloaded the word "and" to mean two different things, so the parser might potentially run into difficulty. However, none of these features were necessary to get the empty room program working, which we managed to do.
