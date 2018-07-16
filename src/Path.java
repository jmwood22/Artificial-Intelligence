import java.util.ArrayList;


public class Path implements java.io.Serializable
{
	ArrayList<NumbrixMove> p;
	
	public Path()
	{
		p = new ArrayList<NumbrixMove>();
	}
	
	public Path(Path t)
	{
		p = new ArrayList<NumbrixMove>();
		for(int i = 0; i < t.size(); i++)
		{
			p.add(t.get(i));
		}
	}
	
	public void add(NumbrixMove m)
	{
		p.add(m);
	}
	
	public void remove(int i)
	{
		p.remove(i);
	}
	
	public NumbrixMove get(int i)
	{
		return p.get(i);
	}
	
	public int size()
	{
		return p.size();
	}

	public boolean contains(NumbrixMove move)
	{
		boolean cont = false;
		
		for(int j = 0; j < p.size(); j++)
		{
			//System.out.println(p.get(j));
			if(p.get(j).getRow() == move.getRow() && p.get(j).getCol() == move.getCol() && p.get(j).getVal() == move.getVal())
			{
				cont = true;
			}
		}
		
		//System.out.println("Check for : " + move + " " + cont);
		
		return cont;
	}
}
