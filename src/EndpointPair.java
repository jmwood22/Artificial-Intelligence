public class EndpointPair implements java.io.Serializable
{
	int start,end,dist;
	
	public EndpointPair(int s, int e, int d)
	{
		start = s;
		end = e;
		dist = d;
	}
	
	public int getStart()
	{
		return start;
	}
	
	public int getEnd()
	{
		return end;
	}
	
	public int getdist()
	{
		return dist;
	}
	
	public String toString()
	{
		return dist + " " + start + " " + end;
	}
}