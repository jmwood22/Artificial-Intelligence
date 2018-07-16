
public class NumbrixMove implements java.io.Serializable
{
	int val,row,col;
	
	public NumbrixMove(String move)
	{
		String[] temp = move.split(" ");
		
		row = Integer.parseInt(temp[0]);
		col = Integer.parseInt(temp[1]);
		val = Integer.parseInt(temp[2]);
	}
	
	public int getVal() { return val; }
	
	public int getRow() { return row-1; }
	
	public int getCol() { return col-1; }
	
	public String toString()
	{
		return val + "\t" + row + "\t" + col;
	}
	
	public void setRow(int r) { row = r;}
	
	public void setCol(int c) { col = c;}
	
}
