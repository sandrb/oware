package oware2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameSander {
	int maxSeeds = 96; //initial and maximum number of seeds
	int[] piles = new int[24]; //values represents the piles, piles 0 - 11 are the users and piles 12 - 23 are the programs
	int programScore; // The score of the program
	int inputScore; // The score of the input
	boolean isProgramTurn; //Next move is my turn or not
	int depthMax = 4; //the maximal depth
	Random randomGenerator = new Random();//Random Generator, used for testing
	Scanner user_input = new Scanner( System.in );
	Algorithm algorithm = new Algorithm();
	
	/**
	 * Initializes a new game with an equal number of seeds per pile.
	 */
	public void newGame(){
		int seedsPerPile = maxSeeds / piles.length;//calculate max number of seeds per pile
		for(int i =0; i < piles.length; i++){
			piles[i] = seedsPerPile;//assign number to each pile
		}
		System.out.println("Who starts? 0: computer, 1: player.");
		String input = user_input.nextLine();
		int start = Integer.parseInt(input);
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
		for(int i = 0; i < piles.length / 2; i++){//for each position
			if(piles[i] > 0){//check if it is "movable"
				options += " " + i;//if yes, add it to the options				
			}
		}
		System.out.println("Select position to sow, options are:" + options);
		String input = user_input.nextLine();
		int position = Integer.parseInt(input);
		while(position < 0 || position > piles.length / 2 || piles[position] == 0){
			System.out.println("Invalid input, options are:" + options);
			input = user_input.nextLine();
			position = Integer.parseInt(input);
		}
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
	 */
	public void computerMove(){
		isProgramTurn = true;
		ArrayList<Integer> options = new ArrayList<Integer>();
		for(int i = 0; i < piles.length / 2; i++){//for each position
			if(piles[i + piles.length / 2] > 0){//check if it is "movable"
				options.add(i + piles.length / 2);
			}
		}
		//for testing: sow a random integer from the options		
        //int position = options.get(randomGenerator.nextInt(options.size()));
		int position = algorithm.computeNextMove(piles, isProgramTurn, programScore, inputScore);
		int lastChanged = sow(position);
		capture(lastChanged);//capture seeds if needed
		System.out.println("Computer sowed position " + position);
		outputPiles();
		if(!hasWonLost()){//no winner yet, continue
			inputMove();			
		}		
	}	
	
	/**
	 * returns the current piles in the same format as they are inputed
	 */
	public void outputPiles(){
		System.out.print("Player 1 (u input): ");
		for(int i = 0; i < 12; i++){
			System.out.print(piles[i] + " ");
		}
		System.out.println();
		System.out.print("Player 2 (program): ");
		for(int i = 23; i >= 12; i--){
			System.out.print(piles[i] + " ");
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
		if(pos_choose >= piles.length || pos_choose < 0){
		   throw new IllegalArgumentException("Invalid input for sow, was " + pos_choose + ", should be 0 <= input < " + (piles.length/2));			
		}
		int lastChanged = 0;
		int i = 0;
		while(piles[pos_choose] > 0){
			if((pos_choose+i+1) % piles.length != pos_choose){//skip pos_choose
				piles[(pos_choose+i+1) % piles.length]++;//place one seed
				lastChanged = (pos_choose+i+1) % piles.length;
				piles[pos_choose]--;//remove one seed				
			}
			i++;//go on to the next position
		}
		return lastChanged;
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
			for(int i = 0; i < piles.length/2 && sumRemaining == 0; i++){
				//get sum of all positions NOT seeded (only need to know if it's more than 0)
				if(i > lastChanged){
					sumRemaining += piles[i];
				}else if(piles[i] != 2 && piles[i] != 3){
					sumRemaining += piles[i];
				}
			}
			if(sumRemaining == 0){
				// if a move would capture all of an opponent's seeds, the capture is forfeited since this would prevent the opponent from continuing the game, and the seeds are instead left on the board
				doCapture = false;
			}
			
			if(doCapture){
				while(lastChanged >= 0 && lastChanged < piles.length/2 && piles[lastChanged] >= 2 && piles[lastChanged] <= 3){
					//continue as long as we are on users side and the number of seeds is 2 or 3
					programScore += piles[lastChanged];
					piles[lastChanged] = 0;	
					lastChanged--;
				}				
			}
		}else{
			//the turn of the user input
			for(int i = piles.length/2; i < piles.length && sumRemaining == 0; i++){
				//get sum of all positions NOT seeded (only need to know if it's more than 0)
				if(i > lastChanged){
					sumRemaining += piles[i];
				}else if(piles[i] != 2 && piles[i] != 3){
					sumRemaining += piles[i];
				}
			}
			if(sumRemaining == 0){
				// if a move would capture all of an opponent's seeds, the capture is forfeited since this would prevent the opponent from continuing the game, and the seeds are instead left on the board
				doCapture = false;
			}
			if(doCapture){
				while(lastChanged >= piles.length/2 && piles[lastChanged] >= 2 && piles[lastChanged] <= 3){
					//continue as long as we are on the programs side and the number of seeds is 2 or 3
					inputScore += piles[lastChanged];
					piles[lastChanged] = 0;
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
		if(programScore > maxSeeds / 2){
			//program has more than half the points
			System.out.println("The program has won, user input has lost.");
			return true;
		}else if(inputScore > maxSeeds / 2){
			//user has more than half the points
			System.out.println("The user input has won, program has lost.");	
			return true;			
		}else if(inputScore == maxSeeds / 2 && programScore == maxSeeds / 2){
			//points equally divided
			System.out.println("The game resulted in a draw.");
			return true;						
		}else{
			return false;
		}
	}

	public static void main(String[] args) {
		GameSander game = new GameSander();
		game.newGame();
	}

}
