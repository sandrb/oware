package oware;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
	int maxSeeds = 96; //initial and maximum number of seeds
	int[] currentPiles = new int[24]; //values represents the current piles, piles 0 - 11 are ours and piles 12 - 23 are the opponents
	int[] nextPiles = new int[currentPiles.length]; //value represents the piles of next move.
	int programScore; // The score of the program
	int inputScore; // The score of the input
	boolean isProgramTurn; //Next move is my turn or not
	int depthMax = 4; //the maximal depth
	Random randomGenerator = new Random();//Random Generator, used for testing
	Scanner user_input = new Scanner( System.in );
	
	/**
	 * Initializes a new game with an equal number of seeds per pile.
	 */
	public void newGame(){
		int seedsPerPile = maxSeeds / currentPiles.length;//calculate max number of seeds per pile
		for(int i =0; i < currentPiles.length; i++){
			currentPiles[i] = seedsPerPile;//assign number to each pile
		}
		System.out.println("Who starts? 0: computer, 1: player.");
		String input = user_input.nextLine();
		int start = Integer.parseInt(input);
		System.arraycopy(currentPiles, 0, nextPiles, 0, currentPiles.length);
		outputPiles();
		
		programScore = 0;
		inputScore = 0;	
		
		if(start == 1){
			inputMove();
		} else {
			computerMove();
		}
	}
		
	/**
	 * Let's the player do a move
	 */
	public void inputMove(){
		isProgramTurn = false;
		String options = "";
		for(int i = 0; i < nextPiles.length / 2; i++){//for each position
			if(nextPiles[i] > 0){//check if it is "movable"
				options += " " + i;//if yes, add it to the options				
			}
		}
		System.out.println("Select position to sow, options are:" + options);
		String input = user_input.nextLine();
		int position = Integer.parseInt(input);
		int lastChanged = sow(position);
		capture(lastChanged);//capture seeds if needed
		
		System.out.println();
		outputPiles();

		if(!hasWonLost()){//no winner yet, continue
			computerMove();			
		}		
	}
	
	/**
	 * Does a computer move
	 * Just a random move for now, just for I/O testing
	 */
	public void computerMove(){
		isProgramTurn = true;
		ArrayList<Integer> options = new ArrayList<Integer>();
		for(int i = 0; i < nextPiles.length / 2; i++){//for each position
			if(nextPiles[i + nextPiles.length / 2] > 0){//check if it is "movable"
				options.add(i + nextPiles.length / 2);
			}
		}
		//for testing: sow a random integer from the options
		int position = options.get(randomGenerator.nextInt(options.size()));
		
		int lastChanged = sow(position);
		capture(lastChanged);//capture seeds if needed
		System.out.println("Computer sowed position " + position);
		outputPiles();
		if(!hasWonLost()){//no winner yet, continue
			inputMove();			
		}		
	}
	
	/**
	 * Compute the next move according to current position
	 */
	public int computeNextMove(){
		Position pos_current = new Position();
		pos_current.setPiles(currentPiles);
		pos_current.setIsMyTurn(isProgramTurn);
		pos_current.setMyScore(programScore);
		pos_current.setYourScore(inputScore);
		int[] valuePosition = new int[2];
		valuePosition = minMaxValue(pos_current,0);
		return valuePosition[1];
	}
	
	
	/**
	 * returns the current piles in the same format as they are inputed
	 */
	public void outputPiles(){
		System.out.print("Player 1 (u input): ");
		for(int i = 0; i < 12; i++){
			System.out.print(nextPiles[i] + " ");
		}
		System.out.println();
		System.out.print("Player 2 (program): ");
		for(int i = 23; i >= 12; i--){
			System.out.print(nextPiles[i] + " ");
		}
		System.out.println();
		System.out.println("Program: " + programScore + ", input: " + inputScore);
		System.out.println();
	}
	
	
	/**
	 * Sows a certain position, spreading it's seeds over the neighbors
	 * @param pos_choose: position that is sowed.
	 * @return: returns the latest pile whose seed count was increased.
	 */
	private int sow(int pos_choose){
		if(pos_choose >= currentPiles.length || pos_choose < 0){
		   throw new IllegalArgumentException("Invalid input for sow, was " + pos_choose + ", should be 0 <= input < " + (currentPiles.length/2));			
		}
		int lastChanged = 0;
		int i = 0;
		while(nextPiles[pos_choose] > 0){
			if((pos_choose+i+1) % currentPiles.length != pos_choose){//skip pos_choose
				nextPiles[(pos_choose+i+1) % currentPiles.length]++;//place one seed
				lastChanged = (pos_choose+i+1) % currentPiles.length;
				nextPiles[pos_choose]--;//remove one seed				
			}
			i++;//go on to the next position
		}
		return lastChanged;
	}
	
	/**
	 * Calculates the sow for that position
	 * @param pos_current: the current position the game is in
	 * @param pos_choose: the position chosen
	 * @return the new position of the game after this sow
	 */
	private Position sowForCal(Position pos_current,int pos_choose){
		if(pos_choose >= currentPiles.length || pos_choose < 0){
		   throw new IllegalArgumentException("Invalid input for sow, was " + pos_choose + ", should be 0 <= input < " + (currentPiles.length));			
		}		
		
		Position pos_next = new Position();
		pos_next.setPiles(pos_current.getPiles()); // copy pos_current to pos_next
		int lastChanged = 0;
		int i = 0;
		
		while(pos_next.getPiles()[pos_choose] > 0){
			if((pos_choose+i+1) % currentPiles.length != pos_choose){//skip pos_choose
				pos_next.getPiles()[(pos_choose+i+1) % currentPiles.length]++;//place one seed
				lastChanged = (pos_choose+i+1) % currentPiles.length;
				pos_next.getPiles()[pos_choose]--;//remove one seed				
			}
			i++;//go on to the next position
			
		}
		
		pos_next.setLastChanged(lastChanged);
		pos_next.setIsMyTurn(!pos_current.getIsMyTurn());
		return pos_next;
	}
	
	/**
	 * Captures the seeds after a turn
	 * @param lastChanged: pile lastly increased
	 */
	private void capture(int lastChanged){
		//no calculation, actually capturing seeds now.		
		boolean doCapture = true;//set to false if it is not needed.
		int sumRemaining = 0;//sum of all the seeds remaining on the opponents side
		if(isProgramTurn){
			//the turn of the code
			for(int i = 0; i < nextPiles.length/2 && sumRemaining == 0; i++){
				//get sum of all positions NOT seeded (only need to know if it's more than 0)
				if(i > lastChanged){
					sumRemaining += nextPiles[i];
				}else if(nextPiles[i] != 2 && nextPiles[i] != 3){
					sumRemaining += nextPiles[i];
				}
			}
			if(sumRemaining == 0){
				// if a move would capture all of an opponent's seeds, the capture is forfeited since this would prevent the opponent from continuing the game, and the seeds are instead left on the board
				doCapture = false;
			}
			
			if(doCapture){
				while(lastChanged >= 0 && lastChanged < nextPiles.length/2 && nextPiles[lastChanged] >= 2 && nextPiles[lastChanged] <= 3){
					//continue as long as we are on users side and the number of seeds is 2 or 3
					programScore += nextPiles[lastChanged];
					nextPiles[lastChanged] = 0;	
					lastChanged--;
				}				
			}
		}else{
			//the turn of the user input
			for(int i = nextPiles.length/2; i < nextPiles.length && sumRemaining == 0; i++){
				//get sum of all positions NOT seeded (only need to know if it's more than 0)
				if(i > lastChanged){
					sumRemaining += nextPiles[i];
				}else if(nextPiles[i] != 2 && nextPiles[i] != 3){
					sumRemaining += nextPiles[i];
				}
			}
			if(sumRemaining == 0){
				// if a move would capture all of an opponent's seeds, the capture is forfeited since this would prevent the opponent from continuing the game, and the seeds are instead left on the board
				doCapture = false;
			}
			if(doCapture){
				while(lastChanged >= nextPiles.length/2 && nextPiles[lastChanged] >= 2 && nextPiles[lastChanged] <= 3){
					//continue as long as we are on the programs side and the number of seeds is 2 or 3
					inputScore += nextPiles[lastChanged];
					nextPiles[lastChanged] = 0;
					lastChanged--;				
				}					
			}		
		}
	}
	
	/**
	 * Checks if the game has been won or lost
	 * @return true if the game is finished, false otherwise
	 */
	private boolean hasWonLost(){
		//create a new position
		Position currentPosition = new Position();
		currentPosition.setPiles(nextPiles);
		currentPosition.setIsMyTurn(isProgramTurn);
		currentPosition.setMyScore(programScore);
		currentPosition.setYourScore(inputScore);
		
		//get the result for this position
		int result = finalPosition(currentPosition);
		if(result != -1){
			//game has finished, output result
			if(result == 96){
				System.out.println("The program has won, user input has lost.");
			}else if(result == -96){
				System.out.println("The user input has won, program has lost.");				
			}else{
				//result == 0
				System.out.println("The game resulted in a draw.");
			}
			return true;
		}else{
			//game has not yet finished
			return false;
		}
	}
	
	private int evaluation(Position pos_current, int pos_choose){
		//difference of the taken seeds from capture
		int seedsCaptured = 0;
		Position pos_next = new Position();
		pos_next = sowForCal(pos_current,pos_choose); // just sow 
		int lastChanged = pos_next.getLastChanged();
		
		if(pos_current.getIsMyTurn() && lastChanged >= pos_next.getPiles().length/2){ // Capture the opponent's seeds
			while(pos_next.getPiles()[lastChanged] >= 2 && pos_next.getPiles()[lastChanged] <= 3){
				//continue as long as we are on the opponents side and the number of seeds is 2 or 3
				seedsCaptured += pos_next.getPiles()[lastChanged]; 	//didn't consider capture all the seeds
				lastChanged--;
			}
		}else if (!pos_current.getIsMyTurn() && lastChanged>=0 && lastChanged<pos_next.getPiles().length/2){ // Capture my seeds
			//the turn of the user input
			while(pos_next.getPiles()[lastChanged] >= 2 && pos_next.getPiles()[lastChanged] <= 3 && lastChanged >= 0){
				//continue as long as we are on the opponents side and the number of seeds is 2 or 3
				seedsCaptured += pos_next.getPiles()[lastChanged];
				lastChanged--;				
			}			
		}else {
			seedsCaptured = 0;
		}

		return seedsCaptured;
	}
	
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
	
	private Position playMove(Position pos_current,int pos_choose){
		Position pos_next = new Position();
		if(finalPosition(pos_current) == -1){ //if not final position // doesn't do more valid check
			pos_next = sowForCal(pos_current,pos_choose);
			
			// Do capture and obtain the new pos_next!
			int seedsCaptured = 0;
			int lastChanged = pos_next.getLastChanged();
			if(pos_current.getIsMyTurn() && lastChanged >= pos_next.getPiles().length/2){ // Capture the opponent's seeds
				while(pos_next.getPiles()[lastChanged] >= 2 && pos_next.getPiles()[lastChanged] <= 3){
					//continue as long as we are on the opponents side and the number of seeds is 2 or 3
					seedsCaptured += pos_next.getPiles()[lastChanged]; 	//didn't consider capture all the seeds
					pos_next.setPiles(lastChanged, 0);
					lastChanged--;
				}
				pos_next.setMyScore(seedsCaptured + pos_current.getMyScore());
			}else if (!pos_current.getIsMyTurn() && lastChanged>=0 && lastChanged<pos_next.getPiles().length/2){ // Capture my seeds
				//the turn of the user input
				while(pos_next.getPiles()[lastChanged] >= 2 && pos_next.getPiles()[lastChanged] <= 3 && lastChanged >= 0){
					//continue as long as we are on the opponents side and the number of seeds is 2 or 3
					seedsCaptured += pos_next.getPiles()[lastChanged];
					pos_next.setPiles(lastChanged, 0);
					lastChanged--;				
				}	
				pos_next.setYourScore(seedsCaptured + pos_current.getYourScore());
			}else {
				seedsCaptured = 0;
			}
			
			pos_next.setIsMyTurn(!pos_current.getIsMyTurn());
		}else return null;
		return pos_next;
	}
	
	private int[] minMaxValue(Position pos_current, int depth){ 
		int[] valuePosition = new int[2];
		int[] tab_values = new int[12];
		Position pos_next; 
	    if (finalPosition(pos_current) == 96){
	    	valuePosition[0] = 96;
//	    	return 96;
	    }
	    if (finalPosition(pos_current) == -96){
	    	valuePosition[0] = -96;
//	    	return -96;
	    }
	    if (finalPosition(pos_current) == 0){
	    	valuePosition[0] = 0;
//	    	return 0;
	    }
	    
	    if (depth == depthMax) {
	    	// current is my turn
	    	if(pos_current.getIsMyTurn()){ 
	    		int result = evaluation(pos_current, 0);
	    		if(depthMax%2 == 0){ //bottom is also my turn // choose the max 
	    			for(int i = 0; i < pos_current.getPiles().length/2; i++){
			    		result = Math.max(evaluation(pos_current, i),result);
			    	}
	    		}else{ ////bottom is not my turn // choose the min
	    			for(int i = 0; i < pos_current.getPiles().length/2; i++){
			    		result = Math.min(evaluation(pos_current, i),result);
			    	}
	    		}
	    		valuePosition[0] = result;
//	    		return result;
	    	}else{// current is not my turn    		
	    		int result = evaluation(pos_current, pos_current.getPiles().length/2);
	    		if(depthMax%2 == 0){ //bottom is also not my turn // choose the min 
	    			for(int i = pos_current.getPiles().length/2; i < pos_current.getPiles().length; i++){
			    		result = Math.min(evaluation(pos_current, i),result);
			    	}
	    		}else{ //bottom is my turn // choose the max
	    			for(int i = pos_current.getPiles().length/2; i < pos_current.getPiles().length; i++){
			    		result = Math.max(evaluation(pos_current, i),result);
			    	}
	    		}
	    		valuePosition[0] = result;
//	    		return result;
	    	}
	    }
	    for(int i=0;i<12;i++){
	               // we play the move i
	               // WRITE function validMove(pos_current, computer_play,i)
	               // it checks whether we can select the seeds in cell i and play (if there is no seed the function returns false)
	    	if (validMove(pos_current,i)){
	                       // WRITE function playMove(&pos_next,pos_current, computer_play,i)
	                       // we play the move i from pos_current and obtain the new position pos_next
	    		pos_next = playMove(pos_current,i);
	 			// pos_next is the new current position and we change the player
	            tab_values[i]=minMaxValue(pos_next,depth+1)[0];
	    	} else {
				if (pos_current.getIsMyTurn()) tab_values[i]=-100;
				else tab_values[i]=+100;
	        }
	    }
	    int res = tab_values[0];
		if (pos_current.getIsMyTurn()){
			for(int i=0;i<12;i++){// WRITE the code: res contains the MAX of tab_values
				res = Math.max(tab_values[i], res);
			}
		} else {
			for(int i=0;i<12;i++){// WRITE the code: res contains the MIN of tab_values
				res = Math.min(tab_values[i], res);
			}        
		}
		valuePosition[0] = res;
		for(int i=0;i<12;i++){
			if(res==i){
				valuePosition[1] = i;
			}
		}
		return valuePosition;
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.newGame();
	}

}
