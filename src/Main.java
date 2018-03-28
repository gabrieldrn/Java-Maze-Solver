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

		Maze lab2 = new Maze("./data/lab.txt");
		
		/*char[] order = {'N', 'S', 'W', 'E'};
		lab2.setOrder(order);*/
		
		//System.out.println(lab2.toString());
		
		lab2.fancyness(true);
		
		BFS_Solver b2 = new BFS_Solver(lab2);
		//DFS_Solver d2 = new DFS_Solver(lab2);
		//AStarSolver a2 = new AStarSolver(lab2);
		
		b2.solve();
		//d2.solve();
		//a2.solve(true);
		
		System.out.println(b2.getResult());
		//System.out.println(d2.getResult());
		//System.out.println(a2.getResult());
		
		//a2.solve(false);
		//System.out.println(a2.getResult());
	}
}
