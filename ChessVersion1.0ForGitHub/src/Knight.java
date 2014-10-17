import java.lang.Math;
public class Knight extends Piece {

	public Knight( String colour, int column, int row) {
		super(colour,column,row);
	}
	
	
	public boolean canMoveTo(Model model, int col, int row ) {
		
		//consulted http://stanford.edu/class/archive/cs/cs106a/cs106a.1136/handouts/36-assignment-5.pdf
		//if the space at (row,col) is two spaces in one direction and one space in the other direction from (this.getRow(), this.col)
		Piece[][] pieces = model.getPieces();
		
		//check difference between row and this.getRow() is 1 and difference between col and this.col is 2 or
		//difference between row and this.getRow() is 2 and difference between col and this.col is 1
		if  ( 	( Math.abs(this.getRow() - row) == 1 && Math.abs(this.getColumn() - col) ==2 ) ||
				( Math.abs(this.getRow() - row) == 2 && Math.abs(this.getColumn() - col) ==1 ) ) {
			
		
			//if the space at (row,col) is occupied by a piece with the same color as this.color return false;
			
			if ( (pieces[col][row] != null) && ( pieces[col][row].getColour().equals( this.getColour() ) ) ) 
				return false;
			else
				//check to see whether or not this move would cause the player to move him/herself into check using moveWouldCauseCheck
				//if this would cause check
				
				if ( moveWouldCauseCheck( model, col,row ) ) //IN PIECE CLASS
					return false;	
				else 					
					return true;
		}
		else 
			return false;
	}
	
	
	public boolean canCapture(Model model, int col, int row ) {
		Piece[][] pieces = model.getPieces();
		
		//first check the move isn't to the current position (eg a1 to a1!)
		if (this.getRow() == row && this.getColumn() == col) {
			return false;
		}
		
		//check difference between row and this.getRow() is 1 and difference between col and this.col is 2 or
		//difference between row and this.getRow() is 2 and difference between col and this.col is 1
		if  ( 	( Math.abs(this.getRow() - row) == 1 && Math.abs(this.getColumn() - col) ==2 ) ||
				( Math.abs(this.getRow() - row) == 2 && Math.abs(this.getColumn() - col) ==1 ) ) {
			
		
			if ( (pieces[col][row] != null) && ( pieces[col][row].getColour().equals( this.getColour() ) ) ) 
				return false;
			else 
				return true;
		}
		else 
			return false;
	}
	
	

	public String toString() {
		return colour + " Knight";
	}
	
	
}
