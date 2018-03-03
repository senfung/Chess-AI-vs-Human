import java.util.*;

public class AIDecision {
	int maxdep;
	int count = 0;
	boolean capture = false;
	Move choosenmove = new Move();
	
	public int max(ChessBoard board, int a, int b, int depth){
		if (depth == 0){
			return board.Evalution(false);
		}
		
		
		board.createMoveList(false);
		
		for (Move next: board.moves){
			
			count++;
		}
		
		return 0;
	}

	
}
