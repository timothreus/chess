public class Pawn extends Piece{

	public Pawn( String colour, int column, int row) {
		super(colour,column,row);
	}
	
	private boolean validWhiteEnPassant(Model model, int col, int row) {
		Move previousMove = model.getPreviousMove();
		if ( 
				(
				this.getRow() == 3  
				&& 
				row == 2
				&&
				col == this.getColumn()-1  
				&&  
				previousMove.getOldX() == this.getColumn()-1
				&&
				previousMove.getOldY() == 1
				&&
				previousMove.getNewY() == 3
				)
				||	
				(
				this.getRow() == 3  
				&& 
				row == 2
				&&
				col == this.getColumn()+1  
				&&  
				previousMove.getOldX() == this.getColumn()+1
				&&
				previousMove.getOldY() == 1
				&&
				previousMove.getNewY() == 3
				)
		) 
			return true;
		else
			return false;
	}
	
	private boolean validBlackEnPassant(Model model, int col, int row) {
		Move previousMove = model.getPreviousMove();
		if ( 
				(this.getRow() == 4  
				&& 
				row == 5
				&&
				col == this.getColumn()-1  
				&&  
				previousMove.getOldX() == this.getColumn()-1
				&&
				previousMove.getOldY() == 6
				&&
				previousMove.getNewY() == 4)
				
				||
				
				(this.getRow() == 4
				&& 
				row == 5
				&&
				col == this.getColumn()+1  
				&&  
				previousMove.getOldX() == this.getColumn()+1
				&&
				previousMove.getOldY() == 6
				&&
				previousMove.getNewY() == 4)
		) 
			return true;
		else
			return false;
	}
	
	public boolean validWhiteMove(Model model, int col, int row ) {
		Piece[][] pieces = model.getPieces();
		if (
	
				//if pawn is capturing it can move from (c,r) to (c-1,r-1) or (c+1,r-1)
				( 
						pieces[col][row] !=null
						&&
						pieces[col][row].getColour().equals("black") 
						&&
						(
							( (col == this.getColumn() -1) && (row == this.getRow() - 1) ) ||	( (col == this.getColumn() + 1) && (row == this.getRow() - 1)) 	
						)
				)	
				||
				//else if it is on row 6 of column c it can move to either rows 4 or 5.
				//(c,6) -> (c,5) or (c,4)
				(
						(col==this.getColumn() ) 	&& 	( this.getRow() ==6 && ( (row == 5) ) && (pieces[col][row] ==null ) )		
				)
				
				||
				
				(
						(col==this.getColumn() ) 	&& 	( this.getRow() ==6 && ( (row==4) ) && (pieces[col][row] ==null )&& ( pieces[col][5] ==null  ) )		
				)
				
				||	
				
				//en passant
				validWhiteEnPassant(model, col, row)
				||
				(
				//else it can move from (c,r) to (c,r-1)
						( col == this.getColumn() ) && (row == this.getRow()-1) && (pieces[col][row]==null) 
				)	
			)
				return true;
		else
				return false;
	}
	
	public boolean validBlackMove(Model model, int col, int row ) {
		Piece[][] pieces = model.getPieces();
		if 	(
				//if pawn is capturing it can move from (c,r) to (c-1,r+1) or (c+1,r+1)
				( 
					pieces[col][row] !=null
					&&
					pieces[col][row].getColour().equals("white") 
					&&
					(
						( (col == this.getColumn() -1) && (row == this.getRow() + 1) ) ||	( (col == this.getColumn() + 1) && (row == this.getRow() + 1)) 	
					)
				)					
				||				
				//else if it is on row 1 of column c it can move to either rows 2 or 3.
				//(c,1) -> (c,2) or (c,3)
				
				
				
				
				(
						(col==this.getColumn()) 	&& 	( this.getRow() ==1 && ( (row == 2)  ) && (pieces[col][row]==null) )		
				)
				
				||
				
				(
						(col==this.getColumn()) 	&& 	( this.getRow() ==1 && ( (row==3) ) && (pieces[col][row]==null)&&(pieces[col][2]==null) )		
				)
				
				||
				//en passant
				validBlackEnPassant(model, col, row)
				||			
				
				(
				//it can move from (c,r) to (c,r+1) if destination is null
					(col == this.getColumn()) && (row == this.getRow()+1) && (pieces[col][row]==null) 
				)

			)	
				return true;
		else
				return false;
	}
	
	public boolean canMoveTo(Model model, int col, int row ) {
		Piece[][] pieces = model.getPieces();
		
		//first check the move isn't to the current position (eg a1 to a1!)
		if (this.getRow() == row && this.getColumn() == col) 	
			return false;
		
		if  ( colour.equals("black")  && validBlackMove(model,col,row) || colour.equals("white")  && validWhiteMove(model,col,row) ) {
										
			if ( (pieces[col][row] != null) && ( pieces[col][row].getColour().equals( this.getColour() ) ) ) {
				return false;
			}
			
			else if ( validWhiteEnPassant( model, col, row ) ) {
						Model clonedModel = model.cloneModel();
						clonedModel.removePiece( col,3 );
						if (moveWouldCauseCheck (clonedModel, col, row)) 
							return false;
						else
							return true;
			}
				
			else if ( validBlackEnPassant( model, col, row)) {
						Model clonedModel = model.cloneModel();
						clonedModel.removePiece(col, 4);
						if (moveWouldCauseCheck(clonedModel, col, row)) 
							return false;
						else
							return true;
			}
			else if ( moveWouldCauseCheck( model, col,row ) )  //IN PIECE CLASS
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
		
		if  ( 
				colour.equals("black")  && validBlackMove(model,col,row)  ||  colour.equals("white")  && validWhiteMove(model,col,row)
			) 
										
			if ( (pieces[col][row] != null) && ( pieces[col][row].getColour().equals( this.getColour() ) ) ) 
					return false;
			else	
					return true;
		else 
			return false;
	}	
	
	public String toString() {
		return colour + " pawn";
	}
	
}
