package oware;

import java.util.Arrays;
import java.util.Scanner;

public class Game {
	int maxSeeds = 96; //initial and maximum number of seeds
	int[] currentPiles = new int[24]; //values represents the current piles, piles 0 - 11 are ours and piles 12 - 23 are the opponents
	int[] nextPiles = new int[24]; //value represents the piles of next move.
	int myScore = 0; // My score, initiated with 0
	int yourScore = 0; // The score of the opponent, initiated with 0 
	
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
			currentPiles[23 - i] = Integer.parseInt(inputArray[i]);
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
		game.setPiles();
		System.out.println("Input:");
		game.outputOriginalPiles();
		//todo: do move
		System.out.println("Output:");		
		game.outputPiles();
	}

}
