import java.lang.Math;

public class Square
{
	private int l;
	private int c;
	private String attribute;
	private boolean wall;
	
	private double g;
	private double h;
	private double f;
	
	/*
	 * Constructor for a "walkable" Square in the maze
	 * l: Line position
	 * c: Column position
	 * a: Square state
	 * 		-> S = Start position
	 * 		-> E = End position
	 * 		-> [Space] = Playable
	 * 		-> * = Closed (when the solving algorithms run through the maze)
	 * 
	 * -- Note: The wall attribute will be set as false because it can't be a wall.
	 */
	public Square(int l, int c, String a)
	{
		this.l = l;
		this.c = c;
		this.attribute = a; //S = Start; E = End; ' ' = Playable; * = Closed
		this.wall = false;
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
	public void calcManhattanH(Square end) 
	{
		if(end.getAttribute() == "E")
			this.h = Math.abs( this.getLine() - end.getLine() ) 
					 + Math.abs( this.getCol() - end.getCol() );
	}
	
	/*
	 * Computes the H value with the Euclidean distance
	 * end: The ending Square in the maze
	 */
	public void calcEuclidH(Square end)
	{
		if(end.getAttribute() == "E")
			this.h = Math.sqrt(
						Math.pow( this.getLine() - end.getLine(), 2 ) 
						+ Math.pow( this.getCol() - end.getCol(), 2 )
					);
	}
	
	//----------------------
	// G Value
	//----------------------

	/*
	 * Returns G value
	 */
	public double getG() 
	{
		return g;
	}
	
	/*
	 * Computes the G value with the Manhattan distance
	 * start: The starting Square in the maze
	 */
	public void calcManhattanG(Square start)
	{
		if(start.getAttribute() == "S")
			this.g = Math.abs( this.getLine() - start.getLine() ) 
					 + Math.abs( this.getCol() - start.getCol() );
	}
	
	/*
	 * Computes the G value with the Euclidean distance
	 * start: The starting Square in the maze
	 */
	public void calcEuclidG(Square start)
	{
		if(start.getAttribute() == "S")
			this.g = Math.sqrt( 
						Math.pow( this.getLine() - start.getLine(), 2 ) 
						+ Math.pow( this.getCol() - start.getCol(), 2 ) 
					);
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
