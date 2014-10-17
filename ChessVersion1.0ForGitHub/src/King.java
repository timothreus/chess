public class King extends Piece{
	
	public King( String colour, int column, int row) {
		super(colour,column,row);
		
	}
	
	//a method to for castling to check that there are no pieces between the King and the Rook are 
	//unoccupied
	private boolean intermediateSquaresUnoccupied(Model model, int startX,int startY,int endX,int endY) {
	
	/*checks for straight lines, not diagonal lines
	 * 
	 * all valid calls
	 * 
	 * first establish if checking vertically or horizontally
	 * 
	 * (startX,startY) is kings starting position so is 
	 * 		(4,0) for black King
	 * 		(4,7) for white King
	 * 
	 * if (startX,startY) is (4,0)
	 * 		valid (endX,endY) is (0,0) or (7,0)
	 * 		if (endX,endY) is (0,0)
	 * 			check (1,0),(2,0),(3,0)
	 * 
	 * 		if (endX,endY) is (7,0)
	 * 			check (5,0),(6,0)
	 * 
	 * if (startX,startY) is (4,7)
	 * 		valid (endX,endY) is (0,7) or (7,7)	 * 
	 * 
	 * 
	 * 		if (endX,endY) is (0,7)
	 * 			check (1,7),(2,7),(3,7)
	 * 
	 * 		if (endX,endY) is (7,0)
	 * 			check (5,7),(6,7)	
	 */
	
	  if (  endX == 0 ) {
		  if ( model.pieceAt( 1, endY) == null &&  model.pieceAt( 2, endY) == null && model.pieceAt( 1, endY) == null  )
			  return true;
	
		  
		  else //endX must equal 7 
			  return false;
	  }
	  else { //endX equals 7
		  if ( model.pieceAt( 5, endY) == null &&  model.pieceAt( 6, endY) == null )
			  return true; 
		  else //endX must equal 7 
			  return false;
	  }
		
	}
	
	
	
	private boolean notPassingThroughCheck(Model model, int startX,int startY,int rookX,int rookY) {
		int endX=-1;
		int endY=-1;
		if (startY == rookY) 
			endY = startY;
		if (rookX == 0)
			endX =2;
		else
			endX = 6;
		if (endX == 2) 
			if ( inCheck(model,4,endY) || inCheck(model,3,endY) || inCheck(model,2,endY) ) 
				return false;
			else
				return true;
			
		if (endX == 6) 
			if ( inCheck(model,4,endY) || inCheck(model,5,endY) || inCheck(model,6,endY) ) 
				return false;
			else
				return true;

		return true;
		
	}
	private boolean inCheck(Model model, int kingX,int kingY) {
		
		Piece[][] pieces = model.getPieces();
		String opponentColour="";
		
		if ( getColour() == "black" )
			opponentColour = "white";
		else if ( getColour() == "white" )
			opponentColour = "black";
		
		for (int x = 0; x<8; x++) 
			for (int y=0; y<8; y++) 
				if (pieces[x][y] != null && pieces[x][y].getColour().equals( opponentColour )  ) 
					//if piece can move to currentPlayer's King
					if ( pieces[x][y].canMoveTo(model,kingX,kingY)) 
						return true;				
		return false;
	}
	
	
	public boolean validCastleMove(Model model, int col, int row ) {
		
		Piece piece;
		int kingX = this.getColumn();
		int kingY = this.getRow();
		int rookX=-1;
		int rookY=-1;
		
		if (kingX == 4 && kingY == 7) {
			if (col == 2 && row ==7) {
				rookX = 0;
				rookY = 7;
			}
			else if (col == 6 && row ==7){
				rookX = 7;
				rookY = 7;
			}
				
		}
		else if (kingX == 4 && kingY == 0) {
			if (col == 2 && row ==0) {
				rookX = 0;
				rookY = 0;
			}
			else if (col == 6 && row ==0){
				rookX = 7;
				rookY = 0;
			}
				
		}
		
		//if destination has a rook of king's colour which has never moved (important else could be a promoted piece of the opponent's!)
		
		if ( rookX >= 0 && rookY >=0 && model.pieceAt(rookX,rookY) != null )
			piece = model.pieceAt(rookX,rookY);
		else
			return false;
		
		if ( piece instanceof Rook && piece.getHasMoved()==false) {
			//if king has never moved
			if (hasMoved == false) {
							
					//if intermediate square are unoccupied
					
					if ( intermediateSquaresUnoccupied(model, kingX,kingY, rookX,rookY) ) {
						
						if ( notPassingThroughCheck(model, kingX,kingY, rookX,rookY) ) {
						
							return true;
						}
						
					}
				 
			}

		}
		
		return false;
	}
	
	public boolean canMoveTo(Model model, int col, int row ) {
		
		Piece[][] pieces = model.getPieces();
		
		//first check the move isn't to the current position (eg a1 to a1!)
		
		if (this.getRow() == row && this.getColumn() == col) {
			
			return false;
		}
		
		if ( 	
				//check difference between row and this.getRow() is either 0 or 1. Same for col/this.col
				
				(
				(( Math.abs( col - this.getColumn()) == 1)  || ( Math.abs( col - this.getColumn()) == 0) )
				
				&&
				
				(( Math.abs( row - this.getRow()) == 1)  || ( Math.abs( row - this.getRow()) == 0) )
				)
			
			    ||
			    
			    validCastleMove(model, col,row)
				
			)	{
			
			
			if ( (pieces[col][row] != null) && ( pieces[col][row].getColour().equals( this.getColour() ) ) ) {
				
				return false;
			}
			else
				if ( moveWouldCauseCheck( model, col,row ) )  //IN PIECE CLASS 
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
		if (this.getRow() == row && this.getColumn() == col)
			return false;
		
		if ( 	
				//check difference between row and this.getRow() is either 0 or 1. Same for col/this.col
				(( Math.abs( col - this.getColumn()) == 1)  || ( Math.abs( col - this.getColumn()) == 0) )
				
				&&
				
				(( Math.abs( row - this.getRow()) == 1)  || ( Math.abs( row - this.getRow()) == 0) )

				
			)	{
			
			
			if ( (pieces[col][row] != null) && ( pieces[col][row].getColour().equals( this.getColour() ) ) )
				return false;
			else 
				return true;
		}	
		else
			return false;
	}
	
	
	public String toString() {
		return colour + " king";
	}

}
