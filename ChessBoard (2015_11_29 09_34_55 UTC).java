import java.util.*;

public class ChessBoard {
	final static public int Empty = 0;
    final static public int Pawn = 1;
    final static public int Knight = 2;
    final static public int Bishop = 3;
    final static public int Rook = 4;
    final static public int Queen = 5;
    final static public int King = 6;
    
	int board[][] = new int [8][8];
	
	static char[] piecetoChar = {' ', 'P', 'N', 'B', 'R', 'Q', 'K'};
	static int[] victimValue = { 0, 1, 3, 3, 5, 9, 200};
	
	ArrayList<Move> possmove = new ArrayList<Move>();
	ArrayList<Move> posscapture = new ArrayList<Move>();
	ArrayList<Move> moves = new ArrayList<Move>();
	
	static Position[] knightmove = {new Position(1,2),new Position(2,1), new Position(1,-2),new Position(-2,1), new Position(-1,2), new Position(2,-1), new Position(-1,-2), new Position(-2,-1)};
	static Position[] bishopmove = {new Position(1,1),new Position(1,-1),new Position(-1,1),new Position(-1,-1)};
	static Position[] rookmove = {new Position(1,0),new Position(0,1),new Position(-1,0),new Position(0,-1)};
	static Position[] queenmove = {new Position(1,0),new Position(0,1),new Position(-1,0),new Position(0,-1),new Position(1,1),new Position(1,-1),new Position(-1,1),new Position(-1,-1)};
	static Position[] kingmove = {new Position(1,0),new Position(0,1),new Position(-1,0),new Position(0,-1),new Position(1,1),new Position(1,-1),new Position(-1,1),new Position(-1,-1)};
	
	boolean whitewin = false;
	boolean blackwin = false;
	
	public ChessBoard(){//initiate chessboard
		board[0][0] = 4;
		board[0][1] = 2;
		board[0][2] = 3;
		board[0][3] = 5;
		board[0][4] = 6;
		board[0][5] = 3;
		board[0][6] = 2;
		board[0][7] = 4;
		for(int i = 0; i < 8; i++){ board[1][i] = 1; }
		for(int i = 0; i < 8; i++){ board[6][i] = -1; }
		board[7][0] = -4;
		board[7][1] = -2;
		board[7][2] = -3;
		board[7][3] = -5;
		board[7][4] = -6;
		board[7][5] = -3;
		board[7][6] = -2;
		board[7][7] = -4;
	}
	
	public ChessBoard(ChessBoard old){
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				board[i][j] = old.board[i][j];
			}
		}
	}
	
	public void print(){ //print chessboard
		char piece;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j] > 0){
					piece = piecetoChar[board[i][j]];
					System.out.print("[" + piece + "]");
				}else if(board[i][j] < 0){
					piece = Character.toLowerCase(piecetoChar[(-1)*board[i][j]]);
					System.out.print("[" + piece + "]");
				}else{
					piece = piecetoChar[0];
					System.out.print("[" + piece + "]");
				}
			}
			System.out.println(" " + i);
		}
		for(int i = 0; i < 8; i++){
			System.out.print(" " + i + " ");
		}
		System.out.println(" ");
		System.out.println(" ");
	}
	

	public void createMoveList(boolean player){
		//find all new moves
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j] == 0){ 
					continue;
				}else{
					if((board[i][j] < 0 && player)||(board[i][j] > 0 && !player)){
						createMoves(new Position(i,j), player);
					}
				}
			}
		}
		
		//sort out best capturing moves
		posscapture.sort(
			new Comparator<Move>(){
				public int compare(Move m1, Move m2){
					if(m1.captureh == m2.captureh){
						return 0;
					}else if(m1.captureh > m2.captureh){
						return -1;
					}else{
						return 1;
					}
				}
			}
		);
		
		moves.addAll(posscapture);
		moves.addAll(possmove);
		posscapture.clear();
		possmove.clear();
		
	}
	
	public void createMoves(Position position, boolean player){
		int x = position.x;
		int y = position.y;
		int piece = board[y][x]; // x and y might be mixed up
		int side;
		Position nextPos = new Position();
		
		if (piece < 0){
			piece = -piece;
			side = -1;
		}else{
			side = 1;
		}
		
		if(piece == Pawn){
			//forward by one grid
			nextPos = new Position();
			nextPos.x = x;
			nextPos.y = y + side;
			if (inrange(nextPos)){
				if (board[nextPos.y][nextPos.x] == 0){
					Move move = new Move(position, nextPos);
					possmove.add(move);
				}
			}						
			//forward by two grid
			nextPos = new Position();
			nextPos.x = x;
			nextPos.y = y + 2*side;
			if (inrange(nextPos)){
				if (board[nextPos.y][nextPos.x] == 0 && board[y+side][x] == 0 ){
					if(position.y == 1 || position.y == 6){
						Move move = new Move(position, nextPos);
						possmove.add(move);
					}
				}
			}			
			//capture left   CAUTION ON negative sign	
			nextPos = new Position();
			nextPos.x = x + 1;
			nextPos.y = y + side;
			if (inrange(nextPos)){
				int capturedpiece = board[nextPos.y][nextPos.x];
				if(piece*capturedpiece*side<0){
					Move move = new Move(position, nextPos);
					if(capturedpiece < 0){
						move.captureh = victimValue[-capturedpiece] - victimValue[piece];
					}
					posscapture.add(move);
				}
			}
						
			//capture right
			nextPos = new Position();
			nextPos.x = x - 1;
			nextPos.y = y + side;
			if (inrange(nextPos)){
				int capturedpiece = board[nextPos.y][nextPos.x];
				if(piece*capturedpiece*side<0){
					Move move = new Move(position, nextPos);
					if(capturedpiece < 0){
						move.captureh = victimValue[-capturedpiece] - victimValue[piece];
					}
					posscapture.add(move);
				}
			}
			
		}else{
			if(piece == Knight){
				for(int i = 0; i < 8; i++){
					nextPos = new Position();
					nextPos.x = x + knightmove[i].x;
					nextPos.y= y + knightmove[i].y;
					if (!inrange(nextPos)){
						continue;
					}
					int destination = board[nextPos.y][nextPos.x];

					if (destination*side>0){
						continue;
					}
					
					Move move = new Move(position, nextPos); 
					
					if (destination == 0){
						possmove.add(move);
					}else{
						if(destination < 0)
							destination = -destination;
						move.captureh = victimValue[destination]-victimValue[piece];
						posscapture.add(move);
					}
					
				}
			}else if(piece == Bishop){
				for(int i = 0; i < 4; i++){
					int j = 1;
					bchangedirection: while(true){
						nextPos = new Position();
						nextPos.x = x + j*bishopmove[i].x;
						nextPos.y = y + j*bishopmove[i].y;
						if (!inrange(nextPos)){
							break bchangedirection;
						}
						int destination = board[nextPos.y][nextPos.x];
						if (destination*side>0){
							break bchangedirection;
						}
						
						Move move = new Move(position, nextPos); 
						
						if (destination == 0){
							possmove.add(move);
						}else{
							if(destination < 0)
								destination = -destination;
							move.captureh = victimValue[destination]-victimValue[piece];
							posscapture.add(move);
							break bchangedirection;
						}
						j++;
					}
				}
			}else if(piece == Rook){
				for(int i = 0; i < 4; i++){
					int j = 1;
					rchangedirection: while(true){
						nextPos = new Position();
						nextPos.x = x + j*rookmove[i].x;
						nextPos.y = y + j*rookmove[i].y;
						
						if (!inrange(nextPos)){
							break rchangedirection;
						}
						
						int destination = board[nextPos.y][nextPos.x];
						
						if (destination*side>0){
							break rchangedirection;
						}
						
						Move move = new Move(position, nextPos); 
						
						if (destination == 0){
							possmove.add(move);
						}else{
							if(destination < 0)
								destination = -destination;
							move.captureh = victimValue[destination]-victimValue[piece];
							posscapture.add(move);
							break rchangedirection;
						}
						j++;
					}
				}
			}else if(piece == Queen){
				for(int i = 0; i < 8; i++){
					int j = 1;
					qchangedirection: while(true){
						nextPos = new Position();
						nextPos.x = x + j*queenmove[i].x;
						nextPos.y = y + j*queenmove[i].y;
						if (!inrange(nextPos)){
							break qchangedirection;
						}
						int destination = board[nextPos.y][nextPos.x];
						if (destination*side>0){
							break qchangedirection;
						}
						
						Move move = new Move(position, nextPos); 
						
						if (destination == 0){
							possmove.add(move);
						}else{
							if(destination < 0)
								destination = -destination;
							move.captureh = victimValue[destination]-victimValue[piece];
							posscapture.add(move);
							break qchangedirection;
						}
						j++;
					}
				}
			}else if (piece == King){
				for(int i = 0; i < 8; i++){
					nextPos = new Position();
					nextPos.x = x + kingmove[i].x;
					nextPos.y= y + kingmove[i].y;
					if (!inrange(nextPos)){
						continue;
					}
					int destination = board[nextPos.y][nextPos.x];
					if (destination*side>0){
						continue;
					}
					
					Move move = new Move(position, nextPos); 
					
					if (destination == 0){
						possmove.add(move);
					}else{
						if(destination < 0)
							destination = -destination;
						move.captureh = victimValue[destination]-victimValue[piece];
						posscapture.add(move);
					}
					
				}
			}						
		}		
	}
	
	public void act(Move move, boolean player){
		int fromvalue;
		fromvalue = board[move.from.y][move.from.x];
		int tovalue;
		tovalue = board[move.to.y][move.to.x];
		
		//pawn promotion
		if((fromvalue == 1 || fromvalue == -1) && (move.to.y == 0 || move.to.y == 7)){
			if (player){
				board[move.to.y][move.to.x] = -5;
			}else{
				board[move.to.y][move.to.x] = 5;
			}
		}else{
			board[move.to.y][move.to.x] = fromvalue;
		}
		//checkmate
		if(tovalue == 6){
			whitewin = true;
		}else if (tovalue == -6){
			blackwin = true;
		}
		
		board[move.from.y][move.from.x] = 0;
	}
	
	public ChessBoard estimatemove(Move move, boolean player){
		ChessBoard newview = new ChessBoard(this);
		int fromvalue;
		//newview.print();
		fromvalue = newview.board[move.from.y][move.from.x];
		int tovalue;
		tovalue = newview.board[move.to.y][move.to.x];
		//pawn promotion
		if((fromvalue == 1 || fromvalue == -1) && (move.to.y == 0 || move.to.y == 7)){
			if (player){
				newview.board[move.to.y][move.to.x] = -5;
			}else{
				newview.board[move.to.y][move.to.x] = 5;
			}
		}else{
			newview.board[move.to.y][move.to.x] = newview.board[move.from.y][move.from.x];
		}
//		//checkmate
//		if(tovalue == 6){
//			whitewin = true;
//		}else if (tovalue == -6){
//			blackwin = true;
//		}
		newview.board[move.from.y][move.from.x] = 0;
		return newview;
	}
	
	public int EvaluationFx(boolean player){
		int value = 0;

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if (board[i][j] == 0){
					continue;
				}else{
					int piece = board[i][j];
					if(piece > 0){
						value += victimValue[piece];
					}else{
						piece = -piece;
						value -= victimValue[piece];
					}
				}
			}
		}
		return value;
	}
	
	public boolean inrange(Position pos){
		if(pos.y < 8 && pos.y >= 0 && pos.x < 8 && pos.x >= 0){
			return true;
		}
		return false;
	}
	
	public boolean checklegalmove(Move move){
		if (inrange(move.from) && inrange(move.to)){
			if(board[move.from.y][move.from.x] < 0){
				createMoves(move.from, true);
				if(possmove.contains(move) || posscapture.contains(move)){
					System.out.println("Move valid.");
					possmove.clear();
					posscapture.clear();
					return true;
				}
			}
		}
		possmove.clear();
		posscapture.clear();
		return false;
	}
	
	public static void main(String args[]){
		ChessBoard game  = new ChessBoard();
		game.print();
	}
}