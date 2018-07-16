import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Numbrix 
{
	static int dimension;
	static ArrayList<String> setup;
	static NumbrixGame game;
	static BufferedReader prompt = new BufferedReader(new InputStreamReader(System.in));
	static BufferedReader fileReader = null;
	static String path;
	static String temp;
	static int choice;
	static Stack<NumbrixGame> states;
	
	public static void main(String[] args) throws IOException, CloneNotSupportedException, ClassNotFoundException
	{
		
		do
		{
			
		
			while(fileReader == null)
			{
				System.out.println("Give initial game data location and press Enter");
				path = prompt.readLine();
				try
				{
					fileReader = new BufferedReader(new FileReader(path));
				} 
				catch (IOException e) 
				{
					System.out.println("************\nFile does not exist or cannot be opened.\n************\n");
				}
			}
		
			System.out.println("\n" + "File opened succesfully!");
		
			temp = fileReader.readLine().trim();
			dimension = Integer.parseInt(temp);
		
			setup = new ArrayList<String>();
		
			while(fileReader.ready())
			{	
				temp = fileReader.readLine().trim();
				setup.add(temp);
			}
			
			fileReader.close();
			fileReader = null;
		
			System.out.println("Game data read successfully! \nBuilding game");
		
			game = new NumbrixGame(dimension, setup);
		
			game.buildGame();
		
			do
			{
				game.printGrid();
				
				System.out.println("What would you like to do? \n1. Make a move. \n2. Delete a value. \n3. View move history. \n4. View rules. \n5. Let AI play. \n6. Restart. \n7. Quit.");
			
				temp = prompt.readLine().trim();
				try
				{
					choice = Integer.parseInt(temp);
				}
				catch(NumberFormatException n)
				{
					System.out.println("************\nInvalid Selection\n************\n");
				}
				
				if(choice == 1)
				{
					makeMove();
					if(game.completed())
					{
						System.out.println("CONGRATULATIONS!!! Would you like to play again? (y/n)");
						temp = prompt.readLine().trim();
						
						if(!temp.equals("y")&&!temp.equals("n"))
							System.out.println("************\nInvalid Selection\n************\n");
						else if(temp.equals("y"))
							break;
						else if(temp.equals("n"))
							choice = 6;
					}
				}
			
				else if (choice == 2)
				{
					deleteMove();
				}
			
				else if(choice == 3)
				{
					game.moveHistory();				
				}
			
				else if(choice == 4)
				{
					displayRules();
				}
				
				else if(choice == 5)
				{
					startAI();
				}
				
				else if(choice == 6)
				{
					break;
				}
		
			} while(choice != 7);
		} while(choice !=7);
		
		prompt.close();
	}
	
	private static void startAI() throws CloneNotSupportedException, IOException, ClassNotFoundException 
	{
		long startTime = System.currentTimeMillis();
		
		states = new Stack<NumbrixGame>();
		states.push(game);
		PathCollection paths;
		states.peek().buildEndpoints();
		int count = 0;
		while(states.peek().getEndpoints().size() > 0)
		{
			
			if(states.peek().hasTrivial())
			{
				
			}
			else if(states.peek().getEndpointDist().size() == 0)
			{
				Path path;
				//System.out.println("PathCollection size is 0");
				states.pop();
				NumbrixGame revert = states.pop();
				
				while(revert.getCollection().size() == 0)
				{
					revert = states.pop();
				}
				
				path = revert.getCollection().get();
				
				NumbrixGame rclone = revert.deepClone();
				states.push(rclone);
				states.push(revert);
				//System.out.println("Revert size: " + revert.getCollection().size());
				
				revert.executePath(path);
			}
			
			else
			{				
				states.peek().begindfs();
				paths = states.peek().getCollection();
				if(paths.size() == 0)
				{
					Path path;
					//System.out.println("PathCollection size is 0");
					states.pop();
					NumbrixGame revert = states.pop();
					
					while(revert.getCollection().size() == 0)
					{
						revert = states.pop();
					}
					
					path = revert.getCollection().get();
					
					NumbrixGame rclone = revert.deepClone();
					states.push(rclone);
					states.push(revert);
					//System.out.println("Revert size: " + revert.getCollection().size());
					
					revert.executePath(path);
					
				}
				else if(paths.size() > 1)
				{
					NumbrixGame current = states.pop();
					//System.out.println("Current state collection size: " + current.getCollection().size());
					Path path = current.getCollection().get();
					NumbrixGame n = current.deepClone();
					//System.out.println("New state collection size: " + n.getCollection().size());
					
					//System.out.println("Printing state being added");
					//n.printGrid();
					//System.out.println("Path Collection size is: " + paths.size());
					current.executePath(path);
					states.push(n);
					states.push(current);
					//System.out.println("Printing state after exec");
					//n.printGrid();
				}
				else
				{
					//System.out.println("Path Collection size is 1");
					states.peek().executePath(paths.get());
				}
			}
			
			count++;
			//System.out.println("Number of states: " + states.size());
			//states.peek().printGrid();
			states.peek().buildEndpoints();
		}
		
		//System.out.println("Reached max count");
		game = states.peek();
		long endTime = System.currentTimeMillis();
		System.out.println("Time elapsed: " + (endTime-startTime) + " ms");
	}

	private static void makeMove() throws IOException
	{
		System.out.println("Input your desired move in the form \nrow column value \nIf you wish to cancel, press 0");
		temp = prompt.readLine().trim();
		String regex = "^[0-9]+ [0-9]+ [0-9]+$";
		
		if(temp.equals("0"))
			System.out.println();
		else
		{
			if(temp.matches(regex))
			{
				game.addMove(temp);
			}
			
			else
				{
					System.out.println("************\nMove not formatted correctly, please try again. \n************\n");
					makeMove();
				}
		}
		
		choice = 0;
	}
	
	public static void deleteMove() throws IOException
	{
		System.out.println("\nInput your desired deletion in the form\nrow column \nIf you wish to cancel, press 0");
		
		temp = prompt.readLine().trim();
		
		String regex = "^[0-9]+ [0-9]+$";
		
		if(temp.equals("0"))
			System.out.println();
		else
			{
				if(temp.matches(regex))
					game.delete(temp);
				else
				{
					System.out.println("************\nMove not formatted correctly, please try again.\n************\n");
					deleteMove();
				}
			}
	}
	
	public static void displayRules()
	{
		System.out.println("A game starts with various positions filled with numbers. The objective of this game is to place\n" 
				+ "numbers into the open cells so they make a path in numerical order, 1 through the last number.\n" 
				+ "Note: the number 1 and the last number are NOT guaranteed to be on the initial board. You can\n" 
				+ "add numbers horizontally or vertically in any direction. Diagonal paths are not allowed.\n"
				+ "The rows and columns are labeled for you. The rules of this game are courtesy of Dr. Dankel.\n");
	}
}
