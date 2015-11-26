package oware;

import java.util.ArrayList;
import java.util.Scanner;

public class Game1 {
	int maxSeeds = 96; //initial and maximum number of seeds
	int[] currentPiles = new int[24]; //values represents the current piles, piles 0 - 11 are ours and piles 12 - 23 are the opponents
	int[] nextPiles = new int[currentPiles.length]; //value represents the piles of next move.
	int myScore; // The score of the mine
	int yourScore; // The score of the opponent
	boolean isMyTurn; //Next move is my turn or not
	int depthMax = 4; //the maximal depth
	Scanner user_input = new Scanner( System.in );

	boolean testMode = false;//if testmode enabled, we use random variables instead of user input

	boolean areWeFirstPlayer;

	
	public static void main(String[] args) {
		Game1 game = new Game1();
		game.newGame();
	}
	public void newGame(){
		for(int i = 0; i < currentPiles.length; i++){
			currentPiles[i] = maxSeeds/currentPiles.length;
		}
		System.out.println("Who will be the first player? 0: We, 1: Opponent.");
		String input = user_input.nextLine();
		int start = Integer.parseInt(input);
		System.arraycopy(currentPiles, 0, nextPiles, 0, currentPiles.length);
		outputPiles();
		
		myScore = 0;
		yourScore = 0;
		
		if(start ==1){ //Opponent start first
			areWeFirstPlayer = false;
			opponentMove();		

		}else if(start == 2){//We start first
			myMove();
		}else if(start == 7){//Opponent start first, testmode enabled
			testMode = true;
			opponentMove();		
		} else if(start == 8){//We start first, testmode enabled
			testMode = true;
			myMove();			

		}if(start ==0){//We start first
			areWeFirstPlayer = true;
			myMove();
		}else{
			System.out.println("Should input 0 or 1");

		}
	}
	public void opponentMove(){
		isMyTurn = false;
//		ArrayList<Integer> Options = options();
//		System.out.println("Please input the opponent's position (options:" + optionsToString(Options) + ")");
		System.out.println("Please input the opponent's position");
		String input = user_input.nextLine();
		int position = Integer.parseInt(input);
		if(!areWeFirstPlayer){// opponent is the first player
			while(position<1 || position>12 || currentPiles[position+11] == 0){
				System.out.println("position should between 1 and 12 and contain seeds");
				input = user_input.nextLine();
				position = Integer.parseInt(input);
			}
			position = position+ 11;
			
		}else{ //we are the first player
			while(position<13 || position>24 || currentPiles[position-1] == 0){
				System.out.println("position should between 13 and 24 and contain seeds");
				input = user_input.nextLine();
				position = Integer.parseInt(input);
			}
			position = position- 1;
		}
		
		int lastChanged = sow(position);
		capture(lastChanged);//capture seeds if needed
		
		System.out.println();
		outputPiles();
		
		System.arraycopy(nextPiles, 0, currentPiles, 0, currentPiles.length); //nextpiles become currentpiles
		
		if(!hasWonLost()){//no winner yet, continue
			myMove();			
		}	
		
	}
	public ArrayList<Integer> options(){
		ArrayList<Integer> options = new ArrayList<Integer>();
		if(isMyTurn){
			//checks for the fist half
			for(int i = 0; i < nextPiles.length / 2; i++){//for each position
				if(nextPiles[i] > 0){//check if it is "movable"
					options.add(i);
				}
			}			
		}else{
			//checks for the second half
			for(int i = 0; i < nextPiles.length / 2; i++){//for each position
				if(nextPiles[i + nextPiles.length / 2] > 0){//check if it is "movable"
					options.add(i + nextPiles.length / 2);
				}
			}	
		}
		return options;
	}
	public String optionsToString(ArrayList<Integer> options){
		String returnVal = "";
		for (Integer i : options){
		    returnVal += " " + i;
		}
		return returnVal;
	}
	public void myMove(){
		isMyTurn = true;
		int suggestPos = computeNextMove();
		int lastChanged = sow(suggestPos);
		capture(lastChanged);//capture seeds if needed
		
		if(!areWeFirstPlayer){
			System.out.println("suggest position is "+ (suggestPos+13) +". After this");
		}else{
			System.out.println("suggest position is "+ (suggestPos+1) +". After this");
		}
		
		System.out.println();
		outputPiles();
		
		System.arraycopy(nextPiles, 0, currentPiles, 0, currentPiles.length); //nextpiles become currentpiles
		
		if(!hasWonLost()){//no winner yet, continue
			opponentMove();			
		}
	}
	
	/**
	 * Compute the next move according to current position
	 */
	public int computeNextMove(){
		Position pos_current = new Position();
		pos_current.setPiles(currentPiles);
		pos_current.setIsMyTurn(isMyTurn);
		pos_current.setMyScore(myScore);
		pos_current.setYourScore(yourScore);
		int[] valuePosition = new int[2];
		valuePosition = minMaxValue(pos_current,0);
		return valuePosition[1];
	}
	
	/**
	 * Sows a certain position, spreading it's seeds over the neighbors
	 * @param pos_choose: position that is sowed.
	 * @return: returns the latest pile whose seed count was increased.
	 */
	private int sow(int pos_choose){

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
	 * Captures the seeds after a turn
	 * @param lastChanged: pile lastly increased
	 */
	private void capture(int lastChanged){
		//no calculation, actually capturing seeds now.		

		int sumRemaining = 0;//sum of all the seeds remaining on the opponents side
		int[] tmpPiles = new int[currentPiles.length];
		int tmpScore;
		System.arraycopy(nextPiles, 0, tmpPiles, 0, nextPiles.length);
		
		if(isMyTurn){ // Capture the opponent's seeds
			tmpScore = myScore;
			while(lastChanged >= tmpPiles.length/2 && tmpPiles[lastChanged] >= 2 && tmpPiles[lastChanged] <= 3){
				//continue as long as we are on the programs side and the number of seeds is 2 or 3
				tmpScore += tmpPiles[lastChanged];
				tmpPiles[lastChanged] = 0;
				lastChanged--;				
			}	
			
			for(int i = tmpPiles.length/2; i < tmpPiles.length; i++){
				sumRemaining += tmpPiles[i];
			}
			
			if(sumRemaining != 0){ //can capture //if sumRemaining == 0, shouldn't capture
				myScore = tmpScore;
				System.arraycopy(tmpPiles, 0, nextPiles, 0, nextPiles.length);
			}
		
		}else{ // Capture my seeds
			tmpScore = yourScore;
			while(lastChanged >= 0 && lastChanged < tmpPiles.length/2 && tmpPiles[lastChanged] >= 2 && tmpPiles[lastChanged] <= 3){
				//continue as long as we are on users side and the number of seeds is 2 or 3
				tmpScore += tmpPiles[lastChanged];
				tmpPiles[lastChanged] = 0;	
				lastChanged--;
			}	
			for(int i = 0; i < tmpPiles.length/2; i++){
				sumRemaining += tmpPiles[i];
			}
			if(sumRemaining != 0){ //can capture //if sumRemaining == 0, shouldn't capture
				yourScore = tmpScore;
				System.arraycopy(tmpPiles, 0, nextPiles, 0, nextPiles.length);
			}	
		}
	}
	/**
	 * Checks if the game has been won or lost
	 * @return true if the game is finished, false otherwise
	 */
	private boolean hasWonLost(){
		//create a new Position
		Position currentPosition = new Position();
		currentPosition.setPiles(currentPiles);
		currentPosition.setIsMyTurn(isMyTurn);
		currentPosition.setMyScore(myScore);
		currentPosition.setYourScore(yourScore);
		
		//get the result for this position
		int result = finalPosition(currentPosition);
		if(result != -1){
			//game has finished, output result
			if(result == 96){
				System.out.println("I won! The opponent lost.");
			}else if(result == -96){
				System.out.println("The opponent won! I lost.");				
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
	public void outputPiles(){
		if(areWeFirstPlayer){
			System.out.print("Me:       ");
			for(int i = 0; i < 12; i++){
				System.out.print(nextPiles[i] + " ");
			}
			System.out.println();
			System.out.print("Opponent: ");
			for(int i = 23; i >= 12; i--){
				System.out.print(nextPiles[i] + " ");
			}
		}else{
			System.out.print("Opponent: ");
			for(int i = 12; i <24; i++){
				System.out.print(nextPiles[i] + " ");
			}			
			System.out.println();
			System.out.print("Me:       ");
			for(int i = 11; i >=0; i--){
				System.out.print(nextPiles[i] + " ");
			}
		}
		
		System.out.println();
		System.out.println("My Score: " + myScore + ", Opponent's Score: " + yourScore);
		System.out.println();
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
	
	
	private int evaluation(Position pos_current, int pos_choose){
		//difference of the taken seeds from capture
		int seedsCaptured = 0;
		int sumRemaining = 0;
		Position pos_next = new Position();
		pos_next = sowForCal(pos_current,pos_choose); // just sow 
		int lastChanged = pos_next.getLastChanged();
		
		if(pos_current.getIsMyTurn() ){ // Capture the opponent's seeds
			while(pos_next.getPiles()[lastChanged] >= 2 && pos_next.getPiles()[lastChanged] <= 3 && lastChanged >= pos_next.getPiles().length/2){
				//continue as long as we are on the opponents side and the number of seeds is 2 or 3
				seedsCaptured += pos_next.getPiles()[lastChanged]; 	//didn't consider capture all the seeds
				lastChanged--;
			}
			for(int i = pos_next.getPiles().length/2; i < pos_next.getPiles().length; i++){
				sumRemaining += pos_next.getPiles()[i];
			}
			
			if(sumRemaining == 0){ // shouldn't capture
				seedsCaptured = 0;	
			}
			
		}else if (!pos_current.getIsMyTurn()){ // Capture my seeds
			//the turn of the user input
			while(pos_next.getPiles()[lastChanged] >= 2 && pos_next.getPiles()[lastChanged] <= 3 && lastChanged >= 0 && lastChanged<pos_next.getPiles().length/2){
				//continue as long as we are on the opponents side and the number of seeds is 2 or 3
				seedsCaptured += pos_next.getPiles()[lastChanged];
				lastChanged--;				
			}	
			for(int i = 0; i < pos_next.getPiles().length/2; i++){
				sumRemaining += pos_next.getPiles()[i];
			}
			if(sumRemaining == 0){ // shouldn't capture
				seedsCaptured = 0;
			}	
		}else {
			seedsCaptured = 0;
		}

		return seedsCaptured;
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
			
			
			
		}else return false;
	}
	
	private Position playMove(Position pos_current,int pos_choose){
		Position pos_next = new Position();
		if(finalPosition(pos_current) == -1){ //if not final position // doesn't do more valid check
			pos_next = sowForCal(pos_current,pos_choose);
			
			int[] tmpPiles = new int[currentPiles.length];
			int sumRemaining = 0;
			
			// Do capture and obtain the new pos_next!
			int seedsCaptured = 0;
			int lastChanged = pos_next.getLastChanged();
			if(pos_current.getIsMyTurn()){ // Capture the opponent's seeds
				while(pos_next.getPiles()[lastChanged] >= 2 && pos_next.getPiles()[lastChanged] <= 3 && lastChanged >= pos_next.getPiles().length/2){
					//continue as long as we are on the opponents side and the number of seeds is 2 or 3
					seedsCaptured += pos_next.getPiles()[lastChanged]; 	//didn't consider capture all the seeds
					tmpPiles[lastChanged] = 0;
					lastChanged--;
				}
				for(int i = tmpPiles.length/2; i < tmpPiles.length; i++){
					sumRemaining += tmpPiles[i];
				}
				if(sumRemaining ==0){//if sumRemaining == 0, shouldn't capture
					pos_next.setMyScore(pos_current.getMyScore());			
				}else{ //can capture 
					pos_next.setPiles(tmpPiles);
					pos_next.setMyScore(seedsCaptured + pos_current.getMyScore());
				}
				pos_next.setYourScore(pos_current.getYourScore());
			}else{ // Capture my seeds
				//the turn of the user input
				while(lastChanged >= 0 && lastChanged<pos_next.getPiles().length/2 && pos_next.getPiles()[lastChanged] >= 2 && pos_next.getPiles()[lastChanged] <= 3){
					//continue as long as we are on the opponents side and the number of seeds is 2 or 3
					seedsCaptured += pos_next.getPiles()[lastChanged];
					tmpPiles[lastChanged] = 0;
					lastChanged--;				
				}	
				for(int i = 0; i < tmpPiles.length/2; i++){
					sumRemaining += tmpPiles[i];
				}
				if(sumRemaining ==0){//if sumRemaining == 0, shouldn't capture
					pos_next.setYourScore(pos_current.getYourScore());
				}else{ //can capture 
					pos_next.setPiles(tmpPiles);
					pos_next.setYourScore(seedsCaptured + pos_current.getYourScore());
				}
				pos_next.setMyScore(pos_current.getMyScore());	
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
	    	valuePosition[1] = -1;
	    	return valuePosition;
	    }
	    if (finalPosition(pos_current) == -96){
	    	valuePosition[0] = -96;
	    	valuePosition[1] = -1;
	    	return valuePosition;
	    }
	    if (finalPosition(pos_current) == 0){
	    	valuePosition[0] = 0;
	    	valuePosition[1] = -1;
	    	return valuePosition;
	    }
	    
	    if (depth == depthMax) {
	    	// current is my turn
	    	int[] results = new int[pos_current.getPiles().length/2];
	    	if(pos_current.getIsMyTurn()){ 
	    		int result = evaluation(pos_current, 0);
	    		
	    		if(depthMax%2 == 0){ //bottom is also my turn // choose the max 
	    			for(int i = 0; i < pos_current.getPiles().length/2; i++){
	    				results[i] = evaluation(pos_current, i);
			    		result = Math.max(evaluation(pos_current, i),result);
			    	}
	    		}else{ ////bottom is not my turn // choose the min
	    			for(int i = 0; i < pos_current.getPiles().length/2; i++){
	    				results[i] = evaluation(pos_current, i);
			    		result = Math.min(evaluation(pos_current, i),result);
			    	}
	    		}
	    		for(int i=0;i<results.length;i++){
	    			if(result == results[i]){
	    				valuePosition[1] = i;
	    			}
	    		}
	    		valuePosition[0] = result;    		
		    	return valuePosition;

	    	}else{// current is not my turn    		
	    		int result = evaluation(pos_current, pos_current.getPiles().length/2);
	    		if(depthMax%2 == 0){ //bottom is also not my turn // choose the min 
	    			for(int i = pos_current.getPiles().length/2; i < pos_current.getPiles().length; i++){
	    				results[i] = evaluation(pos_current, i);
			    		result = Math.min(evaluation(pos_current, i),result);
			    	}
	    		}else{ //bottom is my turn // choose the max
	    			for(int i = pos_current.getPiles().length/2; i < pos_current.getPiles().length; i++){
	    				results[i] = evaluation(pos_current, i);
			    		result = Math.max(evaluation(pos_current, i),result);
			    	}
	    		}
	    		for(int i=0;i<results.length;i++){
	    			if(result == results[i]){
	    				valuePosition[1] = i;
	    			}
	    		}
	    		valuePosition[0] = result;
		    	return valuePosition;
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
	    	} else { // move is not valid
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
		for(int i=11;i>=0;i--){
			if(res==tab_values[i]){
				valuePosition[1] = i;
			}
		}
		return valuePosition;
	}
	
}
