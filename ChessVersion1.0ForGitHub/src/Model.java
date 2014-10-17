import javax.swing.JOptionPane;

public class Model {
	
	private Piece[][] pieces;
	private Move previousMove;
   
	public Model() {
		pieces = new Piece[8][8];
		initialisePieces();
		previousMove=new Move();
	}	
	
	public Model cloneModel() {
		Model clonedModel = new Model();
		
		for (int column = 0; column<8; column++)
			for (int row = 0; row<8; row++) {
				
				if ( pieceAt(column,row) == null)
					clonedModel.pieces[column][row]=null;
				
				else {
					String colour = pieceAt(column,row).getColour();
					if ( pieceAt(column,row) instanceof Pawn)
					
						clonedModel.pieces[column][row]=new Pawn(colour,column,row);
					else if ( pieceAt(column,row) instanceof Rook)
					
						clonedModel.pieces[column][row]=new Rook(colour,column,row);
					else if ( pieceAt(column,row) instanceof Knight)
					
						clonedModel.pieces[column][row]=new Knight(colour,column,row);
					else if ( pieceAt(column,row) instanceof Bishop)
					
						clonedModel.pieces[column][row]=new Bishop(colour,column,row);
					else if ( pieceAt(column,row) instanceof Queen)
					
						clonedModel.pieces[column][row]=new Queen(colour,column,row);
					else if ( pieceAt(column,row) instanceof King)
					
						clonedModel.pieces[column][row]=new King(colour,column,row);
				}
				
			}
		
		return clonedModel;
    }
   
	public void setPreviousMove(Move lastMove) {
		this.previousMove = lastMove;
	}
	
	public Move getPreviousMove() {
		return previousMove;
	}
	
	public void promotePiece(String playerType, int col, int row) {
		
		String colour = pieceAt(col,row).getColour();
		
		if ( playerType.equals("Human") ) {
			//Custom button text
			Object[] options = {"Queen",
								"Bishop",
								"Knight",
		                    	"Rook"};
		
			int n = JOptionPane.showOptionDialog(null,
					"Select Piece", "Promotion",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
		
			switch (n) {
				case 0: 	pieces[col][row]=new Queen(colour,col,row);
							break;
				case 1: 	pieces[col][row]=new Bishop(colour,col,row);
							break;
				case 2: 	pieces[col][row]=new Knight(colour,col,row);
							break;
				case 3: 	pieces[col][row]=new Rook(colour,col,row);
							break;					
				default:
							break;
			}
		}
		else if ( playerType.equals("Computer") ) 
			pieces[col][row]=new Queen(colour,col,row);
	}	

	public void movePiece( Piece piece,int col1, int row1, int col2, int row2 ) {
		
		
		//if there is a piece on the destination square (i.e. a capture is taking place) remove that piece
		if (pieces[col2][row2]!=null)
			
			//HERE WOULD BE THE PLACE TO FIND OUT WHICH PIECE HAS BEEN CAPTURED IF A RECORD OF THAT IS REQUIRED LATER
			removePiece(col2,row2);
		
		//the destination position now holds the piece
		pieces[col2][row2] = piece;
		
		//update the pieces column and row fields 
		piece.setCol(col2);
		piece.setRow(row2);
		
		//remove the piece from its old position
		removePiece(col1,row1);
	}
	
	//this method is called by Controller class
	public void checkForPromotion( String playerType, int col1, int row1, int col2, int row2 ) {
		
		if ( pieceAt(col2,row2) instanceof Pawn  && ( row2==0 || row2 ==7  )      )  
			
			promotePiece(playerType,col2,row2);
		
		
	}
			
	//this method is called by Controller class
	//it checks whether the en passant move has just taken place in order that the captured pawn can be removed
	public void checkForEnPassant ( Piece piece, int col1, int row1, int col2, int row2 ) {

		
		int oldX=-1;
		if (previousMove.getOldX()!= -1)
			oldX = previousMove.getOldX();
		
		int oldY = -1;
		if (previousMove.getOldY()!=-1)
			oldY=previousMove.getOldY();
		
		int newX = -1;
		if(previousMove.getNewX()!=-1)
			newX=previousMove.getNewX();
		
		int newY=-1; 
		if(previousMove.getNewY()!=-1)
			newY=previousMove.getNewY();
		
		Piece previousPiece=null;
		if ( newX>-1 && newY>-1 && pieceAt(newX,newY) != null )
				previousPiece = pieceAt(newX,newY);
		
		if ( checkForWhiteEnPassant( piece, previousPiece, oldX, oldY, newX, newY, col1, row1, col2, row2) ) {
			removePiece(col2,3);
		}	
		else 
			if ( checkForBlackEnPassant( piece, previousPiece, oldX, oldY, newX, newY, col1, row1, col2, row2) ) {
				removePiece(col2,4);
				}	
	}
		
	
	
	
	
	//this method is called by checkForEnPassant method
	//it checks whether a white en passant move has just taken place in order that the captured pawn can be removed	
	public boolean checkForWhiteEnPassant( Piece piece, Piece previousPiece, int oldX, int oldY, int newX, int newY, int col1, int row1, int col2, int row2 ) {
			if (
					piece instanceof Pawn 
					&&
					piece.getColour().equals("white")
					&&
					oldX>-1 && oldY>-1 && newX>-1 && newY>-1
					&&
					previousPiece instanceof Pawn
					&&
					previousPiece.getColour().equals("black")
					&&
					//REPLACE SOME OF THIS WITH A CALL TO validWhiteEnPassant() method in Pawn class?
					((col2 == col1 + 1) || (col2 == col1 - 1))
					&&
					row1 == 3
					&& 
					row2 == 2
					&&
					oldX == col2
					&&
					oldY ==1
					&&
					newX ==col2
					&&
					newY ==3
				)
				return true;
			else
				return false;
		}
		
					
					
					
					
	//this method is called by checkForEnPassant method
	//it checks whether a black en passant move has just taken place in order that the captured pawn can be removed			
	public boolean checkForBlackEnPassant( Piece piece, Piece previousPiece, int oldX, int oldY, int newX, int newY, int col1, int row1, int col2, int row2 ) {
			if (
					piece instanceof Pawn		
					&&
					piece.getColour().equals("black")
					&&
					oldX>-1 && oldY>-1 && newX>-1 && newY>-1
					&&
					previousPiece instanceof Pawn
					&&
					previousPiece.getColour().equals("white")
					&&
					((col2 == col1 + 1) || (col2 == col1 - 1))
					&&
					row1 == 4
					&& 
					row2 == 5
					&&
					row2 == row1 + 1
					&&
					oldX == col2
					&&
					oldY ==6
					&&
					newX ==col2
					&&
					newY ==4	
				) 
				return true;
			else
				return false;
	}
	
	//this method is called by Controller class
	public void checkForCastling ( int col1, int row1, int col2, int row2 ) {
		
	  Piece rook;
		if ( pieceAt(col2,row2) instanceof King  &&  ( Math.abs( col2 - col1) == 2) ) {
			//move rook 
			if (col2 == 2) {
				//move rook from col 0 to col 3
				rook = pieceAt(0,row2);
				movePiece(rook,0,row2,3,row2);
			}
			else if (col2 == 6) {
				//move rook from col 7 to col5
				rook = pieceAt(7,row2);
				movePiece(rook,7,row2,5,row2);
			}
		}
	}

	
	
	public Piece pieceAt( int column, int row) {
		return pieces[column][row];
		
	}
	
	public void setPiece( Piece piece, int column, int row) {
		pieces[column][row]= piece; 
	}
	
	public Piece[][] getPieces() {
		return pieces;
	}
	
	

	public void removePiece(int column, int row) {
		
		//could display the piece somewhere or add it to a list of captured pieces before setting to null
		pieces[column][row]=null;
	}
	
	private void initialisePieces() {
		
		/*
		 * 		A8 is [0][0], A1 is [0][7]
		 * 		H1 is [7][7]
		 */
		
		/*useful for checking castling
		//black rooks
		pieces[0][0]=new Rook("black",0,0);
		pieces[7][0]=new Rook("black",7,0);

		//black queen
		pieces[3][0]=new Queen("black",3,0);
		
		//black king
		pieces[4][0]=new King("black",4,0);

		//white rooks
		pieces[0][7]=new Rook("white",0,7);
		pieces[7][7]=new Rook("white",7,7);
		

		//white queen
		//pieces[3][7]=new Queen("white",3,7);
		
		//white king
		pieces[4][7]=new King("white",4,7);	
		*/
		
		/*
		//to test a very unusual en passant move
		
		pieces[2][1]=new Bishop("white",2,1);
		pieces[3][1]=new Knight("black",3,1);
		pieces[0][3]=new Pawn("black",0,3);
		pieces[2][3]=new Pawn("black",2,3);
		pieces[1][4]=new Pawn("black",1,4);
		pieces[7][3]=new Pawn("black",7,3);
		pieces[3][3]=new King("black",3,3);			
		pieces[3][5]=new King("white",3,5);
		
		pieces[0][6]=new Pawn("white",0,6);
		pieces[1][5]=new Pawn("white",1,5);
		pieces[2][6]=new Pawn("white",2,6);
		pieces[5][5]=new Pawn("white",5,5);
		pieces[6][5]=new Pawn("white",6,5);
		*/
		
		//add black pawns to the 2nd row
		for ( int column = 0; column < pieces.length; column++ ) 
			//pieces[column][1].setIcon(pawnB);		
			pieces[column][1] = new Pawn("black",column,1);
		
		//add white pawns to the 6th row
		for ( int column = 0; column < pieces.length; column++ ) 
			pieces[column][6] = new Pawn("white",column,6);			
		
		//black rooks
		pieces[0][0]=new Rook("black",0,0);
		pieces[7][0]=new Rook("black",7,0);
		
		//black knights
		pieces[1][0]=new Knight("black",1,0);
		pieces[6][0]=new Knight("black",6,0);
		
		//black bishops
		pieces[2][0]=new Bishop("black",2,0);
		pieces[5][0]=new Bishop("black",5,0);
		
		//black queen
		pieces[3][0]=new Queen("black",3,0);
		
		//black king
		pieces[4][0]=new King("black",4,0);

		//white rooks
		pieces[0][7]=new Rook("white",0,7);
		pieces[7][7]=new Rook("white",7,7);
		
		//white knights
		pieces[1][7]=new Knight("white",1,7);
		pieces[6][7]=new Knight("white",6,7);
		
		//white bishops
		pieces[2][7]=new Bishop("white",2,7);
		pieces[5][7]=new Bishop("white",5,7);
		
		//white queen
		pieces[3][7]=new Queen("white",3,7);
		
		//white king
		pieces[4][7]=new King("white",4,7);	
	}
}
