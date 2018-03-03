import java.util.Vector;


public class Move implements Comparable{
	Position from;
	Position to;
	int score;
	int captureh;
	
	public Move(){}
	
	public Move(Position from, Position to){
		this.from = from;
		this.to = to;
		this.score = 0;
		this.captureh = 0;
	}
	
	@Override
	public boolean equals(Object o){
		Move move = (Move) o;
		if(from.y == move.from.y && from.x == move.from.x && to.y == move.to.y && to.x == move.to.x){
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		Move moveb = (Move)	arg0;
		if(this.captureh > moveb.captureh)
			return -1;
		else if(this.captureh == moveb.captureh){
			if(this.score > moveb.score)
				return -1;
			else if(this.score == moveb.score)
				return 0;
			else
				return 1;
		}
		else
			return 1;
	}
	
	public static void main(String args[]){
		Position f = new Position(2,2);
		Position t = new Position(3,3);
		Move a = new Move(f,t);
		Move b = new Move(f,t);
		Vector<Move> test = new Vector<Move>();
		test.add(a);
		if (test.contains(a)){
			System.out.println("contains works");
		}else{
			System.out.println("keep fixing");
		}
	}


	
}
