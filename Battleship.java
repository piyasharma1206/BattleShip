import java.util.Scanner;
import java.util.Random;

public class Battleship {

	public GameBoard newinstance() {return new GameBoard();}
	private Scanner in;
	
	class GameBoard{
		public String block;
		public char player;
		public boolean occupied;
		public boolean selected;
		public int type;
		
		GameBoard(){
			block = "___|";
			occupied = false;
			selected = false;
			type = 0;
		}
	}
	
	class ShipClass{
		public int type;
		public boolean input; //WHERE THE SHIP IS PLACED
		public boolean destroyed;
		public String location;
		public char orientation;
		
		ShipClass(int i){
			type = i;
			input = false;
			destroyed = false;
			location = "";
			orientation = ' ';
		}
	}
	
	class AIplay{
		public int row;
		public int col;
		public int row2;
		public int col2;
		public char shot;
		public char shot2;
		public int count;
		public boolean error;
		
		AIplay(){
			row = 0;
			col = 0;
			row2 = 0;
			col2 = 0;
			shot = 'M';
			shot2 = 'M';
			count = 0;
			error = false;
		}
	}
	
	private GameBoard[][] playerboard = {{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()}};
		
	private GameBoard[][] enemyboard = {{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()},
										{newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance(),newinstance()}};
	
	private char turn = 'X';
	private boolean  victory = false;
	private int count = 0;
	
	//Pseudo-random generation for Computer (Enemy) 
	private Random generator = new Random(System.currentTimeMillis());
	
	//Calling constructor for ship formation
	private ShipClass p_sub = new ShipClass(1);
	private ShipClass p_destroyer= new ShipClass(2);
	private ShipClass p_battleship = new ShipClass(3);
	private ShipClass e_sub = new ShipClass(1);
	private ShipClass e_destroyer = new ShipClass(2);
	private ShipClass e_battleship = new ShipClass(3);
	private AIplay AI = new AIplay();
	
	//main function
	public static void main(String [] args){
		
		String s;
		Battleship bs = new Battleship();
		bs.in = new Scanner(System.in);
		
		bs.playGame();
		
		System.out.println("Would you like to play another game of Battleship?(Y/N): ");
		s = bs.in.nextLine();
		while(s.charAt(0) == 'Y'){
			bs.initialize();
			bs.playGame();
			s = bs.in.nextLine();
		}
		bs.in.close();
		
	}
	
	public void playGame(){
		
		printBoard();
		placeShips();
		AIPlaceGeneration();
		System.out.println("Enter your moves in the form 'A1'.");
		while(! victory)
		playersTurn();
	}
	
	//print the battleship game on which the game will be played
	public void printBoard(){
		System.out.println("                ENEMY                                 PLAYER");
		
		//columns named alphabetically
		System.out.println("    1   2   3   4   5   6   7   8         1   2   3   4   5   6   7   8");
		
		//rows named alphabetically
		for(int x=0; x<8; x++){
			System.out.print((char)(65+x) + " ");
			
			for(int y=0; y<8; y++){
				System.out.print(enemyboard[x][y].block);
			}
			System.out.print("     ");
			System.out.print((char)(65+x) + " ");
			for(int z=0; z<8; z++){
				System.out.print(playerboard[x][z].block);
			}
			System.out.println();
		}
	}
	
	public void placeShips(){
		System.out.println();
		//instructions on how to place the Ships
		System.out.println("Place your ships on the board");
		System.out.println(" You have 3 ships:  1: 2-spot Submarine, 2: 3-spot Destroyer, 3: 4-spot Battleship");
		System.out.println("1. Select the number of the ship.");
		System.out.println("2. Select the coordinates of the place where you want to place the ship. (Example: A3)");
		System.out.println("3. Select the orientation, H=Horizontal, V=Vertical");
		
		System.out.println("For example, '4 C2 H', and press enter");
		System.out.println("When you have placed all the ships on the board, enter 'P' to play.");
		
		String input = "";
		boolean start = false;
		
		while(!start){
			input = in.nextLine();
			
			
			if(input.length() == 1){ //when the game starts and the player enters 'P'
				if(input.charAt(0) == 'P'){
					if(p_battleship.input && p_destroyer.input && p_sub.input)
						start = true;
					else
						System.out.println("You need to place all of your ships.");
				}
				else{
					System.out.println("Invalid Input. Try Again.");
				}
			}
			else if(input.length() == 6){ //when the player is still placing the ships in the game board
				if(((int)input.charAt(0)-48) >= 4){
					System.out.println("Invalid Ship Type. Try Again.");
				}
				else if(((int)input.charAt(2)-65) >= 8){
					System.out.println("Invalid Row. Try Again.");
				}
				else if(((int)input.charAt(3)-48) >= 9){
					System.out.println("Invalid Column. Try Again.");
				}
				else if((int)input.charAt(5) != 72 && (int)input.charAt(5) != 86){
					System.out.println("Invalid Orientation. Try Again.");
				}
				else{   //Valid Input for orientation
					if((int)input.charAt(5) == 72 && (((int)input.charAt(0)-48) + 1) > (9 - ((int)input.charAt(3)-48))){
						System.out.println("Invalid Horizontal Placement. Not enough room for placing the ship.");
					}
					else if((int)input.charAt(5) == 86 && (((int)input.charAt(0)-48) + 1) > (9 - ((int)input.charAt(2) - 64))){
						System.out.println("Invalid Vertical Placement. Not enough room for placing the ship.");
					}
					else{ //Valid Placement, except for overlap
						if(((int)input.charAt(0)-48) == 1){
							placeGameBoard(input.charAt(5), p_sub, input.substring(2,4));
						}
						else if(((int)input.charAt(0)-48) == 2){
							placeGameBoard(input.charAt(5), p_destroyer, input.substring(2,4));
						}
						else{
							placeGameBoard(input.charAt(5), p_battleship, input.substring(2,4));
						}
					}
				}
			}
			else{
				System.out.println("Invalid Input. Try Again.");
			}
		}
	}
	
public void placeGameBoard(char direction, ShipClass current, String coordinate){
		
		boolean overlap = false;
		
		if(direction == 'H'){
			for(int i=((int)coordinate.charAt(1)-49);i<((int)coordinate.charAt(1)-49)+(current.type + 1);i++){
				if(playerboard[((int)coordinate.charAt(0)-65)][i].type != current.type &&
				   playerboard[((int)coordinate.charAt(0)-65)][i].occupied == true)
					overlap = true;
			}
		}
		else{  //layout == 'V'
			for(int i=((int)coordinate.charAt(0)-65);i<((int)coordinate.charAt(0)-65)+(current.type + 1);i++){
				if(playerboard[i][((int)coordinate.charAt(1)-49)].type != current.type &&
				   playerboard[i][((int)coordinate.charAt(1)-49)].occupied == true)
					overlap = true;
			}
		}
		if(!overlap){ //If ships don't overlap
			if(current.input){
				if(current.orientation == 'H'){
					for(int i=((int)current.location.charAt(1)-49);i<((int)current.location.charAt(1)-49)+(current.type + 1);i++){
						playerboard[((int)current.location.charAt(0)-65)][i].block = "___|";
						playerboard[((int)current.location.charAt(0)-65)][i].occupied = false;
						playerboard[((int)current.location.charAt(0)-65)][i].type = 0;
					}
				}
				else{  //orientation == 'V'
					for(int i=((int)current.location.charAt(0)-65);i<((int)current.location.charAt(0)-65)+(current.type + 1);i++){
						playerboard[i][((int)current.location.charAt(1)-49)].block = "___|";
						playerboard[i][((int)current.location.charAt(1)-49)].occupied = false;
						playerboard[i][((int)current.location.charAt(1)-49)].type = 0;
					}
				}
			}
			if(direction == 'H'){
				for(int i=((int)coordinate.charAt(1)-49);i<((int)coordinate.charAt(1)-49)+(current.type + 1);i++){
					playerboard[((int)coordinate.charAt(0)-65)][i].block = "_@_|";
					playerboard[((int)coordinate.charAt(0)-65)][i].occupied = true;
					playerboard[((int)coordinate.charAt(0)-65)][i].type = current.type;
				}
			}
			else{  //layout == 'V'
				for(int i=((int)coordinate.charAt(0)-65);i<((int)coordinate.charAt(0)-65)+(current.type + 1);i++){
					playerboard[i][((int)coordinate.charAt(1)-49)].block = "_@_|";
					playerboard[i][((int)coordinate.charAt(1)-49)].occupied = true;
					playerboard[i][((int)coordinate.charAt(1)-49)].type = current.type;
				}
			}
			current.input = true;
			current.location = coordinate;
			current.orientation = direction;
			printBoard();
		}
		else
			System.out.println("Invalid Placement. Ships Overlap.");
	}
	
	//computer places the ships in its game board
	public void AIPlaceGeneration(){
		
		boolean flag = false;
		for(int i=1;i<4;i++){
			flag = false;
			while(!flag){
				int random1 = generator.nextInt(2);
				int random2 = generator.nextInt(6);
				int random3 = generator.nextInt(6);
				if(random1 == 0)
					random1 = 72;
				else
					random1 = 86;
				random2 += 65;
				random3 += 49;
				
				if(random1 == 72 && (i + 1) > (9 - (random3-48))){
					//not a good place, need a better location
				}
				else if(random1 == 86 && (i + 1) > (9 - (random2-64))){
					//not a good place, need a better location
				}
				else{ //Valid Placement, except for overlapping condition
					
					String a = String.valueOf((char)random2) + String.valueOf((char)random3);
					
					//LOCATION OF SHIPS PLACED BY COMPUTER (ENEMY)
					//System.out.println("AI LOCATION: " + a + " / " + random2 + " " + random3 + " / " + random1);
					if(i == 1){ //when loop runs first time, ship 1 placed
						flag = AIPlaceGameBoard((char)random1, e_sub, a); 
					}
					else if(i == 2){ //ship 2 placed
						flag = AIPlaceGameBoard((char)random1, e_destroyer, a);
					}
					else{ // ship 3 placed otherwise
						flag = AIPlaceGameBoard((char)random1, e_battleship, a);
					}
				}
			}
		}
	}
	
	
public boolean AIPlaceGameBoard(char direction, ShipClass current, String coordinate){
		
		boolean overlap = false;
		
		if(direction == 'H'){
			for(int i=((int)coordinate.charAt(1)-49);i<((int)coordinate.charAt(1)-49)+(current.type + 1);i++){
				if(enemyboard[((int)coordinate.charAt(0)-65)][i].type != current.type &&
				   enemyboard[((int)coordinate.charAt(0)-65)][i].occupied == true)
					overlap = true;
			}
		}
		else{  //layout == 'V'
			for(int i=((int)coordinate.charAt(0)-65);i<((int)coordinate.charAt(0)-65)+(current.type + 1);i++){
				if(enemyboard[i][((int)coordinate.charAt(1)-49)].type != current.type &&
				   enemyboard[i][((int)coordinate.charAt(1)-49)].occupied == true)
					overlap = true;
			}
		}
		if(!overlap){ //If ships don't overlap
			if(direction == 'H'){
				for(int i=((int)coordinate.charAt(1)-49);i<((int)coordinate.charAt(1)-49)+(current.type + 1);i++){
					enemyboard[((int)coordinate.charAt(0)-65)][i].occupied = true;
					enemyboard[((int)coordinate.charAt(0)-65)][i].type = current.type;
				}
			}
			else{  //layout == 'V'
				for(int i=((int)coordinate.charAt(0)-65);i<((int)coordinate.charAt(0)-65)+(current.type + 1);i++){
					enemyboard[i][((int)coordinate.charAt(1)-49)].occupied = true;
					enemyboard[i][((int)coordinate.charAt(1)-49)].type = current.type;
				}
			}
			current.input = true;
			current.location = coordinate;
			current.orientation = direction;
			return true;
		}
		else
			return false;
	}

    //game starts and players take turn to target the enemy's ships
	public void playersTurn(){
		String nextMove = "";
		String validation = "";

		if(turn == 'X'){
			System.out.println("Enter your move: ");	
			nextMove = in.nextLine();
		}
		else
			nextMove = turnOfAI();
		
		//if input of coordinates is wrong
		validation = checkPlayerMove(nextMove);
		while(validation != "okay"){
			if(turn == 'X'){
				System.out.println("ERROR: "+ validation);
				nextMove = in.nextLine();
			}
			else
				nextMove = turnOfAI(); 
			validation = checkPlayerMove(nextMove);
		}
		
		processMove(nextMove);
		
		if(turn == 'O')
			printBoard();
		
		if(count >= 17)
			checkVictory();
		
		if(turn == 'X')
			turn = 'O';
		else
			turn = 'X';
	}
	
	//Validation for correct input/move
	public String checkPlayerMove(String m){
		
		if(((int)m.charAt(0)-65) >= 8)
			return "Invalid Row. Row can only be from A-H. Enter another move: ";	
		if(((int)m.charAt(1)-48) >= 9)
			return "Invalid Column. Column can only be from 1-8. Enter another move: ";
		if(turn == 'X'){
			if(enemyboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].selected)  
				return "Already chosen location. Enter another move: ";
		}
		else{
			if(playerboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].selected){
		        AI.error = true;
		        AI.shot = 'M';
				return "Bad AI, How could you not guess it correct! :O ";
			}
		}
		
		return "okay";
	}
	
	
	public void processMove(String m){
		
		count++;
		//shot == hit or miss 
		char shot = ' ';
		String message = "";
		
		if(turn == 'X'){
			if(enemyboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].occupied){   
				shot = 'X';
				message = "Hit!";
			}
			else{
				shot = 'o';
				message = "Miss!";
			}
			// enters current value of shot
			enemyboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].block = "_"+shot+"_|";
			enemyboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].selected = true;
		}
		else{
			if(playerboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].occupied){   
				shot = 'X';
				message = "Hit!";
				AI.shot2 = AI.shot;
				AI.shot = 'H';
				AI.count++;
			}
			else{
				shot = 'o';
				message = "Miss!";
				AI.shot2 = AI.shot;
				AI.shot = 'M';
				AI.count++;
			}
			playerboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].block = "_"+shot+"_|";
			playerboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].selected = true;
		}
		
		int counter = 0;
		if(shot == 'X'){
			if(turn == 'X'){
				int ship = enemyboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].type;
				
				for(int x=0;x<8;x++){
					for(int y=0;y<8;y++){
						if(enemyboard[x][y].selected && enemyboard[x][y].type == ship)
							counter++;
					}
				}
				if(counter == (ship + 1)){ //valid only when all the coordinates are crossed by the enemy
					String shiptype = "";
					if(ship == 1){
						shiptype = "Submarine!";
						e_sub.destroyed = true;
					}
					else if(ship == 2){
						shiptype = "Destroyer!";
						e_destroyer.destroyed = true;
					}
					else{
						shiptype = "Battleship!";
						e_battleship.destroyed = true;
					}
					message += " You have destroyed the " + shiptype;
				}
			}
			else{
				int ship = playerboard[((int)m.charAt(0)-65)][((int)m.charAt(1)-49)].type;
				
				for(int x=0;x<8;x++){
					for(int y=0;y<8;y++){
						if(playerboard[x][y].selected && playerboard[x][y].type == ship)
							counter++;
					}
				}
				if(counter == (ship + 1)){
					String shipship = "";
					if(ship == 1){
						shipship = "Submarine!";
						p_sub.destroyed = true;
					}
					else if(ship == 2){
						shipship = "Destroyer!";
						p_destroyer.destroyed = true;
					}
					else{
						shipship = "Battleship!";
						p_battleship.destroyed = true;
					}
					message += " Enemy has destroyed your " + shipship;
					AI.shot = 'M';  //Make it choose a random number next time, because ship destroyed
					AI.shot2 = 'M';
				}
			}
		}
		if(turn == 'O')
			message = "Enemy " + message;
		System.out.println(message);
	}
	
	
	public void checkVictory(){  
       if(e_sub.destroyed && e_destroyer.destroyed && e_battleship.destroyed){
    	   victory = true;
    	   System.out.println("Congratulations! You've won!");
       }
       if(p_sub.destroyed && p_destroyer.destroyed && p_battleship.destroyed){
    	   victory = true;
    	   System.out.println("Defeat! You've lost your fleet!");
       }
	}
	
	
	public void initialize(){
		  for(int i=0;i<8;i++){
			  for(int j=0;j<8;j++){
				  enemyboard[i][j] = new GameBoard();
				  playerboard[i][j] = new GameBoard();
			  }			 
		  }
		  
		  turn = 'X';
		  victory = false;
		  count = 0;
		  p_sub = new ShipClass(1);
		  p_destroyer = new ShipClass(2);
		  p_battleship = new ShipClass(3);
		  e_sub = new ShipClass(1);
		  e_destroyer = new ShipClass(2);
		  e_battleship = new ShipClass(3);
	}
	
	public String turnOfAI(){
		int rand1 = 0;
		int rand2 = 0;
		
		if(AI.count<1 || (AI.shot == 'M' && AI.shot2 == 'M') || AI.error){
			rand1 = generator.nextInt(8);
		    rand2 = generator.nextInt(8);
			rand1 += 65;
			rand2 += 49;
			AI.error = false;
		}
		else if(AI.shot == 'H'){
			rand1 = AI.row;
			if(AI.col<56)
				rand2 = AI.col + 1;
			else
				rand2 = AI.col - 1;
		}
		else if(AI.shot2 == 'H' && AI.shot == 'M'){
			if(AI.row2<72)
				rand1 = AI.row2 + 1;
			else
				rand1 = AI.row2- 1;
			rand2 = AI.col2;
		}

		String rand3 = String.valueOf((char)rand1) + String.valueOf((char)rand2);
		
		AI.row2 = AI.row;
		AI.col2 = AI.col;
		AI.row = rand1;
		AI.col = rand2;
		
		return rand3;
	}
	

}
