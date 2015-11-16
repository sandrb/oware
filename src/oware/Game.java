package oware;

public class Game {
	int[] piles = new int[12]; //values represent the piles, piles 0 - 5 are ours and piles 6 - 11 are the opponents
	
	/**
	 * Sets the values for the current piles
	 * @param input: the values for the current piles, 12 non-negative integers differentiated by spaces representing the 12 piles
	 */
	public void setPiles(String input){
		String[] inputArray = input.split(" ");
		//todo: string --> integer array
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
		//todo: initialize game.
	}

}
