import java.util.Iterator;
import java.util.LinkedList;

public class BFS_Solver
{
	private Maze maze;
	private String result;
	private LinkedList<Node<Maze>> frontier;
	private LinkedList<Square> closedSquares;
	private int nodesCounter;
	private int pathLength;
	
	/*
	 * Constructor
	 * m: Maze to solve
	 */
	public BFS_Solver(Maze m)
	{
		this.maze = m;
		this.result = "";
		this.frontier = new LinkedList<Node<Maze>>();
		this.closedSquares = new LinkedList<Square>();
	}
	
	/*
	 * Solves the maze with the Breadth First Search algorithm
	 */
	public void solve()
	{
		Boolean endfound = false;
		this.nodesCounter = 0;
		this.pathLength = 0;
		
		//Init maze
		this.closedSquares.clear();
		this.maze.initMaze();
		
		//Init frontier
		this.frontier.clear();
		this.frontier.add(new Node<Maze>(this.maze)); //Add initial state
		
		//Measure run time
		long startTime = System.currentTimeMillis();
		
		//Search
		while(!endfound)
		{
			if(this.frontier.isEmpty()) //Check if the frontier is empty
				break;
			
			else
			{
				Node<Maze> current = this.frontier.removeFirst(); //Get first node from the frontier
				this.maze = (Maze) current.getContent(); //Get maze from the node
				Square currState = this.maze.getCurrState(); //Get current state from the maze
				
				//System.out.println(this.maze.printMaze());
				
				if(currState.getLine() == this.maze.getEnd().getLine() && currState.getCol() == this.maze.getEnd().getCol())
				{
					Node<Maze> temp = new Node<Maze>(this.maze);
					temp.setFather(current); //Set current as father for all next states
					this.frontier.add(temp);
					endfound = true;
				}
				
				else
				{
					LinkedList<Node<Maze>> nexts = this.getNextSquares(); //Get next possible states
					if(!this.closedSquares.contains(currState))
					{
						this.closedSquares.add(currState);
						currState.setAttribute("*");
					}
					
					Iterator<Node<Maze>> x = nexts.iterator();
					
					//Set fathers
					while(x.hasNext())
					{
						Node<Maze> temp = x.next();
						temp.setFather(current); //Set current as father for all next states
						this.frontier.add(temp);
						this.nodesCounter++;
					}
				}
				
				//System.out.println(this.frontier.toString());
			}
		}
		
		long endTime = System.currentTimeMillis();
		
		this.setResult(endfound, (endTime - startTime));
	}
	
	/*
	 * Get the next ("walkables") states from the given state
	 * c: Square from where to get the nexts squares
	 */
	public LinkedList<Node<Maze>> getNextSquares()
	{
		LinkedList<Node<Maze>> res = new LinkedList<Node<Maze>>();
		
		//Get 4 next squares
		LinkedList<Maze> nexts = this.maze.getCurrState().getNexts();
		
		for(int i = 0; i < nexts.size(); i++)
		{
			Square tempSq = nexts.get(i).getCurrState();
			if(!this.closedSquares.contains(tempSq))
			{
				//this.closedSquares.add(tempSq);
				//this.maze.getGrid()[tempSq.getLine()][tempSq.getCol()].setAttribute("*");
				Node<Maze> tempNode = new Node<Maze>(nexts.get(i));
				res.add(tempNode); //Add the state
			}
		}
		
		return res;
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
		this.result = "    ____                      ____  __       _______           __     _____                      __  \r\n" + 
					"   / __ )________  ____ _____/ / /_/ /_     / ____(_)_________/ /_   / ___/___  ____ ___________/ /_ \r\n" + 
					"  / __  / ___/ _ \\/ __ `/ __  / __/ __ \\   / /_  / / ___/ ___/ __/   \\__ \\/ _ \\/ __ `/ ___/ ___/ __ \\\r\n" + 
					" / /_/ / /  /  __/ /_/ / /_/ / /_/ / / /  / __/ / / /  (__  ) /_    ___/ /  __/ /_/ / /  / /__/ / / /\r\n" + 
					"/_____/_/   \\___/\\__,_/\\__,_/\\__/_/ /_/  /_/   /_/_/  /____/\\__/   /____/\\___/\\__,_/_/   \\___/_/ /_/ \n";
		
		if(success)
		{
			this.maze.resetGrid();
			Node<Maze> revertedTree = this.frontier.removeLast();
			
			revertedTree = revertedTree.getFather();
			this.result += "Path: " + this.maze.getEnd().toString() + "(End) <- ";
			this.pathLength++;
			
			while(revertedTree.hasFather())
			{
				Maze temp = revertedTree.getContent();
				Square state = temp.getCurrState();
				
				if(!state.equals(this.maze.getEnd()))
				{
					this.result += state.toString() + " <- ";
					this.maze.getGrid()[state.getLine()][state.getCol()].setAttribute("*");
					this.pathLength++;
				}
				revertedTree = revertedTree.getFather();
			}
			
			this.result += this.maze.getStart().toString() + "(Start) \n" + "Path length: " + this.pathLength + "\nNumber of nodes created: " + this.nodesCounter + "\nExecution time: " + time/1000d + " seconds\n";
			this.result += this.maze.printMaze();
		}
		else
		{
			this.result += "Failed : Unable to go further and/or end is unreachable.";
		}
	}
	
	/*
	 * Returns the result from the last solving
	 */
	public String getResult()
	{
		if(result == "")
			return "No resolution computed, please use BFS_Solver.solve() first";
		else
			return this.result;
	}
	
	/*
	 * Returns all the closed nodes in a string
	 */
	public String printClosedNodes()
	{
		String res = "Closed nodes : \n";
		for(int i = 0; i < this.closedSquares.size(); i++)
			res += "(" + i + ") " + this.closedSquares.get(i).toString() + "\n";
		
		return res;
	}
	
	/*
	 * Returns the frontier from the last solving
	 */
	public LinkedList<Node<Maze>> getFrontier() 
	{
		return this.frontier;
	}
}
