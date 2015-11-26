package oware;

public class testGame {
	int result;
	int won = 0;
	int lost = 0;
	int draw = 0;
	
	public void runTest(int amount){
		for(int i = 0; i < amount; i++){
			runSingleTest();
		}
		int total = won + lost + draw;
		double percentage = won / total * 100;
		System.out.println();
		System.out.println("Total amount of games: " + total);
		System.out.println("Amount won: " + won);
		System.out.println("Amount lost: " + lost);
		System.out.println("Amount draw: " + draw);
		System.out.println("Percentage won: " + percentage + "%");
		
	}
	
	public void runSingleTest(){
		GameSander game = new GameSander();
		game.testModeOn();
		game.newGame();
		result = game.report();
		if(result == 1){
			won++;
		}else if(result == 0){
			draw++;
		}else{
			lost++;
		}
	}
	
	public static void main(String[] args) {
		testGame test = new testGame();
		test.runTest(50);
	}
}