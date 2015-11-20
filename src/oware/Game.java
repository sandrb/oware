package oware;

import java.util.Arrays;
import java.util.Scanner;

public class Game {
	int maxSeeds = 96; //initial and maximum number of seeds
	int[] currentPiles = new int[24]; //values represents the current piles, piles 0 - 11 are ours and piles 12 - 23 are the opponents
	int[] nextPiles = new int[currentPiles.length]; //value represents the piles of next move.
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
	
	//A move is valid or not.
	private boolean isValidMove(int[] tmpPiles){
		//We need to find out all the invalid moves
		//Sander: is this really needed? If we just do the move() and capture() functions right we know it's always valid.
		return true;
	}
	
	/**
	 * Sows a certain position, spreading it's seeds over the neighbors
	 * @param pos_choose: position that is sowed, must be one of our own positions.
	 */
	private void sow(int pos_choose){
		if(pos_choose >= currentPiles.length/2 || pos_choose < 0){
		   throw new IllegalArgumentException("Invalid input for sow, was " + pos_choose + ", should be 0 <= input < " + (currentPiles.length/2));			
		}
		
		int num = currentPiles[pos_choose];
		nextPiles[pos_choose] = 0;
		for(int i = 0;i<num;i++){
			nextPiles[(pos_choose+i+1) % currentPiles.length]++;
		}
	}
	
	/**
	 * Starts capturing from a certain position
	 * @param pos_choose: position to start capturing from, must be one from the opponent.
	 * note: starts at the last position and goes down from there on, so if we start on 15, then follows 14, 13, etc.
	 */
	private void capture(int pos_choose){
		//1st input condition: must be opponents house
		if(pos_choose >= currentPiles.length/2 || pos_choose < 0){
		   throw new IllegalArgumentException("Invalid input for sow, was " + pos_choose + ", should be " + (currentPiles.length/2) + " <= input < " + currentPiles.length);			
		}
		
		//second input condition: must be a house that changed this turn
		if(nextPiles[pos_choose] == currentPiles[pos_choose]){
			throw new IllegalArgumentException("Invalid input for sow, house " + pos_choose + " was not changed by this player, hence it is not allowed to sow it.");			
		}
		
		//third input condition: the seed total of the house must be 2 or three
		if(nextPiles[pos_choose] > 3 || nextPiles[pos_choose] < 2){
			throw new IllegalArgumentException("Invalid input for sow, house " + nextPiles + " has " + nextPiles[pos_choose] + " seeds, should be 2 or 3.");			
		}		
		
		while(pos_choose >= currentPiles.length/2 && nextPiles[pos_choose] < 4 && nextPiles[pos_choose] > 1){			
			//remove seeds
			myScore += nextPiles[pos_choose];
			nextPiles[pos_choose] = 0;
			
			//previous position
			pos_choose--;
		}
	}
	
	private boolean hasWon(){
		//return true if: enough points OR opponent has no more valid moves.
		
		return false;
	}
	
	//Scenario1: In a situation to capture all the seeds of other's. Not capture.
	private void computeNextMove(){
		//todo, step 1: sow, optional step 2: capture, step 3: check if we've won (only needed if we did 2)
		//for the capture step i think it would be nice to do a greedy approach and get as much seeds as possible
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
