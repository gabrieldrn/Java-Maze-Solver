import java.lang.Math;
import java.util.LinkedList;

public class Square
{
	private Maze maze;
	private int l;
	private int c;
	private String attribute;
	private boolean wall;
	public int depth;
	
	private int g;
	private double h;
	private double f;
	
	/*
	 * Constructor for a "walkable" Square in the maze
	 * l: Line position
	 * c: Column position
	 * a: Square state
	 * 		-> S = Start position
	 * 		-> E = End position
	 * 		-> [Space] = Open
	 * 		-> * = Closed (when the solving algorithms run through the maze)
	 * 		-> o = Current position
	 * 
	 * -- Note: The wall attribute will be set as false because it can't be a wall.
	 */
	public Square(int l, int c, String a)
	{
		this.l = l;
		this.c = c;
		this.attribute = a; //S = Start; E = End; ' ' = Playable; * = Closed
		this.wall = false;
		this.depth = 0;
		this.g = 0;
		this.h = 0;
		this.f = 0;
	}
	
	/*
	 * Constructor for a wall square in the maze
	 * l: Line poisition
	 * c: Column position
	 * w: Wall state
	 * 		-> Set as true to define the square as a wall
	 */
	public Square(int l, int c, boolean w)
	{
		this.l = l;
		this.c = c;
		this.attribute = null;
		this.wall = w;
		this.g = 0;
		this.h = 0;
		this.f = 0;
	}
	
	public void assignMaze(Maze m)
	{
		this.maze = m;
	}

	/*
	 * Returns the line position of the square in the maze
	 */
	public int getLine() 
	{
		return l;
	}

	/*
	 * Sets the line position of the square in the maze
	 * l: Line position
	 */
	public void setLine(int l) 
	{
		this.l = l;
	}

	/*
	 * Returns the column position of the square in the maze
	 */
	public int getCol() 
	{
		return c;
	}

	/*
	 * Sets the column position of the square in the maze
	 * c: Column position
	 */
	public void setCol(int c) 
	{
		this.c = c;
	}
	
	/*
	 * 
	 * c: The origin Square from where to get the next squares
	 */
	public LinkedList<Maze> getNexts()
	{
		LinkedList<Maze> nexts = new LinkedList<Maze>();
		
		for(int i = 0; i < 4; i++)
		{
			Maze tempMaze = this.maze.clone();
			
			if(this.maze.order[i] == 'N')
			{
				if(this.getNorth() != null && !this.getNorth().isWall())
				{
					tempMaze.setNextState(this.getNorth());
					nexts.push(tempMaze);
				}
			}
			else if (this.maze.order[i] == 'E')
			{
				if(this.getEast() != null && !this.getEast().isWall())
				{
					tempMaze.setNextState(this.getEast());
					nexts.push(tempMaze);
				}
			}
			else if (this.maze.order[i] == 'S')
			{
				if(this.getSouth() != null && !this.getSouth().isWall())
				{
					tempMaze.setNextState(this.getSouth());
					nexts.push(tempMaze);
				}
			}
			else if (this.maze.order[i] == 'W')
			{
				if(this.getWest() != null && !this.getWest().isWall())
				{
					tempMaze.setNextState(this.getWest());
					nexts.push(tempMaze);
				}
			}
		}
		return nexts;
	}
	
	/*
	 * Returns the Square at North from the given Square
	 * c: The origin Square from where to get the North Square
	 */
	public Square getNorth()
	{
		if(this.l - 1 < 0)
			return null;
		else
			return this.maze.getGrid()[this.l - 1][this.c];
	}
	
	/*
	 * Returns the Square at West from the given Square
	 * c: The origin Square from where to get the West Square
	 */
	public Square getWest()
	{
		if(this.c - 1 < 0)
			return null;
		else
			return this.maze.getGrid()[this.l][this.c - 1];
	}
	
	/*
	 * Returns the Square at South from the given Square
	 * c: The origin Square from where to get the South Square
	 */
	public Square getSouth()
	{
		if(this.l + 1 == this.maze.lMax)
			return null;
		else
			return this.maze.getGrid()[this.l + 1][this.c];
	}
	
	/*
	 * Returns the Square at East from the given Square
	 * c: The origin Square from where to get the East Square
	 */
	public Square getEast()
	{
		if(this.c + 1 == this.maze.cMax)
			return null;
		else
			return this.maze.getGrid()[this.l][this.c + 1];
	}

	/*
	 * Returns the square attribute
	 * If the square is a wall, it returns null
	 */
	public String getAttribute() 
	{
		return attribute;
	}

	/*
	 * Sets the square attribute
	 * If the attribute given is not correct, it changes nothing
	 */
	public void setAttribute(String attribute) 
	{
		if(attribute == " " || attribute == "S" || attribute == "E" || attribute == "*")
		{
			this.attribute = attribute;
			this.wall = false;
		}
	}

	/*
	 * Returns wall attribute
	 */
	public boolean isWall()
	{
		return this.wall;
	}

	/*
	 * Sets the square as a wall
	 * 		Also sets the square attribute as null
	 */
	public void setWall()
	{
		this.wall = true;
		this.attribute = null;
	}
	
	//----------------------
	// H Value
	//----------------------
	
	/*
	 * Returns H value
	 */
	public double getH()
	{
		return this.h;
	}

	/*
	 * Computes the H value with the Manhattan distance
	 * end: The ending Square in the maze
	 */
	public void calcManhattanH() 
	{
		this.h = Math.abs( this.getLine() - this.maze.getEnd().getLine() ) 
				 + Math.abs( this.getCol() - this.maze.getEnd().getCol() );
	}
	
	/*
	 * Computes the H value with the Euclidean distance
	 * end: The ending Square in the maze
	 */
	public void calcEuclidH()
	{
		this.h = Math.sqrt(
					Math.pow( this.getLine() - this.maze.getEnd().getLine(), 2 ) 
					+ Math.pow( this.getCol() - this.maze.getEnd().getCol(), 2 )
				);
	}
	
	//----------------------
	// G Value
	//----------------------

	/*
	 * Returns G value
	 */
	public int getG() 
	{
		return g;
	}
	
	/*
	 * Increases the G value
	 */
	public void incG(int prev)
	{
		this.g = 1 + prev;
	}
	
	//----------------------
	// F Value
	//----------------------
	
	/*
	 * Computes F value by addition of H and G
	 */
	public double calcF()
	{
		this.f = this.g + this.h;
		return this.f;
	}
	
	/*
	 * Returns F value
	 */
	public double getF()
	{
		return this.f;
	}
	
	/*
	 * Returns the Square in a string with the format "[LINE, COLUMN](F)"
	 */
	public String toString()
	{
		return "[" + this.l + ", " + this.c + "](" + this.f + ")";
	}
}
