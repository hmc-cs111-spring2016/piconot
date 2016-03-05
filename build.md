# Building and running Piconot

To build Piconot, type `sbt compile` in the top-level directory (the one with
this file in it).

## Running the "empty room program"

Type the following in the top-level directory (the one with this file in it):

```
sbt "run-main piconot.external.Piconot resources/empty.txt src/main/scala/piconot/external/Empty.bot"
```

## Running the "maze program"

Type the following in the top-level directory (the one with this file in it):

```
sbt "run-main piconot.external.Piconot resources/maze.txt src/main/scala/piconot/external/RightHand.bot"
```
