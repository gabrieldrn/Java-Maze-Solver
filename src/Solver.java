import java.util.AbstractCollection;
import java.util.LinkedList;

public abstract class Solver 
{
	protected Maze maze;
	protected String result;
	protected AbstractCollection<Node<Maze>> frontier;
	protected AbstractCollection<Square> closedSquares;
	protected int nodesCounter;
	protected int pathLength;
	protected Boolean manhattan;
	
	/*
	 * Solves the maze with the related (BFS, DFS, A*) algorithm and
	 * sets the result in this format :
	 * 	- Path trace
	 *  - Path length
	 *  - Number of nodes created
	 *  - The maze with the path written
	 */
	public abstract String solve();
	
	/*
	 * Get the nexts ("walkables") squares from the given square
	 */
	public abstract LinkedList<Node<Maze>> getNextSquares();
	
	/*
	 * Returns the result from the last solving
	 */
	public abstract String getResult();
	
	/*
	 * Returns the frontier from the last solving
	 */
	public abstract AbstractCollection<Node<Maze>> getFrontier();
}
