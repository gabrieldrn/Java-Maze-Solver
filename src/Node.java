public class Node<T>
{
	private T value;
	private Node<T> father;
	
	/*
	 * Constructor
	 * c: Content to bind to the Node
	 */
	public Node(T c)
	{
		this.value = c;
		this.father = null;
	}

	/*
	 * Returns the content binded to the Node
	 */
	public T getContent() 
	{
		return value;
	}

	/*
	 * Sets the content given to the Node
	 * value: The content to bind
	 */
	public void setContent(T value) 
	{
		this.value = value;
	}

	/*
	 * Returns the father Node from this Node
	 */
	public Node<T> getFather() 
	{
		return father;
	}

	/*
	 * Sets the the father of this Node
	 * father: The father to set
	 */
	public void setFather(Node<T> father) 
	{
		this.father = father;
	}
	
	/*
	 * Returns true if this Node have a father
	 */
	public boolean hasFather()
	{
		return this.father != null;
	}
	
	/*
	 * Returns the link father -> son in a String
	 */
	public String printFather()
	{
		return this.value.toString() + " <- " + this.father.value.toString(); 
	}
	
	/*
	 * Returns the toString() method of the content binded to this Node
	 */
	public String toString()
	{
		return this.getContent().toString();
	}
}
