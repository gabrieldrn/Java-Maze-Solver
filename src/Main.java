import java.io.IOException;

public class Main 
{
	public static void main(String[] args) throws IOException 
	{
		Square start1 = new Square(1, 0, "S");
		Square end1 = new Square(3, 4, "E");
		
		Maze lab1 = new Maze(6, 5, start1, end1);
		
		lab1.setMazeWall(2, 0);
		lab1.setMazeWall(3, 0);
		lab1.setMazeWall(1, 2);
		lab1.setMazeWall(2, 2);
		lab1.setMazeWall(3, 2);
		lab1.setMazeWall(4, 2);
		lab1.setMazeWall(4, 3);
		lab1.setMazeWall(1, 4);
		lab1.setMazeWall(2, 4);
		
		//====================
		//====================
		
		Square start2 = new Square(1, 0, "S");
		Square end2 = new Square(3, 5, "E");
		
		Maze lab2 = new Maze("./data/test.txt");
		
		/*Maze lab2 = new Maze(6, 6, start2, end2);
		
		lab2.setMazeWall(2, 0);
		lab2.setMazeWall(3, 0);
		lab2.setMazeWall(1, 2);
		lab2.setMazeWall(2, 2);
		lab2.setMazeWall(3, 2);
		lab2.setMazeWall(4, 2);
		lab2.setMazeWall(4, 3);
		lab2.setMazeWall(1, 4);
		lab2.setMazeWall(2, 4);
		lab2.setMazeWall(0, 0);
		lab2.setMazeWall(2, 0);
		lab2.setMazeWall(3, 0);
		lab2.setMazeWall(4, 0);
		lab2.setMazeWall(5, 0);
		lab2.setMazeWall(0, 5);
		lab2.setMazeWall(1, 5);
		lab2.setMazeWall(2, 5);
		lab2.setMazeWall(4, 5);
		lab2.setMazeWall(5, 5);
		//lab2.setMazeWall(0, 2);
		//lab2.setMazeWall(5, 3);*/
		
		//====================
		//====================
		
		/*char[] order = {'N', 'S', 'W', 'E'};
		lab2.setOrder(order);*/
		
		//System.out.println(lab2.toString());
		
		lab2.fancyness(true);
		
		BFS_Solver b2 = new BFS_Solver(lab2);
		DFS_Solver d2 = new DFS_Solver(lab2);
		AStarSolver a2 = new AStarSolver(lab2);
		
		b2.solve();
		d2.solve();
		a2.solve(true);
		
		System.out.println(b2.getResult());
		System.out.println(d2.getResult());
		System.out.println(a2.getResult());
		
		a2.solve(false);
		System.out.println(a2.getResult());
	}
}
