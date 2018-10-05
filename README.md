# Java-Maze-Solver
This program is a console-based maze solving in Java with BFS, DFS, A*.




Maze structure
======
In this implementation, Mazes consists in a matrix of Squares.

Here is the orthogonal reprensentation of a Maze:
```
o---> X [Lines]
|
v
Y [Columns]
```




Solve mazes
======

Firstly, see how to [load a maze](#load-a-maze) from a .txt file or [create one](#create-a-maze) directly from code.

Next, refer to "[Use a solver](#use-a-solver)" to begin solving when your Maze is all set.

Like any solver, you can also [set your own shift order](#changing-the-shift-order). This program use cardinals as reference and the default one is North-East-South-West.




Load a maze
------
You can load a maze from a .txt as an argument in Maze class constructor, like this:

```Java
Maze myMaze = new Maze("./path/to/my/maze/myMaze.txt");
```
[Example in Main.java line 25][1]

The file must be written within this form :

```
-----------------e-
--xxxxxxxxxxxx-----
-------------x-----
----s--------x-----
-------------x-----
-------------------
-------------------
```
The number of characters in the first line will be the maze number of columns and the number of lines... the number of lines.

Every character is a square of the maze and is read like this:
* `s` : maze starting point
* `e` : maze end point
* `x` : wall
* Other ones (whatever they are) are empty squares.





Create a maze
------
It is also possible to create a maze directly from code by following those steps, but this is the most laborious method.

__1. In Main.java, create a starting and a ending square:__
```Java
Square start = new Square(1, 0, "S");
Square end = new Square(3, 4, "E");
```
Square constructor arguments: `Line (int), Column (int), Identifier (String)`

`S` stands for starting point and `E` for ending point.

__2. Create a new Maze:__
```Java
Maze myMaze = new Maze(6, 5, start, end);
```
Maze constructor arguments : `Number of lines (int), Number of columns (int), Starting point (Square), Ending point (Square)`

__3. Create walls:__

(If you don't want walls, you can skip this step)

Use the `setMazeWall()` method:
```Java
myMaze.setMazeWall(2, 0);
myMaze.setMazeWall(3, 0);
myMaze.setMazeWall(1, 2);
myMaze.setMazeWall(2, 2);
...
```
Arguments: `Wall line pos (int), Wall column pos (int)`





Use a solver
------
You have 3 solvers available, each one corresponding to one algorithm:
* AStarSolver
* BFS_Solver
* DFS_Solver

Simply use
```Java
BFS_Solver bfsSolver = new BFS_Solver(myMaze);
DFS_Solver dfsSolver = new DFS_Solver(myMaze);
AStarSolver aStarSolver = new AStarSolver(myMaze, true);

System.out.println(bfsSolver.solve());
System.out.println(dfsSolver.solve());
System.out.println(aStarSolver.solve());
```

Note that `AStarSolver` takes one other boolean argument to specifies if you want your maze to be solve by A* with Manhattan or Euclidean heuristic.

Set to `true` to use Manhattan heuristic. Euclidean heuristic otherwhise.




Changing the shift order
------
To change the shift order the solvers will use to test squares, use
```Java
char[] order = {'W', 'E', 'N', 'S'};
myMaze.setOrder(order);
```
As the exemple shows it, the order is now West-East-North-South




About the author
======
My name is Gabriel, I'm a french student in Video Games Development in Canada



[1]:https://github.com/Gaderr/Maze_solver/blob/a6ac782c316adee15d8029a12531a7fcd5f659ef/src/Main.java#L25
