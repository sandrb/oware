package oware;

import java.util.ArrayList;
import java.util.Arrays;

public class Algorithm{
	
	Position initialPosition;//the input values computed to Position
	
	/**
	 * Computes which pile to seed next
	 * @param currentPosition: array of the current position
	 * @param isProgramTurn: boolean, true if it is the programs turn
	 * @param programScore: integer, current program score
	 * @param inputScore: integer, current input player score
	 * @return the position to seed
	 * @pre first half of currentPosition belongs to input player, other half to computer player
	 */
	public int computeNextMove(int[] currentPosition, boolean isProgramTurn, int programScore, int inputScore){
		initialPosition = new Position(currentPosition, isProgramTurn, programScore, inputScore);
		int maxDepth = 6;
		Position result = decisionTree(maxDepth,initialPosition);		
		return result.movesDone.get(0);
	}


	/**
	 * Computes which pile to seed next
	 * @param curDepth: if 0, this will not go any deeper
	 * @param initialPosition: the position to use
	 * @return the position to seed
	 * @pre first half of currentPosition belongs to input player, other half to computer player
	 */
	private Position decisionTree(int curDepth, Position initialPosition) {
		ArrayList<Position> positions = generateAllPossibillities(initialPosition);
		Position returnPosition = new Position();
		Position compareValue;
		if(curDepth > 0){
			
			//go for next level
			if(initialPosition.isProgramTurn){
				//program turn, pick maximum value
				returnPosition.eveluationValue = -10000;
				for(Position position : positions){
					compareValue = decisionTree(curDepth - 1, position);
					if(compareValue.eveluationValue > returnPosition.eveluationValue && isMovable(compareValue)){
						returnPosition = compareValue;
					}
				}	
			}else{
				//not program turn, pick minimum value
				returnPosition.eveluationValue = 10000;
				for(Position position : positions){
					compareValue = decisionTree(curDepth - 1, position);
					if(compareValue.eveluationValue < returnPosition.eveluationValue && isMovable(compareValue)){
						returnPosition = compareValue;
					}
				}	
			}		
		}else{//do eveluation function
			//run evaluation function			
			returnPosition = evaluation(initialPosition);
		}
		//System.out.println("depth:" + curDepth);
		//printMovesDone(returnPosition);
		return returnPosition;
	}
	
	/**
	 * @param position: input position
	 * @return an arraylist with all possible positions after doing 1 move from the given position
	 */
	private ArrayList<Position> generateAllPossibillities(Position position){
		ArrayList<Integer> options = new ArrayList<Integer>();
		
		if(!position.isProgramTurn){
			//seed first half
			for(int i = 0; i < position.piles.length / 2; i++){//for each position
				if(position.piles[i] > 0){//check if it is "movable"
					options.add(i);
				}
			}			
		}else{
			//seed second half
			for(int i = 0; i < position.piles.length / 2; i++){//for each position
				if(position.piles[i + position.piles.length / 2] > 0){//check if it is "movable"
					options.add(i + position.piles.length / 2);
				}
			}
		}
		
		ArrayList<Position> returnValue = new ArrayList<Position>();
		if(options.size() > 0){
			for(int option : options){//for each option
				Position newPos = new Position();//create a new position
				newPos.copy(position);//copy current values
				newPos.play(option);//move this option
				returnValue.add(newPos);//add this option to the arrayList
			}			
		}else{//found a final position, just return the same input position
			returnValue.add(position);
		}
		
		return returnValue; //return the returnValue		
	}
	
	/**
	 * fills in the evaluation value of the position, higher is better for us.
	 * @param input: a position 
	 * @return the same position with an evaluationValue
	 */
	private Position evaluation(Position input){
		double evaluationScore = 0.0;
		//step 1: give points based on the current score
		evaluationScore = (double) input.programScore - (double) input.inputScore;
		//step 2: low values in the beginning of our side are bad as they can be easily captured, good on the opponents side
		for(int i = 0; i < 5; i++){
			if(input.piles[i + input.piles.length / 2] == 1){
				//can be formed into 2, assumed 75% chance
				evaluationScore -= 0.75 * 2;
			}else if(input.piles[i + input.piles.length / 2] == 2){
				//can be formed into 3, assumed 75% chance
				evaluationScore -= 0.75 * 3;				
			}
			if(input.piles[i] == 1){
				//can be formed into 2, assumed 75% chance
				evaluationScore += 0.75 * 2;
			}else if(input.piles[i] == 2){
				//can be formed into 3, assumed 75% chance
				evaluationScore += 0.75 * 3;				
			}
		}
		//step 3: high values at the end are good, 0.5 points for each seed in the last 5 positions
		for(int i = 0; i < 5; i++){
			if(input.piles[input.piles.length - 1 - i] > 10){
				evaluationScore += 5;
			}else{
				evaluationScore += input.piles[input.piles.length - 1 - i] * 0.5;				
			}
			if(input.piles[input.piles.length/2 - 1 - i] > 10){
				evaluationScore -= 5;				
			}else{
				evaluationScore -= input.piles[input.piles.length/2 - 1 - i] * 0.5;				
			}
		}
		//input.programScore
		input.eveluationValue = evaluationScore;
		return input;
	}
	
	/**
	 * Checks if both players can still do a move, returns false if not
	 * @param toCheck
	 * @return
	 */
	private boolean isMovable(Position toCheck){
		boolean returnValue1 = false;//initially false for first half
		boolean returnValue2 = false;//initially false for second half
		for(int i = 0; i < toCheck.piles.length / 2 && !returnValue1; i++){
			if(toCheck.piles[i] > 0){
				returnValue1 = true;
			}
		}

		for(int i = 0; i < toCheck.piles.length / 2 && !returnValue2; i++){
			if(toCheck.piles[i + toCheck.piles.length / 2] > 0){
				returnValue2 = true;
			}
		}
		return (returnValue1 && returnValue2 && (toCheck.movesDone.size() > 0));
	}
	
}
