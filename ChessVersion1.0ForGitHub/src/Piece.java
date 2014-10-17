public abstract class Piece {

	String colour;
	boolean hasMoved;
	private int column;
	private int row;
	
	public Piece( ) {

	}
	
	public Piece( String colour, int column, int row) {
		hasMoved = false;
		this.colour = colour;
		this.column = column;
		this.row = row;
	}
	
	public void setHasMoved (boolean value) {
		hasMoved = value;
	}
	
	public boolean getHasMoved() {
		return hasMoved;
	}
	
	public boolean hasLegalMoves(Model model) {
		
	
		for (int x = 0; x<8; x++) {
			for (int y = 0; y<8; y++) {
				
				if ( canMoveTo(model,x,y) 	) {
					return true;
				}
					
				
			}
		}
		return false;
	}
	
	public abstract boolean canMoveTo(Model model, int col, int row );
	
	public abstract boolean canCapture(Model model, int col, int row );
	
	
	public String getColour() {
		return colour;
	}
	
	
	//following method will check if making a move will put the move maker (not the opponent) in check
	//called from subclasses of this one.
	public boolean moveWouldCauseCheck(Model model, int newCol, int newRow) {	
		//make a new model (cloned from the parameter version)	
		Model clonedModel = model.cloneModel();
		
		//the first parameter was previously this which caused no end of probelms!!!
		clonedModel.movePiece ( clonedModel.pieceAt(this.column,this.row), this.column,  this.row,  newCol,  newRow );
	
		Piece[][] clonedPieces = clonedModel.getPieces();
		
		String opponentColour;
		
		if ( colour.equals("white") )
			opponentColour = "black";
		else
			opponentColour = "white";
		
		int kingX=-1;
		int kingY=-1;
		
		for (int x = 0; x<8; x++) 
			for (int y=0; y<8; y++) 
				if ( 
						clonedPieces[x][y] !=null
						&&
						
						clonedPieces[x][y].getColour().equals(colour)
						&&
						//is of type king
						(clonedPieces[x][y] instanceof King) 
						
					) {
					kingX = x;
					kingY = y;
				}
		
		//for every piece that is of opponentColour, see if it can "take" the King of colour.
		
		//loop through each possible opponent piece {

		for (int x = 0; x<8; x++) 
			for (int y=0; y<8; y++) 
				if (clonedPieces[x][y] != null && clonedPieces[x][y].getColour().equals( opponentColour )  ) {
					//if piece can move to currentPlayer's King
					if ( clonedPieces[x][y].canCapture(clonedModel,kingX,kingY)) 
						return true;						
				}						
		return false;
	}
	
	public void setCol(int col) {
		
		this.column = col;
		
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	
	
	/*
	public void compareModels(Model model1, Model model2) {
		for (int column = 0; column<8; column++)
			for (int row = 0; row<8; row++) {
				if (model1.pieceAt(column, row) !=null) {
					
				
					JOptionPane.showMessageDialog(null,"original model column " + column + " and " +row+" is " + model1.pieceAt(column, row),"debuggin",JOptionPane.PLAIN_MESSAGE);
				}
				
				if (model2.pieceAt(column, row) !=null) {
					JOptionPane.showMessageDialog(null,"cloned model column " + column + " and " +row+" is " + model2.pieceAt(column, row),"debuggin",JOptionPane.PLAIN_MESSAGE);
				}
			}
				
	}
	*/
	
}
