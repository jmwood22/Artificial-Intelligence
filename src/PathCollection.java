import java.util.Stack;


public class PathCollection implements java.io.Serializable
{
	Stack<Path> col;
	
	public PathCollection()
	{
		col = new Stack<Path>();
	}
	
	public void add(Path p)
	{
		col.push(p);
	}
	
	public Path get()
	{
		return col.pop();
	}
	
	public int size()
	{
		return col.size();
	}
}
