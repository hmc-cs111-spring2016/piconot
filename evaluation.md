# Evaluation: running commentary

## Internal DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

The only change we really had to make was the addition of some parentheses to our statements. This was necessary because we couldn't figure out how to tell scala to give one operator higher precedence than another, so it was simply looking at things from left-to-right which was not always what we wanted.

We should also say that we don't know if we would have had to make more changes because we didn't completely finish implementing the internal DSL.

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

2.

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

7-8. Our DSL was very semantically different from the existing API, because we did not have a strong notion of states within our syntax. Because of this, we had to make file after file in order to provide wrappers for pieces of the provided API that had the functionality that we needed (for instance, we needed to make a wrapper for lists of rules that would be able to store a list of rules, and merge or combine with other lists of rules with modifications in a certain way).

## External DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**
