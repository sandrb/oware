package oware;

import java.util.Arrays;

public class Game {
	int maxSeeds = 96; //initial and maximum number of seeds
	int[] currentPiles = new int[24]; //values represents the current piles, piles 0 - 11 are ours and piles 12 - 23 are the opponents
	int[] nextPiles = new int[24]; //value represents the piles of next move.
	int myScore = 0; // My score, initiated with 0
	int yourScore = 0; // The score of the opponent, initiated with 0 
	
	/**
	 * Sets the values for the current piles
	 * @param input: the values for the current piles, 24 non-negative integers differentiated by spaces representing the 12 piles
	 * @param input: it is also possible to name the changed piles only for example 3 5 to state that pile 3 has been changed to 5, assumed we changed less than 12 piles
	 */
	public void setPiles(String input){
		String[] inputArray = input.split(" ");
		if(inputArray.length > 23){
			for(int i = 0; i < inputArray.length; i++){
				currentPiles[i] = Integer.parseInt(inputArray[i]);
			}			
		}else{
			for(int i = 0; i < inputArray.length; i= i + 2){
				currentPiles[i] = Integer.parseInt(inputArray[i+1]);
			}			
		}
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
		for(int i = 0; i < nextPiles.length; i++){
			System.out.print(nextPiles[i] + " ");
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
	
	//A move is valid or not.
	private boolean isValidMove(int[] tmpPiles){
		//We need to find out all the invalid moves
		
		return true;
	}
	
	private void move(int pos_choose){
		int num = currentPiles[pos_choose];
		for(int i = 0;i<num;i++){
			if(pos_choose +i +1 < 24){
				nextPiles[pos_choose +i +1] = 1 + currentPiles[pos_choose +i +1];
			}else{
				nextPiles[pos_choose +i +1 -24] = 1 + currentPiles[pos_choose +i +1 -24];
			}
		}
	}
	
	//Scenario1: In a situation to capture all the seeds of other's. Not capture.
	
	

	private void computeNextMove(){
		//todo
	}
	

	public static void main(String[] args) {
		Game game = new Game();
		game.newGame();
		//todo: input, do move
		game.outputPiles();
	}

}
