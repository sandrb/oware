package oware;

import java.util.Arrays;
import java.util.Scanner;

public class Game {
	int maxSeeds = 96; //initial and maximum number of seeds
	int[] currentPiles = new int[24]; //values represents the current piles, piles 0 - 11 are ours and piles 12 - 23 are the opponents
	int[] nextPiles = new int[currentPiles.length]; //value represents the piles of next move.
	int myScore = 0; // My score, initiated with 0
	int yourScore = 0; // The score of the opponent, initiated with 0 
//	boolean isMyTurn; //Next move is my turn or not
	int depthMax = 4; //the maximal depth
	
	/**
	 * Sets the values for the current piles
	 * Input should be in the following way:
	 * Player 1: 01 02 03 04 05 06 07 08 09 10 11 12
	 * Player 2: 24 23 22 21 20 19 18 17 16 15 14 13
	 */
	public void setPiles(){
		System.out.println("Player 1 piles? 12 numbers seperated by spaces.");
		Scanner user_input = new Scanner( System.in );
		String input = user_input.nextLine();
		String[] inputArray = input.split(" ");
		for(int i = 0; i < 12; i++){
			currentPiles[i] = Integer.parseInt(inputArray[i]);
		}	
		

		System.out.println("Player 2 piles? 12 numbers seperated by spaces.");
		input = user_input.nextLine();
		inputArray = input.split(" ");
		for(int i = 0; i < 12; i++){
			currentPiles[12 + i] = Integer.parseInt(inputArray[i]);
			//Alternatively: replace "12 + i" with "23 - i" if you want to fill up in the same way as is displayed.
		}
		
		System.arraycopy(currentPiles, 0, nextPiles, 0, currentPiles.length);
	}
	
	/**
	 * Initializes a new game with an equal number of seeds per pile.
	 */
	public void newGame(){
		int seedsPerPile = maxSeeds / currentPiles.length;//calculate max number of seeds per pile
		for(int i =0; i < currentPiles.length; i++){
			currentPiles[i] = seedsPerPile;//assign number to each pile
		}
		System.out.println("game created");
		
	}
	
	/**
	 * returns the current piles in the same format as they are inputed
	 */
	public void outputPiles(){
		System.out.print("Player 1: ");
		for(int i = 0; i < 12; i++){
			System.out.print(nextPiles[i] + " ");
		}
		System.out.println();
		System.out.print("Player 2: ");
		for(int i = 23; i >= 12; i--){
			System.out.print(nextPiles[i] + " ");
		}
		System.out.println();
	}
	
	
	/**
	 * Sows a certain position, spreading it's seeds over the neighbors
	 * @param pos_choose: position that is sowed, must be one of our own positions.
	 */
	private void sow(int pos_choose){
		if(pos_choose >= currentPiles.length/2 || pos_choose < 0){
		   throw new IllegalArgumentException("Invalid input for sow, was " + pos_choose + ", should be 0 <= input < " + (currentPiles.length/2));			
		}
		
		int i = 1;
		while(nextPiles[pos_choose] > 0){
			if((pos_choose+i+1) % currentPiles.length != pos_choose){//skip pos_choose
				nextPiles[(pos_choose+i+1) % currentPiles.length]++;//place one seed
				nextPiles[pos_choose]--;//remove one seed				
			}
			i++;//go on to the next position
		}
	}
	
//	/**
//	 * Starts capturing from a certain position
//	 * @param pos_choose: position to start capturing from, must be one from the opponent.
//	 * note: starts at the last position and goes down from there on, so if we start on 15, then follows 14, 13, etc.
//	 */
//	private void capture(int pos_choose){
//		//1st input condition: must be opponents house
//		if(pos_choose >= currentPiles.length/2 || pos_choose < 0){
//		   throw new IllegalArgumentException("Invalid input for sow, was " + pos_choose + ", should be " + (currentPiles.length/2) + " <= input < " + currentPiles.length);			
//		}
//		
//		//second input condition: must be a house that changed this turn
//		if(nextPiles[pos_choose] == currentPiles[pos_choose]){
//			throw new IllegalArgumentException("Invalid input for sow, house " + pos_choose + " was not changed by this player, hence it is not allowed to sow it.");			
//		}
//		
//		//third input condition: the seed total of the house must be 2 or three
//		if(nextPiles[pos_choose] > 3 || nextPiles[pos_choose] < 2){
//			throw new IllegalArgumentException("Invalid input for sow, house " + nextPiles + " has " + nextPiles[pos_choose] + " seeds, should be 2 or 3.");			
//		}		
//		
//		while(pos_choose >= currentPiles.length/2 && nextPiles[pos_choose] < 4 && nextPiles[pos_choose] > 1){			
//			//remove seeds
//			myScore += nextPiles[pos_choose];
//			nextPiles[pos_choose] = 0;
//			
//			//previous position
//			pos_choose--;
//		}
//	}

	
	
//	/**
//	 * @return true, if we have won, false otherwise.
//	 */
//	private boolean hasWon(){
//		//return true if: enough points OR opponent has no more valid moves.
//		if(myScore > maxSeeds/2){
//			//at least half+1 of the seeds
//			return true;
//		}
//		
//		int opponentSeeds = 0;
//		for(int i = nextPiles.length / 2; i < nextPiles.length && opponentSeeds == 0; i++){
//			opponentSeeds += nextPiles[i];
//		}
//		if(opponentSeeds == 0){
//			return true;
//		}
//		
//		return false;
//	}
	private int finalPosition(Position pos_current){
		//Ending condition1: one player took more than half of the seeds.
		if(pos_current.getMyScore() > maxSeeds/2){
			return 96; //I win
		}
		if(pos_current.getYourScore() > maxSeeds/2){
			return -96; // You win
		}
		if(pos_current.getMyScore() == maxSeeds/2 && pos_current.getYourScore()== maxSeeds/2){
			return 0; //Draw
		}
		
		//Ending condition2: piles of playerA are all empty and playerB cannot give A seeds in the next move.
		//Note: one player is empty != game is finished.
		int seeds = 0;
		if(pos_current.getIsMyTurn()){ // is my turn 
			for(int i = 12; i < 24; i++){
				seeds += pos_current.getPiles()[i];
			}
			if(seeds == 0 ){ //opponent is empty
				boolean isFinished = true; // Game is finished or not, initiated with true
				for(int j=0;j<12;j++){
					if(pos_current.getPiles()[j] >= 12-j){ //not finished
						isFinished = false;
					}
				}
				if(isFinished){
					for(int j=0;j<12;j++){
						seeds += pos_current.getPiles()[j];
					}
					pos_current.setMyScore(pos_current.getMyScore() + seeds);
					if(pos_current.getMyScore() > maxSeeds/2){
						return 96; //I win
					}
					if(pos_current.getYourScore() > maxSeeds/2){
						return -96; // You win
					}
					if(pos_current.getMyScore() == maxSeeds/2 && pos_current.getYourScore()== maxSeeds/2){
						return 0; //Draw
					}
				}		
			}
		}else{ // is not my turn
			for(int i = 0; i < 12; i++){
				seeds += pos_current.getPiles()[i];
			}
			if(seeds == 0 ){ //My piles are empty
				boolean isFinished = true; // Game is finished or not, initiated with true
				for(int j=12;j<24;j++){
					if(pos_current.getPiles()[j] >= 24-j){ //not finished
						isFinished = false;
					}
				}
				if(isFinished){
					for(int j=12;j<24;j++){
						seeds += pos_current.getPiles()[j];
					}
					pos_current.setYourScore(pos_current.getYourScore() + seeds);
					if(pos_current.getMyScore() > maxSeeds/2){
						return 96; //I win
					}
					if(pos_current.getYourScore() > maxSeeds/2){
						return -96; // You win
					}
					if(pos_current.getMyScore() == maxSeeds/2 && pos_current.getYourScore()== maxSeeds/2){
						return 0; //Draw
					}
				}		
			}
		}
		
		return -1;// Not finalPosition
	}
	
	//Scenario1: In a situation to capture all the seeds of other's. Not capture.
//	private void computeNextMove(){
//		//todo, step 1: sow, optional step 2: capture, step 3: check if we've won (only needed if we did 2)
//		//for the capture step i think it would be nice to do a greedy approach and get as much seeds as possible
//		
//		//option: infinite loop check?
//	}
	
	private int evaluation(Position pos_current, boolean isMyTurn, int depth, int pos_choose){
		//difference of the taken seeds from capture
		
		return 1;
	}
	private boolean validMove(Position pos_current,int pos_choose){
		if(finalPosition(pos_current) == -1){ // if not final position  // has some overlap in finalPosition considering only i
			
			//1. there is no seed the function returns false
			if(pos_current.getPiles()[pos_choose] == 0){
				return false;
			}
			
			//2. piles of playerA are all empty and playerB must give A seeds in the next move. 
			int seeds = 0;
			if(pos_current.getIsMyTurn()){ // is my turn 
				for(int i = 12; i < 24; i++){
					seeds += pos_current.getPiles()[i];
				}
				if(seeds == 0 ){ //opponent is empty
					if(pos_current.getPiles()[pos_choose] >= 12-pos_choose){ //I can give opponent seeds in the next move
						return true;
					}else return false;
				}else return true;		
			}else{ // is not my turn
				for(int i = 0; i < 12; i++){
					seeds += pos_current.getPiles()[i];
				}
				if(seeds == 0 ){ //My piles are empty
					if(pos_current.getPiles()[pos_choose] >= 24-pos_choose){ //Opponent can give me seeds in the next move
						return true;
					}else return false;
				}else return true;				
			}
			
			//3. capture all other's seeds
			
		}else return false;
	}
	
	private Position playMove(Position pos_current,boolean isMyTurn,int pos_choose){
		Position pos_next = new Position();
		if(finalPosition(pos_current) == -1){ //if not final position // doesn't do more valid check
			int num = pos_current.getPiles()[pos_choose];
			pos_next.setPiles(pos_current.getPiles()); // copy pos_current to pos_next
			if(pos_current.getIsMyTurn() && pos_choose<12 && pos_choose>=0){ // is my turn
				pos_next.setPiles(pos_choose,0);
				for(int i = 1; i <= num; i++){
					if(pos_choose + i < 24){
						pos_next.setPiles(pos_choose + i,pos_next.getPiles()[pos_choose + i]+1);	
					}
					if(pos_choose + i >=24 && i<24){
						pos_next.setPiles(pos_choose+i-24,pos_next.getPiles()[pos_choose+i-24]+1);
					}
					if(i>=24 && i<36){ // skip pos_choose once and end at opponent's piles
						pos_next.setPiles(pos_choose+i-24+ 1,pos_next.getPiles()[pos_choose+i-24+1 + 1]+1);
					}			
					if(i>=36){//skip pos_choose once
						//.....complex here, working on it later
					}
				}
				
			}
			else if(!pos_current.getIsMyTurn() && pos_choose<24 && pos_choose>=12){ // is not my turn
				// need to complete
			}else{
				System.out.println("Wrong chosen position");
			}
			
			// Do capture and obtain the new pos_next!
			
			pos_next.setIsMyTurn(!pos_current.getIsMyTurn());
		}else return null;
		return pos_next;
	}
	
	private int minMaxValue(Position pos_current, boolean isMyTurn, int depth){ //isMyTurn??
		int[] tab_values = new int[12];
		Position pos_next; 
	    if (finalPosition(pos_current) == 96){
	    	return 96;
	    }
	    if (finalPosition(pos_current) == -96){
	    	return -96;
	    }
	    if (finalPosition(pos_current) == 0){
	    	return 0;
	    }
	    
	    if (depth == depthMax) {
	    	return evaluation(pos_current, isMyTurn, depth);
	               // the simplest evealution fucntion is the difference of the taken seeds
	    }
	    for(int i=0;i<12;i++){
	               // we play the move i
	               // WRITE function validMove(pos_current, computer_play,i)
	               // it checks whether we can select the seeds in cell i and play (if there is no seed the function returns false)
	    	if (validMove(pos_current,i)){
	                       // WRITE function playMove(&pos_next,pos_current, computer_play,i)
	                       // we play the move i from pos_current and obtain the new position pos_next
	    		pos_next = playMove(pos_current, isMyTurn,i);
	 			// pos_next is the new current position and we change the player
	            tab_values[i]=minMaxValue(pos_next,!isMyTurn,depth+1);
	    	} else {
				if (isMyTurn) tab_values[i]=-100;
				else tab_values[i]=+100;
	        }
	      	}
	    int res;
		if (isMyTurn){
		               // WRITE the code: res contains the MAX of tab_values
		} else {
		               // WRITE the code: res contains the MIN of tab_values
		}
		return res;
		
	}
	
	/**
	 * returns the original piles in the same format as they are inputed
	 */
	public void outputOriginalPiles(){
		System.out.print("Player 1: ");
		for(int i = 0; i < 12; i++){
			System.out.print(currentPiles[i] + " ");
		}
		System.out.println();
		System.out.print("Player 2: ");
		for(int i = 23; i >= 12; i--){
			System.out.print(currentPiles[i] + " ");
		}
		System.out.println();
	}
	
	/**
	 * returns the changes we made
	 */
	public void outputChanges(){
		for(int i = 0; i < nextPiles.length; i++){
			if(nextPiles[i] != currentPiles[i]){
				System.out.print(i + " " + nextPiles[i]);				
			}
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.newGame();
		game.setPiles();
		System.out.println("Input:");
		game.outputOriginalPiles();
		System.out.println("test move on place 10");
		game.sow(10);
		System.out.println("Output:");		
		game.outputPiles();
	}

}
