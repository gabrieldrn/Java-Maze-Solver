import java.util.LinkedList;
import java.util.Stack;

public class DFS_Solver
{
	private Maze maze;
	private String result;
	private Stack<Square> frontier;
	private Stack<Node<Square>> dynTreeNodes;
	private int nodesCounter;
	private int pathLength;
	
	/*
	 * Constructor
	 * m: The maze to solve
	 */
	public DFS_Solver(Maze m)
	{
		this.maze = m;
		this.result = "";
		this.frontier = new Stack<Square>();
		this.dynTreeNodes = new Stack<Node<Square>>();
	}
	
	/*
	 * Solves the maze with the Depth First Search algorithm
	 */
	public void solve()
	{
		Boolean endfound = false;
		this.nodesCounter = 0;
		this.pathLength = 0;
		
		//Init maze
		this.maze.closedNodes.clear();
		this.maze.initGrid();
		
		//Create the reverted tree -> First n containing the frist square
		this.dynTreeNodes.add(new Node<Square>(this.maze.getStart()));
		Node<Square> revertedTree = null;
		
		//Init frontier
		this.frontier.clear();
		this.frontier.push(this.maze.getStart()); //Add first state
		
		//Measure run time
		long startTime = System.currentTimeMillis();
		
		//Search
		while(!endfound)
		{
			if(this.frontier.isEmpty())
				break;
			
			else
			{
				Square current = this.frontier.pop(); //Get first node from the frontier
				revertedTree = this.dynTreeNodes.pop();
				
				if(current.getLine() == this.maze.getEnd().getLine() && current.getCol() == this.maze.getEnd().getCol())
				{
					endfound = true;
					Node<Square> temp = new Node<Square>(current);
					temp.setFather(revertedTree);
					this.dynTreeNodes.push(temp);
				}	
				
				else
				{
					LinkedList<Square> nexts = this.getNextSquares(current); //Get next possible states
					
					for(int i = 0; i < nexts.size(); i++)
					{
						Node<Square> temp = new Node<Square>(nexts.get(i));
						temp.setFather(revertedTree);
						this.dynTreeNodes.push(temp);
						this.nodesCounter++;
					}
					
					this.frontier.addAll(nexts); //Add all next squares into the frontier
					
					this.maze.closedNodes.add(current); //Set current square as closed
				}
			}
		}
		
		long endTime = System.currentTimeMillis();
		
		this.setResult(endfound, (endTime - startTime));
	}
	
	/*
	 * Sets the result in this format :
	 * 	- Path trace
	 *  - Path length
	 *  - Number of nodes created
	 *  - The maze with the path written
	 *  
	 *  PRIVATE: This method must be called only at the end of the solve method. Any other call may throw errors.
	 */
	private void setResult(boolean success, long time)
	{
		if(this.maze.unicodeIsTheNewBlack())
		{
			this.result = "    ____             __  __       _______           __     _____                      __  \r\n" + 
					"   / __ \\___  ____  / /_/ /_     / ____(_)_________/ /_   / ___/___  ____ ___________/ /_ \r\n" + 
					"  / / / / _ \\/ __ \\/ __/ __ \\   / /_  / / ___/ ___/ __/   \\__ \\/ _ \\/ __ `/ ___/ ___/ __ \\\r\n" + 
					" / /_/ /  __/ /_/ / /_/ / / /  / __/ / / /  (__  ) /_    ___/ /  __/ /_/ / /  / /__/ / / /\r\n" + 
					"/_____/\\___/ .___/\\__/_/ /_/  /_/   /_/_/  /____/\\__/   /____/\\___/\\__,_/_/   \\___/_/ /_/ \r\n" + 
					"          /_/                                                                             \n";
		}
		else
			this.result = "/*********************/\nDEPTH FIRST SEARCH ALGORITHM\n";
		
		if(success)
		{
			this.maze.initGrid();
			Node<Square> revertedTree = this.dynTreeNodes.pop();
			
			this.result += "Path: " + this.maze.getEnd().toString() + "(End) <- ";
			this.pathLength++;
			
			while(revertedTree.hasFather())
			{
				if(!revertedTree.getContent().equals(this.maze.getEnd()))
				{
					this.result += revertedTree.getContent().toString() + " <- ";
					this.maze.getGrid()[revertedTree.getContent().getLine()][revertedTree.getContent().getCol()].setAttribute("*");
					this.pathLength++;
				}
				revertedTree = revertedTree.getFather();
			}
			
			this.result += this.maze.getStart().toString() + "(Start) \n" + "Path length: " + this.pathLength + "\nNumber of nodes created: " + this.nodesCounter + "\nExecution time: " + time/1000d + " seconds\n";
			this.result += this.maze.toString();
		}
		else
		{
			this.result += "Failed : Unable to go further and/or end is unreachable.";
		}
	}
	
	/*
	 * Get the nexts ("walkables") squares from the given square
	 * c: Square from where to get the nexts squares
	 */
	public LinkedList<Square> getNextSquares(Square c)
	{
		LinkedList<Square> res = new LinkedList<Square>();
		
		//Get 4 next squares
		Square[] nextsquares = this.maze.getNexts(c);
		
		//Reversing for the DFS frontier, because it's a stack
		for(int i = 0; i < nextsquares.length / 2; i++)
		{
			Square temp = nextsquares[i]; 
			nextsquares[i] = nextsquares[nextsquares.length - i - 1]; 
			nextsquares[nextsquares.length - i - 1] = temp; 
		}
		
		for(Square s : nextsquares)
		{
			if(s != null && !s.isWall()) //Check if the square at next position is not null and if it's not a wall
			{
				if(!maze.closedNodes.contains(s) && !this.frontier.contains(s)) //Check if the square isn't already closed AND not already in the frontier
				{
					res.add(s); //Add the square
				}
			}
		}
		
		return res;
	}
	
	/*
	 * Returns the result from the last solving
	 */
	public String getResult()
	{
		if(result == "")
			return "No resolution computed, please use DFS_Solver.solve() first";
		else
			return result;
	}

	/*
	 * Returns the frontier from the last solving
	 */
	public Stack<Square> getFrontier() 
	{
		return this.frontier;
	}
}
