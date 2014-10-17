public class Move {
	private Piece piece;
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	public Move(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
	}
	
	public Move() {
		x1 = -1;
		y1 = -1;
		x2 = -1;
		y2 = -1;
		
	}
	

	
	public void setMove( Piece piece, int oldX, int oldY, int newX, int newY ) {
		this.piece = piece;
		x1 = oldX;
		y1 = oldY;
		x2 = newX;
		y2 = newY;
	}
	
	public int getOldX() {
		return x1;
	}
	
	public int getNewX() {
		return x2;
	}
	public int getOldY() {
		return y1;
	}
	public int getNewY() {
		return y2;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public String toString() {
		return "("+x1+","+y1+") to ("+x2+","+y2+")";
	}
	
}
