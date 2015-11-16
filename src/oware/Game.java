package oware;

import java.util.Arrays;

public class Game {
	int maxSeeds = 96; //initial and maximum number of seeds
	int[] piles = new int[24]; //values represent the piles, piles 0 - 11 are ours and piles 12 - 23 are the opponents
	
	/**
	 * Sets the values for the current piles
	 * @param input: the values for the current piles, 24 non-negative integers differentiated by spaces representing the 12 piles
	 */
	public void setPiles(String input){
		String[] inputArray = input.split(" ");
		//todo: string --> integer array
	}
	
	/**
	 * Initializes a new game with an equal number of seeds per pile.
	 */
	public void newGame(){
		int seedsPerPile = maxSeeds / piles.length;//calculate max number of seeds per pile
		for(int i =0; i < piles.length; i++){
			piles[i] = seedsPerPile;//assign number to each pile
		}
		System.out.println("game created");
		System.out.print(Arrays.toString(piles));
	}
	
	/**
	 * returns the current piles in the same format as they are inputed
	 */
	public void getPiles(){
		//todo println string		
	}
	
	
	private void computeNextMove(){
		//place where all the work gets done.
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.newGame();
		//todo: initialize game.
	}

}
