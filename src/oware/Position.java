package oware;

public class Position {
	int[] _piles = new int[24];
	int _myScore = 0; // My score, initiated with 0
	int _yourScore = 0; // The score of the opponent, initiated with 0 
	boolean isMyTurn; //Next move is my turn or not
	
	public int[] getPiles(){
		return _piles;
	}
	public void setPiles(int[] piles){
		_piles = piles;
	}
	public int getMyScore(){
		return _myScore;
	}
	public void setMyScore(int myScore){
		_myScore = myScore;
	}
	public int getYourScore(){
		return _yourScore;
	}
	public void setYourScore(int yourScore){
		_yourScore = yourScore;
	}
	public boolean getIsMyTurn(){
		return isMyTurn;
	}
	
}	
