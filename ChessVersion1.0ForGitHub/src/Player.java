public abstract class Player {

	String name;
	String colour;
	
	public Player( String name, String colour) {
		this.name = name;
		this.colour = colour;
	}
	
	public boolean isValidMove( Model model,int col1, int row1, int col2, int row2) {
		Piece piece = model.pieceAt(col1,row1);
		
		if ( piece.canMoveTo(model,col2,row2) ) 
			return true;
		else 
			return false;
	}
	
	public String getName() {
		return name;
	}
	
	public String getColour() {
		return colour;
	}
}
