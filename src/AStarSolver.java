import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStarSolver extends Solver
{
	/*
	 * Constructor
	 * m: The maze to solve
	 */
	public AStarSolver(Maze m, Boolean manhattan)
	{
		this.maze = m;
		this.result = "";
		this.manhattan = manhattan; 
		this.frontier = new PriorityQueue<Node<Maze>>(new Comparator<Node<Maze>>()
		{
			public int compare(Node<Maze> s1, Node<Maze> s2) 
		    {
		    	Double cs1 = s1.getContent().getCurrState().getF();
		    	Double cs2 = s2.getContent().getCurrState().getF();
		    	
		    	if(cs1 > cs2)
		    		return 1;
		    	else if(cs1 == cs2)
		    		return 0;
		    	else
		    		return -1;
		    }
		});
		this.closedSquares = new PriorityQueue<Square>(new Comparator<Square>()
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
	}
	
	/*
	 * Solves the maze with the A* algorithm
	 * manhattan: Set as true to use the MANHATTAN DISTANCE instead of EUCLIDEAN DISTANCE 
	 */
	public void solve()
	{
		this.maze.initMaze(); //Re-init maze
		
		Boolean endfound = false;
		this.nodesCounter = 0;
		this.pathLength = 0;
		
		//Compute F value of Starting square
		if(manhattan)
			this.maze.getStart().calcManhattanH();
		else
			this.maze.getStart().calcEuclidH();
		
		this.maze.getStart().calcF();
		
		//Init data structures
		this.frontier.clear(); //Clear frontier Queue
		((PriorityQueue<Node<Maze>>) this.frontier).offer(new Node<Maze>(this.maze)); //Adding the first node (Start node) (G is at 0, Start to Start = 0)
		this.closedSquares.clear(); //Clear closedSquares
		
		//Measure run time
		long startTime = System.currentTimeMillis();
		
		while(!endfound)
		{
			if(this.frontier.isEmpty())
				break;
			
			else
			{
				Node<Maze> current = ((PriorityQueue<Node<Maze>>) this.frontier).remove();
				this.maze = (Maze) current.getContent();
				Square currState = this.maze.getCurrState();
				
				if(currState.getCol() == this.maze.getEnd().getCol() && currState.getLine() == this.maze.getEnd().getLine())
				{
					Node<Maze> temp = new Node<Maze>(this.maze);
					temp.setFather(current);
					((PriorityQueue<Node<Maze>>) this.frontier).offer(temp);
					endfound = true;
				}
				
				else
				{
					LinkedList<Node<Maze>> nexts = this.getNextSquares();
					if(!this.closedSquares.contains(currState))
					{
						this.closedSquares.add(currState);
						currState.setAttribute("*");
					}
					
				    Iterator<Node<Maze>> x = nexts.iterator();
				    
				    while(x.hasNext())
				    {
				    	Node<Maze> neighbor = x.next();
				    	
				    	if(this.closedSquares.contains(neighbor.getContent().getCurrState()))
				    		continue;
				    	else
				    	{
				    		if(!this.frontier.contains(neighbor))
				    		{
				    			neighbor.setFather(current);
				    			((PriorityQueue<Node<Maze>>) this.frontier).offer(neighbor);
				    			this.nodesCounter++;
				    		}
				    	}
				    }
				}
			}
		}
		long endTime = System.currentTimeMillis();
		
		this.setResult(endfound, endTime - startTime);
	}
	
	/*
	 *  Get the next ("walkables") squares from the given square
	 *  c: Square from where to get the nexts squares
	 */
	public LinkedList<Node<Maze>> getNextSquares()
	{
		LinkedList<Node<Maze>> res = new LinkedList<Node<Maze>>();
		
		//Get 4 next squares
		LinkedList<Maze> nexts = this.maze.getCurrState().getNexts();
		
		int gCurrent = this.maze.getCurrState().getG();
		
		for(int i = 0; i < nexts.size(); i++)
		{
			Square tempSq = nexts.get(i).getCurrState();
			if(!this.closedSquares.contains(tempSq))
			{
				if(this.manhattan)
					nexts.get(i).getCurrState().calcManhattanH();
				else
					nexts.get(i).getCurrState().calcEuclidH();
				
				nexts.get(i).getCurrState().incG(gCurrent);
				
				nexts.get(i).getCurrState().calcF();
				
				Node<Maze> tempNode = new Node<Maze>(nexts.get(i));
				res.add(tempNode);
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
		if(this.manhattan)
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
		
		if(success)
		{
			this.maze.resetGrid();
			Node<Maze> revertedTree = ((PriorityQueue<Node<Maze>>) this.frontier).remove();
			
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
		if(this.result == "")
			return "No resolution computed, use the solve method first";
		else
			return this.result;
	}
}
