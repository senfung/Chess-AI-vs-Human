import java.util.Scanner;

public class ChessGame {
	
	private static Scanner inputScanner;
	
	public static void main(String[] args){
		ChessBoard gameboard = new ChessBoard();	
		ComputerAI computer = new ComputerAI();
		
		Position from = new Position();
		Position to = new Position();
		String frominput = null;
		String toinput = null;
		
		inputScanner = new Scanner(System.in);
		
		System.out.println("Welcome to Chess!");
		System.out.println("You are white.");
		
		while(true){
			if(gameboard.whitewin){
				System.out.println("Wait... Congratulations, you won!");
				break;
			}else if(gameboard.blackwin){
				gameboard.print();
				System.out.println("Opps! Sorry you lose.");
				break;
			}
			
			gameboard.print();
			Move humanmove = null;
			
			inputloop: while(true){
				System.out.println("Your turn. Please input as: row,column (e.g. 3,4) ");
				System.out.println("From:");
				frominput = inputScanner.next();
				from.y = frominput.charAt(0) -'0';
				from.x = frominput.charAt(2) -'0';
				System.out.println("To:");
				toinput = inputScanner.next();
				to.y = toinput.charAt(0) - '0';
				to.x = toinput.charAt(2) - '0';
				humanmove = new Move(from, to);
				if(gameboard.checklegalmove(humanmove)){
					break inputloop;
				}else{
					System.out.println("Invalid Move.");
				}
			}
			gameboard.act(humanmove, true);
			System.out.println("Computer's turn.");
			computer.AlphaBetaPruning(gameboard, -2000, 2000);
			gameboard.act(computer.choosenmove,false);
			System.out.println("Computer moves piece from " + computer.choosenmove.from.y + "," + computer.choosenmove.from.x + " to "+ computer.choosenmove.to.y + "," + computer.choosenmove.to.x);
		}
	}
}
