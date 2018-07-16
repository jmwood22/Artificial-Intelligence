import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;


public class NumbrixGame implements java.io.Serializable
{	
	int dimension;
	ArrayList<String> setup;
	ArrayList<NumbrixMove> initMoves,playerMoves,multimoves;
	boolean[] played;
	ArrayList<Integer> endpoints;
	ArrayList<EndpointPair> endpointDist;
	NumbrixMove[] moves;
	PathCollection collection;
	
	int[][] grid;
	
	public NumbrixGame(int dim, ArrayList<String> init)
	{
		dimension = dim;
		setup = init;
	}
	
	public NumbrixGame(NumbrixGame g)
	{
		dimension = g.getDimension();
		setup = g.getSetup();
		initMoves = g.InitMoves();
		playerMoves = g.getPlayerMoves();
		multimoves = g.getMultimoves();
		played = g.getPlayed();
		endpoints = g.getEndpoints();
		endpointDist = g.getEndpointDist();
		moves = g.getMoves();
		collection = g.getCollection();
		grid = g.getGrid();
	}
	
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	private int[][] getGrid() 
	{
		return grid;
	}

	public PathCollection getCollection() 
	{
		return collection;
	}

	private NumbrixMove[] getMoves() 
	{	
		return moves;
	}

	public ArrayList<EndpointPair> getEndpointDist() 
	{
		return endpointDist;
	}

	public ArrayList<Integer> getEndpoints() 
	{
		return endpoints;
	}

	private boolean[] getPlayed() 
	{
		return played;
	}

	private ArrayList<NumbrixMove> getMultimoves() 
	{
		return multimoves;
	}

	private ArrayList<NumbrixMove> getPlayerMoves() 
	{
		return playerMoves;
	}

	private ArrayList<NumbrixMove> InitMoves() 
	{
		return initMoves;
	}

	private ArrayList<String> getSetup() 
	{
		return setup;
	}

	private int getDimension() 
	{
		return dimension;
	}

	public void buildGame()
	{
		initializeGrid();
		getInitMoves();
		
		System.out.println("Game State Initialized. \nAdding initial moves.");	
		
		for(int i = 0; i < initMoves.size(); i++)
		{
			grid[initMoves.get(i).getRow()][initMoves.get(i).getCol()] = initMoves.get(i).getVal();
			played[initMoves.get(i).getVal()-1] = true;
			moves[initMoves.get(i).getVal()-1] = new NumbrixMove((initMoves.get(i).getRow()+1) + " " + (initMoves.get(i).getCol()+1) + " " + initMoves.get(i).getVal() );
		}
		
		//printGrid();
	}
	
	private void initializeGrid()
	{
		grid = new int[dimension][dimension];
		played = new boolean[dimension*dimension];
		moves = new NumbrixMove[dimension*dimension];
		//printGrid();
		
		for(int i = 0; i < moves.length; i++)
		{
			moves[i] = new NumbrixMove(0 + " " + 0 + " " + (i+1));
		}
					
	}
	
	private void getInitMoves()
	{
		initMoves = new ArrayList<NumbrixMove>();
		playerMoves = new ArrayList<NumbrixMove>();
		NumbrixMove temp;
		
		for(int i = 0; i < setup.size(); i++)
		{
			temp = new NumbrixMove(setup.get(i));
			initMoves.add(temp);
		}
		
	}
	
	public void printGrid()
	{
		for(int i = dimension-1; i >=0;i--)
		{
			System.out.print((i+1) + "|\t");
			for(int j = 0; j < dimension; j++)
				System.out.print(grid[i][j] + "\t");
			if(i!=0)
				System.out.print("\n\n");
		}
		
		System.out.print("\n\t");
		for(int i = 0; i<dimension; i++)
			System.out.print("_\t");
		
		System.out.println();
		System.out.print("\t");
		for(int i = 1; i <= dimension; i++)
			System.out.print(i + "\t");
		System.out.println("\n\n");
	}

	public boolean addMove(String move) 
	{
		playerMoves.add(new NumbrixMove(move));
		int i = playerMoves.size()-1;
		//System.out.println(move);
		/*for(int j = 0; j < moves.length; j++)
		{
			System.out.println(moves[j]);
		}*/
				
		if(moveValid(playerMoves.get(i)))
		{
			grid[playerMoves.get(i).getRow()][playerMoves.get(i).getCol()] = playerMoves.get(i).getVal();
			played[playerMoves.get(i).getVal()-1] = true;
			moves[playerMoves.get(i).getVal()-1] = new NumbrixMove(move);
			
			return true;
		}
			
		else
		{
			System.out.println("************\nDesired move cannot be made or the desired value has already been used. Please try again.\n************\n");
			return false;
		}
		
		//printGrid();
	}
	
	private boolean moveValid(NumbrixMove move)
	{
		int count = 0;
		int r = move.getRow();
		int c = move.getCol();
		
		if(!inBounds(r,c))
			return false;
		if(grid[r][c]!=0)
			return false;
		if(move.getVal()>dimension*dimension || move.getVal()<=0)
			return false;
		if(played[move.getVal()-1]==true)
			return false;
		
		if(inBounds(r-1,c))
		{
			if(inRange(grid[r-1][c],move.getVal()) || grid[r-1][c]==0)
				count++;
		}
		
		if(inBounds(r,c-1))
		{
			if(inRange(grid[r][c-1],move.getVal()) || grid[r][c-1]==0)
				count++;
		}
		
		if(inBounds(r+1,c))
		{
			if(inRange(grid[r+1][c],move.getVal()) || grid[r+1][c]==0)
				count++;
		}
		
		if(inBounds(r,c+1))
		{
			if(inRange(grid[r][c+1],move.getVal()) || grid[r][c+1]==0)
				count++;
		}
		
		if(move.getVal()==1 || move.getVal()==dimension*dimension)
			return (count>=1);
		else
			return (count>=2);
	}
	
	private boolean inBounds(int val1, int val2)
	{
		return ((val1>=0 && val1<=dimension-1)&&(val2>=0 && val2 <= dimension-1));
	}
	
	private boolean inRange(int val1, int val2)
	{
		return (Math.abs(val1-val2)==1);
	}

	public boolean completed() 
	{
		for(int i = 0; i < played.length; i++)
		{
			if(played[i]==false)
				return false;
		}
		
		return true;
	}

	public void moveHistory() 
	{
		System.out.println("\nMove History" + "\n" + "Val \t Row \t Col");
		for(int i = 0; i < playerMoves.size(); i++)
		{
			System.out.println(playerMoves.get(i).toString());
		}
		System.out.println("\n");
	}

	public void delete(String pair) 
	{
		String[] str = pair.split(" ");
		int r = Integer.parseInt(str[0])-1;
		int c = Integer.parseInt(str[1])-1;
		int v;
		
		if(inBounds(r,c)&&!inSetup(r,c))
		{
			v = grid[r][c];
			if(v == 0)
				System.out.println("************\nThere is no value to be deleted.\n************\n");
			else
				{
					grid[r][c] = 0;
					played[v-1] = false;
				}
			
		}
		
		else
			System.out.println("************\nRequested position is not valid.\n************\n");
		
		//printGrid();
	}

	private boolean inSetup(int r, int c) 
	{
		for(int i = 0; i < initMoves.size(); i++)
		{
			if(initMoves.get(i).getRow() == r && initMoves.get(i).getCol() == c)
				return true;
		}
		
		return false;
	}

	public void buildEndpoints() 
	{
		endpoints = new ArrayList<Integer>();
		
		for(int i = 0; i < played.length; i++)
		{
			if(played[i] == true)
			{
				if(i == 0)
				{
					if(played[i+1] == false && !endpoints.contains(i))
						endpoints.add(i);
				}
				
				else if(i == played.length-1)
				{
					if(played[i-1] == false && !endpoints.contains(i))
						endpoints.add(i);
				}
				
				else if((played[i+1] == false || played[i-1] == false) && !endpoints.contains(i))
					endpoints.add(i);
			}
		}
		
		//System.out.println(endpoints);
		
		endpointDist = new ArrayList<EndpointPair>();
		int end1,end2,dist;
		
		for(int i = 0; i < endpoints.size()-1; i++)
		{
			end1 = endpoints.get(i);
			end2 = endpoints.get(i+1);
			dist = end2-end1;
			if(dist > 1 && played[(end2+end1)/2] == false)
				endpointDist.add(new EndpointPair(end1,end2,dist));
		}
		
		Collections.sort(endpointDist, new CustomComparator());
		/*for(int i = 0; i < moves.length; i++)
		{
			System.out.println(moves[i]);
		}*/
	}

	public void begindfs() 
	{		
		EndpointPair temp = endpointDist.get(0);
		NumbrixMove st = moves[temp.getStart()];
		NumbrixMove en = moves[temp.getEnd()];
		//System.out.println("Start: " + st.getVal() + " End: " + en.getVal());
		collection = dfs(temp.getdist(), st, en);
		
		//return collection;
	}

	public void executePath(Path path) 
	{
		//System.out.println("Execute Begin");
		String temp;
		int base = path.get(0).getVal();
		for(int i = 1; i < path.size()-1; i++)
		{
			temp = (path.get(i).getRow()+1) + " " + (path.get(i).getCol()+1) + " " + (base+i);
			//System.out.println(temp);
			this.addMove(temp);
		}
	}

	private PathCollection dfs(int depth, NumbrixMove st, NumbrixMove en) 
	{
		//System.out.println("Entering dfs");
		
		int row = st.getRow();
		int col = st.getCol();
		PathCollection c = new PathCollection();
		
		Path temp = new Path();
		int i = 0;
		ArrayList<Path> list = new ArrayList<Path>();
		temp.add(st);
		list.add(temp);
		
		while(i < list.size())
		{
			
			
			Path path1 = new Path(list.get(i));
			Path path2 = new Path(list.get(i));
			Path path3 = new Path(list.get(i));
			Path path4 = new Path(list.get(i));
			row = path1.get(path1.size()-1).getRow();
			col = path1.get(path1.size()-1).getCol();
			
			//System.out.println(i );
			/*System.out.println("********Path being evaled**********");
			for(int j = 0; j < path1.size(); j++)
				System.out.println(path1.get(j));
			System.out.println("********End of path****************");*/
			
			/*System.out.println("List");
			for(int k = 0; k < list.size(); k++)
			{
				for(int j = 0; j < list.get(k).size(); j++)
					System.out.println(list.get(k).get(j));
				System.out.println();
			}*/
			
			
			if(path1.size() < depth+1)
			{
				if(inBounds(row+1,col) && ((grid[row+1][col] == 0 && path1.size() < depth) || (grid[row+1][col] == en.getVal() && path1.size() == depth)))
				{
					//System.out.println("row + 1");
					if(!path1.contains(new NumbrixMove((row+2) + " " + (col+1) + " " + (grid[row+1][col]))))
					{
						path1.add(new NumbrixMove((row+2) + " " + (col+1) + " " + (grid[row+1][col])));
						list.add(path1);
					}
					
				}
			
				if(inBounds(row-1,col) && ((grid[row-1][col] == 0 && path2.size() < depth) || (grid[row-1][col] == en.getVal() && path2.size() == depth)))
				{	
					//System.out.println("row - 1");
					if(!path2.contains(new NumbrixMove((row) + " " + (col+1) + " " + (grid[row-1][col]))))
					{
						path2.add(new NumbrixMove((row) + " " + (col+1) + " " + (grid[row-1][col])));
						list.add(path2);
					}
					
					/*for(int j = 0; j < path.size(); j++)
						System.out.println(path.get(j));*/
					
					/*System.out.println("List Before");
					for(int k = 0; k < list.size(); k++)
					{
						for(int j = 0; j < list.get(k).size(); j++)
							System.out.println(list.get(k).get(j));
					}*/
				}
			
				if(inBounds(row,col+1) && ((grid[row][col+1] == 0 && path3.size() < depth) || (grid[row][col+1] == en.getVal() && path3.size() == depth)))
				{
					//System.out.println("col + 1");
					if(!path3.contains(new NumbrixMove((row+1) + " " + (col+2) + " " + (grid[row][col+1]))))
					{
						path3.add(new NumbrixMove((row+1) + " " + (col+2) + " " + (grid[row][col+1])));
						list.add(path3);
					}
					
					/*for(int j = 0; j < path.size(); j++)
						System.out.println(path.get(j));*/
					
					//System.out.println();
					/*for(int k = 0; k < list.size(); k++)
					{
						for(int j = 0; j < list.get(k).size(); j++)
							System.out.println(list.get(k).get(j));
					}*/
				}
				
				if(inBounds(row,col-1) && ((grid[row][col-1] == 0 && path4.size() < depth) || (grid[row][col-1] == en.getVal() && path4.size() == depth)))
				{	
					//System.out.println("col - 1");
					if(!path4.contains(new NumbrixMove((row+1) + " " + (col) + " " + (grid[row][col-1]))))
					{
						path4.add(new NumbrixMove((row+1) + " " + (col) + " " + (grid[row][col-1])));
						list.add(path4);
					}
										
				}
			}
			
			else if(path1.get(path1.size()-1).getVal() == en.getVal())
			{
				/*System.out.println("List Before Add");
				for(int k = 0; k < list.size(); k++)
				{
					for(int j = 0; j < list.get(k).size(); j++)
						System.out.println(list.get(k).get(j));
					System.out.println();
				}*/
				
				//System.out.println("Path Added");
				//for(int j = 0; j < path1.size(); j++)
					//System.out.println(path1.get(j));
				
				c.add(path1);
			}
			/*System.out.println("*******************List for Iteration " + i);
			for(int k = 0; k < list.size(); k++)
			{
				for(int j = 0; j < list.get(k).size(); j++)
					System.out.println(list.get(k).get(j));
				System.out.println();
			}
			System.out.println("********************");*/
			i++;
		}
		
		/*System.out.println("List at End");
		for(int k = 0; k < list.size(); k++)
		{
			for(int j = 0; j < list.get(k).size(); j++)
				System.out.println(list.get(k).get(j));
			System.out.println();
		}*/
		
		//System.out.println("Depth : " + depth);
		return c;
	}

	public NumbrixGame deepClone() throws IOException, ClassNotFoundException 
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		oos.flush();
		oos.close();
		bos.close();
		byte[] byteData = bos.toByteArray();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (NumbrixGame) ois.readObject();
	}

	public boolean hasTrivial() 
	{
		
		for(int i = 0; i < endpoints.size(); i++)
		{
			int row = moves[endpoints.get(i)].getRow();
			int col = moves[endpoints.get(i)].getCol();
			int nrow = 0;
			int ncol = 0;
			int count = 0;
			
			if(inBounds(row+1,col) && grid[row+1][col] == 0)
			{
				nrow = row+1;
				ncol = col;
				count++;
			}
			if(inBounds(row-1,col) && grid[row-1][col] == 0)
			{
				nrow = row-1;
				ncol = col;
				count++;
			}
			if(inBounds(row,col+1) && grid[row][col+1] == 0)
			{
				nrow = row;
				ncol = col+1;
				count++;
			}
			if(inBounds(row,col-1) && grid[row][col-1] == 0)
			{
				nrow = row;
				ncol = col-1;
				count++;
			}
			
			if(count==1)
			{
				int v;
				if(endpoints.get(i) > 0 && played[endpoints.get(i)-1] == false)
					v = endpoints.get(i);
				else
					v = endpoints.get(i)+2;
				return this.addMove((nrow+1) + " " + (ncol+1) + " " + v);
				
			}
			
		}
		return false;
	}

	
}
