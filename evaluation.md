# Evaluation: running commentary

## Internal DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

Rather than having tags followed by colons and then the appropriate information (e.g. `North: x` in the ideal syntax I described means that north is open for that rule), for the most part colons were replaced by `=`. Since my syntax was partly inspired by HTML in the first place, I decided this wasn't a huge deviation. More importantly, the reason I changed this is that in the implementation the "tags" are actually just named parameters of a function. By implementing the DSL this way
I had to do literally nothing besides write the function (which was simple, too) in order to let the user put north, south, east, and west in whichever order they wish. Implementing tags as named parameters also let me easily get the default behavior I wanted by just specifying default values. Current state was an exception, where I used an apply method to construct a state object since this was easier to implement and still fairly easy to understand.


**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

Something like a 4. The main principle is very much still there. State and surroundings still must come in the first half and in that order, and the individual parts of the surroundings can come in whatever order or not at all. Movement direction and state change must come in the second half, and can also be left out. Mostly there are just a lot more parentheses than before, so it doesn't really look that similar at first.

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

Not very difficult, at least for the modified version of the syntax, maybe a 2. On a basic level, my syntax is sort of the same as the original picobot and therefore the API, in that it specifies state, surroundings, movement, and state change in more or less the same terms. The main difference is the exact phrasing. When coming up with a syntax I had considered making a syntax that specifies movement in terms of left, right, forward, and backward, and a syntax like that would
probably have mapped to the API with considerably more difficulty.

## External DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

I got rid of the colons because I decided they looked pretty obnoxious. I also decided that indicating "blocked" with `+` instead of `w` was less confusing since `w` also means "west" but "+" doesn't. I certainly could've left the syntax alone if I had chosen to.

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

I would put it at a 1 for certain. I didn't actually _have_ to make any changes at all, although I chose to in a couple of places.

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

Something like a 6 or 7. It wasn't so much that the API wasn't compatible but rather that, where in the internal DSL all I had to do was pass `RelativeDescription`s as named parameters to a function, I had to do a _lot_ to allow those to be given in more than just the NEWS order.
