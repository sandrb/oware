package oware;

import java.util.ArrayList;

public class Position {
	int[] piles = new int[24];//assuming 24 piles here
	int programScore = 0; // My score, initiated with 0
	int inputScore = 0; // The score of the opponent, initiated with 0 
	boolean isProgramTurn; //Next move is my turn or not
	ArrayList<Integer> movesDone = new ArrayList<Integer>();
	double eveluationValue = 0;//value given to this position by the evaluation function
	
	public Position(int[] newPiles, boolean newIsProgramTurn, int newProgramScore, int newInputScore){
		piles = newPiles;
		programScore = newProgramScore;
		inputScore = newInputScore;
		isProgramTurn = newIsProgramTurn;
	}
	
	public Position(){
		//empty initializer in case of copy
	}
	
	/**
	 * Takes all the variables from another position and copies them into this position
	 * @param another the other position
	 */
	public void copy(Position another){
		//Used to copy a Position
		System.arraycopy(another.piles, 0, this.piles, 0, this.piles.length);
		if(another.isProgramTurn){
			this.isProgramTurn = true;
		}else{
			this.isProgramTurn = false;
		}
		this.programScore = Integer.valueOf(another.programScore);
		this.inputScore = Integer.valueOf(another.inputScore);
		this.movesDone = new ArrayList<Integer>(another.movesDone);
	}
	
	/**
	 * Plays the given position
	 * @param pos the position to play
	 */
	public void play(int position){
		int lastChanged = sow(position);
		capture(lastChanged);
		movesDone.add(position);
		isProgramTurn = !isProgramTurn;
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
}	
