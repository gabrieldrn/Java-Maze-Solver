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
	
	public abstract void solve();
	
	public abstract LinkedList<Node<Maze>> getNextSquares();
	
	public abstract String getResult();
}
