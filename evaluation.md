# Evaluation: running commentary

## Internal DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

   * Needed to add () to the stuff before and after the ->: Needed so Scala could parse the syntax correctly
   * Needed to replace : with +: Scala didn't like using : since it is widely used in the language already
   * Needed to replace => with ->: Scala didn't like using => since it is widely used in the language already.
   * Originally wanted to use | and ->: Needed to switch to + and -> so order of operations was the same


**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

3 -- We needed to add a few extra () and "" to our code to make it work. Additionally, we couldn't use ':' and '=>'
as operators as originally intended because these are a part of the scala language. We then attempted to use "|"
and "->", but this didn't work since Scala has predefined operator order of operations. We were able to get "+" and "->"
to work since "+" and "-" have the same precedence since an operator's priority is based on the first character.


**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

5 -- It was more difficult to do this than it should have been. That being said, most of this was probably due to
unfamility with Scala. After we learned the few hold ups, it was pretty easy.


## External DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

   * Didn't need to make many changes
   * Would have been nice to add more error checking and a GUI to improve unicode input, but this got out of scope

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

2 -- Would have liked to add more error checking and the unicode GUI.


**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

2 -- The hardest part was figuring out how to get the PackratParser to work. Once we figured that out, it was very easy!
