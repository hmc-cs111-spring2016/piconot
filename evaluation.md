# Evaluation: running commentary

## Internal DSL

**Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change.**

First, we changed "def" to "state", because def is a reserved keyword in scala, so we couldn't use it.

Next, we got rid of colons after the state name. We tried to make these into a function, but we decided to just use the apply function.

We also had to change the "while" and "if" keywords to "loop" and "cond", since these were also reserved.

We then realized that scala need a TON of parentheses in order to figure out what was going on.... so our code looks like lisp.

Next, we wrapped the individual states into instances of an `OurState` class so that they could be processed properly in our syntax. We did the same thing for all of the states in an `OurProgram` wrapper.

We were having trouble because both the Surroudings and the move function had the same symbol (i.e. `N`) but different types. We then simplified the move function argument to be a `MoveDirection`, which was already built in.

Saving the best for last, we made some heavy modifications to `OurSurroudings`. Nothing was working, so we opted for quantity that we knew would work versus the optimal solution. We ran into trouble overriding the `~` operator so we changed it to `not` at the beginning of a direction function. We defined a default Surroundings object using `*`, which we then passed in to represent all directions not mentioned in an instance of `OurSurroundings`.

Somehow, we still did not learn how to properly type the word "surroudnings."


**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

9, we feel as though we made many changes from the original in "example-ideal.txt"

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

7, due to the amount of time that it took us to map the syntax to the provided API. Once we modified our syntax to be closer to Scala syntax, it got exponentially easier. suroudirngns.


## External DSL

**Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change.**

We ended up implementing our ideal syntax, with the exception of integers in place of strings for state numbers. We decided that it would be easier to keep track of which values corresponded to which states if we used integers instead of strings. Other than that, it was manageable to implement our ideal syntax as an external DSL. 


**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

0.5, due to the string to integer trade off that we explained above.

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

4, since we had difficulty parsing lists with arbitrary numbers of elements. We also eventually realized that we should be parsing an empty object as the empty string instead of a space. 
