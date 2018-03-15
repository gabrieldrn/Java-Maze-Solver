import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Maze 
{
	private Square[][] grid;
	private Square start;
	private Square end;
	private int lMax;
	private int cMax;
	private char[] order = {'N', 'E', 'S', 'W'}; //Shift order in the grid during solving in cardinals
	LinkedList<Square> closedNodes = new LinkedList<Square>();
	
	boolean fancyMode;
	
	/*
	 * Constructor with no file
	 * lRange: Number of lines in the maze
	 * cRange: Number of columns in the maze
	 * start: The starting square of the maze
	 * end: the ending square of the maze
	 */
	public Maze(int lRange, int cRange, Square start, Square end)
	{
		//Init grid
		this.grid = new Square[lRange][cRange];
		for(int i = 0; i < lRange; i++)
		{
			for(int j = 0; j < cRange; j++)
			{
				this.getGrid()[i][j] = new Square(i,j," ");
			}
		}
		
		//Set max values
		this.lMax = lRange;
		this.cMax = cRange;
		
		//Create start and end MazeState objects contaning the start and end squares (stated)
		this.setEnd(end);
		this.setStart(start);
		//At this point, the grid is and stay the inital Maze unsolved.
	}
	
	/*
	 * Constructor with file
	 * file: .txt file from where to import the maze
	 */
	public Maze(String filepath) throws IOException
	{
		try 
		{
			BufferedReader file = new BufferedReader(new FileReader(filepath));
			String line = file.readLine();
			String buffer = line.toUpperCase();
			int count = 0;
			
			//Get columns range
			this.cMax = line.length();
			this.lMax = 1;
			
			//Get lines range
			while((line = file.readLine()) != null)
			{
				this.lMax++;
				buffer += line.toUpperCase();
			}
			
			//Init grid
			this.grid = new Square[this.lMax][this.cMax];
			
			for(int i = 0; i < this.lMax; i++)
			{
				for(int j = 0; j < this.cMax; j++)
				{
					switch(buffer.charAt(count++))
					{
						case 'E': this.setEnd(new Square(i, j, "E"));
							break;
							
						case 'S': this.setStart(new Square(i, j, "S"));
							break;
							
						case 'X': this.getGrid()[i][j] = new Square(i, j, true);
							break;
							
						default : this.getGrid()[i][j] = new Square(i, j, " ");
							break;
					}
				}
			}
			
			file.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * Retuns the starting Square
	 */
	public Square getStart() 
	{
		return start;
	}

	/*
	 * Sets the starting Square for the attribute and the grid
	 * start: The square to set as starting square
	 */
	public void setStart(Square start) 
	{
		this.start = start;
		this.getGrid()[start.getLine()][start.getCol()] = start;
	}

	/*
	 * Returns the ending Square
	 */
	public Square getEnd() 
	{
		return end;
	}

	/*
	 * Sets the ending Square for the attribute and the grid
	 * end: The square to set as ending square
	 */
	public void setEnd(Square end) 
	{
		this.end = end;
		this.getGrid()[end.getLine()][end.getCol()] = end;
	}
	
	/*
	 * Sets a wall in the maze
	 * l: Line position of the wall
	 * c: Column position of the wall
	 */
	public void setMazeWall(int l, int c)
	{
		this.getGrid()[l][c].setWall();
	}
	
	/*
	 * Initiates the maze
	 */
	public void initGrid()
	{
		//Init grid
		for(int i = 0; i < this.lMax; i++)
		{
			for(int j = 0; j < this.cMax; j++)
			{
				if(this.getGrid()[i][j].getAttribute() == "*")
					this.getGrid()[i][j].setAttribute(" ");
			}
		}
	}
	
	/*
	 * Sets a new shift order
	 * newOrder: 4 entries char array, each char (in caps) means a shift direction, for example : ['N', 'S', 'W', 'E'] will shifts North -> South -> West -> East.
	 */
	public void setOrder(char[] newOrder)
	{
		if(newOrder.length == 4)
		{
			this.order = newOrder;
		}
	}
	
	/*
	 * Resets the default order North -> East -> South -> West (Clockwise)
	 */
	public void resetOrder()
	{
		char[] no = {'N', 'E', 'S', 'W'}; 
		this.order = no;
	}
	
	/*
	 * Returns a Square array containing all the squares next to the given Square in the maze
	 * c: The origin Square from where to get the next squares
	 */
	public Square[] getNexts(Square c)
	{
		Square[] nexts = new Square[4];
		
		for(int i = 0; i < 4; i++)
		{
			switch(this.order[i])
			{
				case 'N': nexts[i] = this.getNorth(c);
					break;
					
				case 'E': nexts[i] = this.getEast(c);
					break;
					
				case 'S': nexts[i] = this.getSouth(c);
					break;
					
				case 'W': nexts[i] = this.getWest(c);
					break;
			}
		}
		return nexts;
	}
	
	/*
	 * Returns the Square at North from the given Square
	 * c: The origin Square from where to get the North Square
	 */
	public Square getNorth(Square c)
	{
		if(c.getLine() - 1 < 0)
			return null;
		else
			return this.getGrid()[c.getLine() - 1][c.getCol()];
	}
	
	/*
	 * Returns the Square at West from the given Square
	 * c: The origin Square from where to get the West Square
	 */
	public Square getWest(Square c)
	{
		if(c.getCol() - 1 < 0)
			return null;
		else
			return this.getGrid()[c.getLine()][c.getCol() - 1];
	}
	
	/*
	 * Returns the Square at South from the given Square
	 * c: The origin Square from where to get the South Square
	 */
	public Square getSouth(Square c)
	{
		if(c.getLine() + 1 == lMax)
			return null;
		else
			return this.getGrid()[c.getLine() + 1][c.getCol()];
	}
	
	/*
	 * Returns the Square at East from the given Square
	 * c: The origin Square from where to get the East Square
	 */
	public Square getEast(Square c)
	{
		if(c.getCol() + 1 == cMax)
			return null;
		else
			return this.getGrid()[c.getLine()][c.getCol() + 1];
	}
	
	/*
	 * Returns all the closed nodes in a string
	 */
	public String printClosedNodes()
	{
		String res = "Closed nodes : \n";
		for(int i = 0; i < this.closedNodes.size(); i++)
			res += "(" + i + ") " + this.closedNodes.get(i).toString() + "\n";
		
		return res;
	}

	/*
	 * Returns the maze grid
	 */
	public Square[][] getGrid() 
	{
		return grid;
	}
	
	/*
	 * Magical attribute setter...
	 */
	public void fancyness(boolean verity)
	{
		this.fancyMode = verity;
	}
	
	/*
	 * Magical attribute getter...
	 */
	public boolean unicodeIsTheNewBlack()
	{
		return this.fancyMode;
	}

	/*
	 * Get the maze in a unicode box draw format
	 */
	public String toString()
	{
		String res = "   ";
		String res_under = "";
		Square temp = null;
		Square templineunder = null;
		Square tempnextcol = null;
		Square tempdiag = null;
		
		//Columns numbers
		for(int i = 0; i < cMax; i++)
		{
			if(i < 10)
				res += "  " + i + " ";
			else
				res += "  " + i;
		}
		res += "\n   ╔";
		
		//First row : Maze top edge
		for(int i = 1; i < this.cMax; i++)
		{
			temp = this.grid[0][i - 1];
			tempnextcol = this.grid[0][i];
			if(temp.isWall())
				res += "═══╤";
			else
				if(tempnextcol.isWall())
					res += "═══╤";
				else
					res += "════";
		}
		res += "═══╗\n";
		
		//Browse all squares
		// res = the line containing the square states
		// res_under = the graphics under the squares line with the corner unicode characters
		// contatenation of res + res_under at each line
		//Example :
		//		│   │   │   │ <- res
		//		└───┼───┼───┘ <- res_under
		//		    │   │     <- res
		//		    └───┘     <- res_under
		//		etc...
		for(int l = 0; l < this.lMax; l++)
		{
			res_under = "";
			for(int c = 0; c < this.cMax; c++)
			{
				//Get Squares
				temp = this.grid[l][c]; // = A -> Current square
				tempnextcol = this.getEast(temp); // = B -> Square at the right of temp
				templineunder = this.getSouth(temp); // = C -> Square below temp
				if(l < this.lMax - 1 && c < this.cMax - 1)
					tempdiag = this.getEast(templineunder); // = D -> Square in the temp lower right-hand diagonal
				
				if(c == 0) //First colomn of current line l
				{
					if(l < 10)
						res += l + "  ║";
					else
						res += l + " ║";
					
					if(templineunder != null)
					{
						if(temp.isWall() || templineunder.isWall())
							res_under += "   ╟";
						else
							res_under += "   ║";
					}
				}
				
				if(temp.isWall())
				{
					res += "   ";
					res_under += "───";
				}
				else
				{
					res += " " + temp.getAttribute() + " ";
					if(l < this.lMax - 1)
					{
						if(templineunder.isWall())
							res_under += "───";
						else
							res_under += "   ";
					}
				}
				
				//Maze right edge
				if(c == this.cMax - 1)
				{	
					res += "║";
					if(temp != null && templineunder != null)
					{
						if(temp.isWall() || templineunder.isWall())
							res_under += "╢";
						else
							res_under += "║";
					}
				}
				else
				{
					//Squares corners.
					// two cases : wall square or not
					if(temp.isWall())
					{
						res += "│";
						if(templineunder != null && tempdiag != null && tempnextcol != null)
						{
							//"┼" = (B + D).(C + D) -> The most reccurent corner to write
							if((tempnextcol.isWall() || tempdiag.isWall()) && (templineunder.isWall() || tempdiag.isWall()))
								res_under += "┼";
							else
							{		
								if(!templineunder.isWall() && !tempdiag.isWall() && !tempnextcol.isWall()) //Wall on top left only
									res_under += "┘";
								else if(!templineunder.isWall() && !tempdiag.isWall() && tempnextcol.isWall()) // Walls on top
									res_under += "┴";
								else if(templineunder.isWall() && !tempdiag.isWall() && !tempnextcol.isWall()) // Walls on left
									res_under += "┤";
							}
						}
					}
					else
					{
						if(tempnextcol != null)
						{
							if(tempnextcol.isWall())
								res += "│";
							else
								res += " ";
							
							if(templineunder != null && tempdiag != null)
							{
								//"┼" = (C).(D) -> The most reccurent corner to write
								if(templineunder.isWall() && tempnextcol.isWall())
									res_under += "┼";
								else
								{		
									if(!templineunder.isWall() && !tempdiag.isWall() && !tempnextcol.isWall()) //No wall
										res_under += " ";
									else if(!templineunder.isWall() && tempdiag.isWall() && !tempnextcol.isWall()) //Wall on right below
										res_under += "┌";
									else if(templineunder.isWall() && !tempdiag.isWall() && !tempnextcol.isWall()) //Wall on left below
										res_under += "┐";
									else if(!templineunder.isWall() && !tempdiag.isWall() && tempnextcol.isWall()) //Wall on top right
										res_under += "└";
									else if(!templineunder.isWall() && tempdiag.isWall() && tempnextcol.isWall()) //Walls on right
										res_under += "├";
									else if(templineunder.isWall() && tempdiag.isWall() && !tempnextcol.isWall()) //Walls below
										res_under += "┬";
								}
							}
						}
					}
				}
			}//<- for each column
			if(l < this.lMax - 1)
				res += "\n" + res_under + "\n"; //Concatenate res + res_under
			else
			{
				//Maze bottom edge
				res += "\n   ╚";
				for(int i = 1; i < this.cMax; i++)
				{
					temp = this.getGrid()[l][i - 1];
					if(this.getEast(temp).isWall() || temp.isWall())
						res += "═══╧";
					else
						res += "════";
				}
				res += "═══╝\n";
			}
		}
		
		/*//Affichage simple
		String res = "   ";
		for(int i = 0; i < cMax; i++)
		{
			if(i >= 9)
				res += " " + i;
			else
				res += " " + i + " ";
		}
		res += "\n";
		for(int i = 0; i < this.lMax; i++)
		{
			if(i > 9)
				res += i + " ";
			else
				res += i + "  ";
			
			for(int j = 0; j < this.cMax; j++)
			{
				if(this.getGrid()[i][j].getAttribute() != "" && !this.getGrid()[i][j].isWall())
					res += "[" + this.getGrid()[i][j].getAttribute() + "]";
				else
					res += "[■]";
			}
			res += "\n";
		}*/
		return res;
	}
}
