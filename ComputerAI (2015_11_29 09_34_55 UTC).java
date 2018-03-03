
public class ComputerAI {
	int maxdep = 5;
	boolean capture = false;
	
	Move choosenmove = new Move();
		
	public int Max(ChessBoard board, int a, int b, int depth){
		int value = -2000;
		
		if (depth == 0){
			return board.EvaluationFx(false);
		}
		
		board.createMoveList(false);
		
		for (Move next: board.moves){
			ChessBoard newboard = new ChessBoard(board.estimatemove(next, false));
			value = Math.max(Min(newboard, a, b, depth-1),value);
			if (value >= b){return value;}
			
			if(value > a){
				a = value;
				if (depth  == maxdep)choosenmove = next;
			}
		}
		board.moves.clear();
		return value;
	}
	
	public int Min(ChessBoard board, int a, int b, int depth){
		int value = 2000;
		
		if (depth == 0){
			return board.EvaluationFx(true);
		}
				
		board.createMoveList(true);
		for (Move next: board.moves){
			ChessBoard newboard = new ChessBoard(board.estimatemove(next, false));
			value = Math.min(Max(newboard, a, b, depth-1),value);
			if(value <= a){return value;}
			
			if(value < b){b = value;}
		}
		board.moves.clear();
		return value;
	}
	
	public int AlphaBetaPruning(ChessBoard board, int a, int b){
		int value;
		value = Max(board, a ,b ,maxdep);
		return value;
	}

	
}
