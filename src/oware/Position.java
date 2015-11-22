package oware;

public class Position {
	int[] _piles = new int[24];
	int _myScore = 0; // My score, initiated with 0
	int _yourScore = 0; // The score of the opponent, initiated with 0 
	boolean _isMyTurn; //Next move is my turn or not
	
	public int[] getPiles(){
		return _piles;
	}
	public void setPiles(int position, int value){
		_piles[position] = value;
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
		return _isMyTurn;
	}
	public void setIsMyTurn(boolean isMyTurn){
		_isMyTurn = isMyTurn;
	}
}	
