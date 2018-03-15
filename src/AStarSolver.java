import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarSolver
{
	private Maze maze;
	private String result;
	private Queue<Square> openNodes;
	private Queue<Node<Square>> dynTreeNodes;
	private int nodesCounter;
	private int pathLength;
	
	/*
	 * Constructor
	 * m: The maze to solve
	 */
	public AStarSolver(Maze m)
	{
		this.maze = m;
		this.result = "";
		this.openNodes = new PriorityQueue<Square>(new Comparator<Square>()
		{
			public int compare(Square s1, Square s2) 
		    {
		    	Double cs1 = s1.getF();
		    	Double cs2 = s2.getF();
		    	
		    	if(cs1 > cs2)
		    		return 1;
		    	else if(cs1 == cs2)
		    		return 0;
		    	else
		    		return -1;
		    }
		});
		this.dynTreeNodes = new PriorityQueue<Node<Square>>(new Comparator<Node<Square>>()
		{
			public int compare(Node<Square> s1, Node<Square> s2) 
		    {
		    	Double cs1 = s1.getContent().getF();
		    	Double cs2 = s2.getContent().getF();
		    	
		    	if(cs1 > cs2)
		    		return 1;
		    	else if(cs1 == cs2)
		    		return 0;
		    	else
		    		return -1;
		    }
		});
	}
	
	/*
	 * Solves the maze with the A* algorithm
	 * manhattan: Set as true to use the MANHATTAN DISTANCE instead of EUCLIDEAN DISTANCE 
	 */
	public void solve(boolean manhattan)
	{
		this.maze.initGrid(); //Re-init maze
		
		Boolean endfound = false;
		this.nodesCounter = 0;
		this.pathLength = 0;
		
		//Compute F value of Starting square
		if(manhattan)
			this.maze.getStart().calcManhattanH(this.maze.getEnd());
		else
			this.maze.getStart().calcEuclidH(this.maze.getEnd());
		
		this.maze.getStart().calcF();
		
		//Init data structures
		this.openNodes.clear(); //Clear openNodes Queue
		this.openNodes.offer(maze.getStart()); //Adding the first node (Start node) (G is at 0, Start to Start = 0)
		this.maze.closedNodes.clear(); //Clear closedNodes
		
		//Init Tree nodes
		this.dynTreeNodes.clear();
		this.dynTreeNodes.offer(new Node<Square>(this.maze.getStart()));
		Node<Square> revertedTree = null;
		
		//Measure run time
		long startTime = System.currentTimeMillis();
		
		while(!endfound)
		{
			if(this.openNodes.isEmpty())
				break;
			
			else
			{
				Square current = this.openNodes.remove();
				
				if(current.getCol() == this.maze.getEnd().getCol() && current.getLine() == this.maze.getEnd().getLine())
					endfound = true;
				
				else
				{
					revertedTree = this.dynTreeNodes.remove();
					
					LinkedList<Square> nexts = this.getNextSquares(current, manhattan);
					this.maze.closedNodes.add(current);
					
				    Iterator<Square> it = nexts.iterator();
				    while(it.hasNext())
				    {
				    	Square neighbor = it.next();
				    	
				    	if(this.maze.closedNodes.contains(neighbor))
				    		continue; //Ignore if already evaluated
				    	else
				    	{
				    		if(!this.openNodes.contains(neighbor))
				    		{
				    			this.openNodes.offer(neighbor);
				    			this.nodesCounter++;
					    		
					    		Node<Square> temp = new Node<Square>(neighbor);
					    		temp.setFather(revertedTree);
					    		this.dynTreeNodes.offer(temp);
				    		}
				    	}
				    }
				}
			}
		}
		long endTime = System.currentTimeMillis();
		
		this.setResult(endfound, manhattan, endTime - startTime);
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
	private void setResult(boolean success, boolean manhattan, long time)
	{
		if(this.maze.unicodeIsTheNewBlack())
		{
			if(manhattan)
				this.result = "    ___                    __  ___            __          __  __            \r\n" + 
						"   /   | __/|_            /  |/  /___ _____  / /_  ____ _/ /_/ /_____ _____ \r\n" + 
						"  / /| ||    /  ______   / /|_/ / __ `/ __ \\/ __ \\/ __ `/ __/ __/ __ `/ __ \\\r\n" + 
						" / ___ /_ __|  /_____/  / /  / / /_/ / / / / / / / /_/ / /_/ /_/ /_/ / / / /\r\n" + 
						"/_/  |_||/             /_/  /_/\\__,_/_/ /_/_/ /_/\\__,_/\\__/\\__/\\__,_/_/ /_/ \n";
			else
				this.result = "    ___                    ______           ___     __\r\n" + 
						"   /   | __/|_            / ____/_  _______/ (_)___/ /\r\n" + 
						"  / /| ||    /  ______   / __/ / / / / ___/ / / __  / \r\n" + 
						" / ___ /_ __|  /_____/  / /___/ /_/ / /__/ / / /_/ /  \r\n" + 
						"/_/  |_||/             /_____/\\__,_/\\___/_/_/\\__,_/   \n";
		}
		else
		{
			this.result = "/*********************/\nA* ALGORITHM";
			if(manhattan)
				this.result += " - MANHATTAN DISTANCE\n";
			else
				this.result += " - EUCLIDEAN DISTANCE\n";
		}
		
		if(success)
		{
			Node<Square> revertedTree = this.dynTreeNodes.remove();
			this.maze.initGrid();
			
			this.result += "Path: " + this.maze.getEnd().toString() + "(End) <- ";
			
			revertedTree = revertedTree.getFather();
			this.pathLength++;
			
			while(revertedTree.hasFather())
			{
				if(!revertedTree.getContent().equals(this.maze.getEnd()))
				{
					result += revertedTree.getContent().toString() + " <- ";
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
	 *  Get the next ("walkables") squares from the given square
	 *  c: Square from where to get the nexts squares
	 *  manhattan: If True, the distances computed will use the MANHATTAN DISTANCE instead of EUCLIDEAN DISTANCE
	 */
	public LinkedList<Square> getNextSquares(Square c, Boolean manhattan)
	{
		LinkedList<Square> res = new LinkedList<Square>();
		
		//Get 4 next squares
		Square[] nextsquares = this.maze.getNexts(c);
		
		Square start = this.maze.getStart();
		Square end = this.maze.getEnd();
		
		for(Square s : nextsquares)
		{
			if(s != null && !s.isWall()) //Check if the square at next position is not null and if it's not a wall
			{
				//Calc G / H / F
				if(manhattan)
				{
					//Manhattan
					s.calcManhattanG(start);
					s.calcManhattanH(end);
				}
				else
				{
					//Euclid
					s.calcEuclidG(start);
					s.calcEuclidH(end);
				}
				
		    	s.calcF(); //Calc F value
		    	
				res.add(s); //Add the square
			}
		}
		return res;
	}

	/*
	 * Returns the result from the last solving
	 */
	public String getResult() 
	{
		if(this.result == "")
			return "No resolution computed, use the solve method first";
		else
			return this.result;
	}
}
