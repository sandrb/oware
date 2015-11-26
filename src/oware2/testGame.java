package oware2;

public class testGame {
	
	public void runTest(){
		GameSander game = new GameSander();
		game.testModeOn();
		game.newGame();
	}
	
	public static void main(String[] args) {
		testGame test = new testGame();
		test.runTest();
	}
}