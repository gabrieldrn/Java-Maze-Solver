import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class DFS_Solver extends Solver
{
	/*
	 * Constructor
	 * m: The maze to solve
	 */
	public DFS_Solver(Maze m)
	{
		this.maze = m;
		this.result = "";
		this.frontier = new Stack<Node<Maze>>();
		this.closedSquares = new Stack<Square>();
	}
	
	public String solve()
	{
		Boolean endfound = false;
		this.nodesCounter = 0;
		this.pathLength = 0;
		
		//Init maze
		this.closedSquares.clear();
		this.maze.initMaze();
		
		//Init frontier
		this.frontier.clear();
		((Stack<Node<Maze>>) this.frontier).push(new Node<Maze>(this.maze)); //Add first state
		
		//Measure run time
		long startTime = System.currentTimeMillis();
		
		//Search
		while(!endfound)
		{
			if(this.frontier.isEmpty())
				break;
			
			else
			{
				Node<Maze> current = ((Stack<Node<Maze>>) this.frontier).pop(); //Get first node from the frontier
				this.maze = (Maze) current.getContent();
				Square currState = this.maze.getCurrState();
				
				if(currState.getLine() == this.maze.getEnd().getLine() && currState.getCol() == this.maze.getEnd().getCol())
				{
					Node<Maze> temp = new Node<Maze>(this.maze);
					temp.setFather(current);
					((Stack<Node<Maze>>) this.frontier).push(temp);
					endfound = true;
				}	
				
				else
				{
					LinkedList<Node<Maze>> nexts = this.getNextSquares(); //Get next possible states
					if(!this.closedSquares.contains(currState))
					{
						((Stack<Square>) this.closedSquares).push(currState);
						currState.setAttribute("*");
					}
					
					Iterator<Node<Maze>> x = nexts.descendingIterator();
					
					while(x.hasNext())
					{
						Node<Maze> temp = x.next();
						temp.setFather(current);
						((Stack<Node<Maze>>) this.frontier).push(temp);
						this.nodesCounter++;
					}
				}
			}
		}
		
		long endTime = System.currentTimeMillis();
		
		long time = endTime - startTime;
		
		this.result = "    ____             __  __       _______           __     _____                      __  \r\n" + 
				"   / __ \\___  ____  / /_/ /_     / ____(_)_________/ /_   / ___/___  ____ ___________/ /_ \r\n" + 
				"  / / / / _ \\/ __ \\/ __/ __ \\   / /_  / / ___/ ___/ __/   \\__ \\/ _ \\/ __ `/ ___/ ___/ __ \\\r\n" + 
				" / /_/ /  __/ /_/ / /_/ / / /  / __/ / / /  (__  ) /_    ___/ /  __/ /_/ / /  / /__/ / / /\r\n" + 
				"/_____/\\___/ .___/\\__/_/ /_/  /_/   /_/_/  /____/\\__/   /____/\\___/\\__,_/_/   \\___/_/ /_/ \r\n" + 
				"          /_/                                                                             \n";
	
		if(endfound)
		{
			this.maze.initMaze();
			Node<Maze> revertedTree = ((Stack<Node<Maze>>) this.frontier).pop();
			
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
		
		return this.result;
	}
	
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
				Node<Maze> tempNode = new Node<Maze>(nexts.get(i));
				res.add(tempNode);
			}
		}
		
		return res;
	}
	
	public String getResult()
	{
		if(result == "")
			return "No resolution computed, please use DFS_Solver.solve() first";
		else
			return this.result;
	}

	public AbstractCollection<Node<Maze>> getFrontier() 
	{
		return  this.frontier;
	}
}
