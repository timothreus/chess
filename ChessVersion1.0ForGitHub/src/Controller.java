import java.awt.event.*;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Controller implements ActionListener {

	private Model model;
	private GUI view;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	private String checkState;
	private Move lastMove;
	
	private boolean firstClick = true;
	int col=-1;
	int row=-1;
	int col2=-1;
	int row2=-1;
		
	public Controller(Model model, GUI view) {
		checkState="";
		this.model = model;
		this.view = view;
		
		
		Object[] options = {"1 Player","2 Player"};
		
		int n = JOptionPane.showOptionDialog(null,
			    "Would you like to play a 1-player or 2-player game?","Number of Players", 
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
			
			switch (n) {
				case 0: 	start1PlayerGame();
							break;
				case 1: 	start2PlayerGame();
							break;
				default:
							start1PlayerGame();
							break;
			}
	}
	
	public void start1PlayerGame() {
		String colour;
		Object[] options = {"Black","White"};
		
		int n = JOptionPane.showOptionDialog(null,
			    "Would you like to play black or white?", "Colour",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
			
			switch (n) {
				case 0: 	colour = "black";
							break;
				case 1: 	colour = "white";
							break;
							
				default:
							colour = "white";
							break;
							
			}
		if ( colour.equals("white") ) {
			player1 = new HumanPlayer("Human",colour);
			player2 = new ComputerPlayer("Computer","black");
		}
		else {
			player1 = new ComputerPlayer("Computer","white");
			player2 = new HumanPlayer("Human","black");
		}
		lastMove = new Move();
		
		currentPlayer = player1;
		view.addComment("White is " + player1.getName() + ". Black is " + player2.getName() + ". ");
		//if ( player1.getName().equals("Computer") ) {
		if (player1 instanceof ComputerPlayer) {
			view.showBlackView(model);
			makeComputerMove();
		}	
	}
		
	public void start2PlayerGame() {
		player1 = new HumanPlayer("Human1","white");
		player2 = new HumanPlayer("Human2","black");
		lastMove = new Move();
		currentPlayer = player1;
		view.addComment("White is " + player1.getName() + " black is " + player2.getName() + ". " + currentPlayer.getName() + " to move");
	}
	
	
	/*
	 * http://stanford.edu/class/archive/cs/cs106a/cs106a.1136/handouts/36-assignment-5.pdf was consulted
	 *
	 * for debugging 
	 * JOptionPane.showMessageDialog(null,"in controller, button " + col + row +" was pressed","debuggin",JOptionPane.PLAIN_MESSAGE);
	 */
	
	
	
	
	public void actionPerformed(ActionEvent e) {	
		
		if (currentPlayer instanceof HumanPlayer) 
			makeHumanMove(e);
			
	}
	private void makeHumanMove(ActionEvent e) {
			//(it takes two clicks to complete a move - a starting square and a destination square)
			if (firstClick) 
				processFirstClick(e);
			else 
				processSecondClick(e);
	}
	
	private void processFirstClick(ActionEvent e) {
			
			//find which button (board square) was clicked.
			getCoordinates( e );		
			//if the button clicked contains a piece, get the colour of that piece
			Piece piece = model.getPieces()[col][row];
			String pieceColour="";
			if (piece != null ) {
				pieceColour= piece.getColour();
				//if the piece colour is the same as the colour of the current player and the piece has legal moves
				//highlight the square that was clicked
				if ( pieceColour !=null && pieceColour.equals(currentPlayer.getColour() ) && piece.hasLegalMoves(model) ) {
					firstClick = false;
					view.highlightButton(col,row); //alternatively could just draw the board?
				}
			}	
		
	}
	
	private void processSecondClick(ActionEvent e) {
		//second click
		//find which button was clicked
		getCoordinates( e );
		String playerType = "Human";
		//if piece can move to the selected spot AND destination is not the spot the piece already occupies)
		if ( currentPlayer.isValidMove(model,col,row,col2,row2) && !( (col==col2) && (row == row2) ))  {
		
			//clear all highlighted squares with the method unselectAll - 
			view.removeHighlight(col,row);
			makeMove(playerType);
			
		}
		//else if piece can not move to the selected spot 	
		else 
			firstClick = false;	
	}
	
	private void makeMove(String playerType) {
		model.movePiece(model.getPieces()[col][row],col,row,col2,row2);
	
		model.checkForPromotion(playerType,col,row,col2,row2);
		model.checkForCastling(col,row,col2,row2);
		model.checkForEnPassant(model.getPieces()[col2][row2],col,row,col2,row2);
	
		if (model.getPieces()[col2][row2] !=null)
			model.getPieces()[col2][row2].setHasMoved(true);
	
		lastMove.setMove(model.getPieces()[col2][row2],col,row,col2,row2);
		model.setPreviousMove(lastMove);
	
		//draw the board
		view.updateGUI(model);
		
		//if the move was successful it becomes the other player's turn
		swapCurrentPlayer();
		firstClick= true;
									
		//CHECK for CHECK/STALEMATE/CHECKMATE
		checkGameState();	
			
		//IF NOT END OF GAME
		if ( checkState!="Check Mate!"  && checkState!="Stale Mate!" ) {
			view.addComment(checkState + " " + currentPlayer.getName() + " to move.");
			if( currentPlayer instanceof ComputerPlayer ) {
				view.disableUserInput();	
				makeComputerMove();
				view.enableUserInput();
			}
		}
		else if ( checkState.equals("Check Mate!")  || checkState.equals("Stale Mate!") ) {
			view.addComment(checkState);
			JOptionPane.showMessageDialog(null,"Game Over", checkState,JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	public void makeComputerMove() {
		
		Move randomMove = getRandomMove(model);
		Move bestMove = getBestMove(model);
		if (bestMove.getOldX() >-1) {  //
			col = bestMove.getOldX();
			row = bestMove.getOldY();
			col2 = bestMove.getNewX();
			row2 = bestMove.getNewY();
		}
		else {
			col = randomMove.getOldX();
			row = randomMove.getOldY();
			col2 = randomMove.getNewX();
			row2 = randomMove.getNewY();
		}
			
		view.highlightButton(col, row);
		view.highlightButton(col2, row2);
		String playerType = "Computer";
	
		makeMove(playerType);
	}
	
	public Move getBestMove( Model model) {
		int col=-1;
		int row=-1;
		
		Piece piece=null;
		String pieceColour=null; 
		int col2 = -1;
		int row2 = -1;
		
		int maxPoints =0;
		Move bestMove = new Move();
		int riskAfterMove = 0;
		int overallScore = 0;
		
		for (col = 0; col<8;col++) { 
			for (row = 0;row<8;row++) {
				
				int riskBeforeMove = 0;
				if (model.pieceAt(col,row) != null)
					riskBeforeMove= evaluateRisk(model, model.pieceAt(col, row));
				
				
				piece = model.getPieces()[col][row];
				if (piece !=null) {
					pieceColour = piece.getColour();
					
					for (col2 = 0; col2<8;col2++) 
						for (row2 = 0;row2<8;row2++) {
							if (pieceColour.equals( currentPlayer.getColour()) ){
								if (currentPlayer.isValidMove(model,col,row,col2,row2) && !( (col==col2) && (row == row2) ))  {
									int points = 0;
									if (model.pieceAt(col2,row2)!=null) {
										if ( model.pieceAt(col2,row2) instanceof Queen )
											
											points = 8;
										else if ( model.pieceAt(col2,row2) instanceof Rook )
											points = 5;
										else if ( model.pieceAt(col2,row2) instanceof Knight )
											points = 4;
										else if ( model.pieceAt(col2,row2) instanceof Bishop )
											points = 3;
										else if ( model.pieceAt(col2,row2) instanceof Pawn )
											points = 2;
										
									}
									Model clonedModel = model.cloneModel();
									clonedModel.movePiece(clonedModel.getPieces()[col][row], col,row,col2,row2);
									
									//swap back later as this isn't a real move
									swapCurrentPlayer();
									
									if (inCheckMate(clonedModel,currentPlayer))
										points += 100;
									if (inCheck(clonedModel,currentPlayer))
										points += 1;
									//if can get promoted add 8 to the score
									if ( model.pieceAt(col2,row2) instanceof Pawn  && ( row2==0 || row2 ==7  )      ) 
										points +=8;
									
									
									riskAfterMove = getNextMovePoints(clonedModel);
									
									overallScore = points - riskAfterMove;
									
									if (riskBeforeMove > overallScore)
										//Make the move more likely
										//THE PROPOSED MOVE GETS MORE POINtS EVEN THOUGH IT MAY BE EQUALLY UNATTRACTIVE
										//want to move it somewhere but not necessarily where is currently suggested.
										overallScore+=1;
									swapCurrentPlayer();
									if (overallScore > maxPoints) {								
										maxPoints = points;
										//minRisk = riskAfterMove;
										//finalScore = maxPoints-minRisk;
										bestMove = new Move(col,row,col2,row2);
									}
									
								}
				
							}
						}
				}
			}
		}
		return bestMove;
	}
	
	public int getNextMovePoints( Model model) {
		int col;
		int row;
		
		Piece piece=null;
		String pieceColour=null; 
		int col2 = -1;
		int row2 = -1;
		int risk=0;
		int maxPoints = 0;
		
		for (col = 0; col<8;col++) {
			for (row = 0;row<8;row++) {
				
				piece = model.getPieces()[col][row];
				if (piece !=null) {
					pieceColour = piece.getColour();
					
					for (col2 = 0; col2<8;col2++) 
						for (row2 = 0;row2<8;row2++) {
							if (pieceColour.equals( currentPlayer.getColour()) ){
							
								if (currentPlayer.isValidMove(model,col,row,col2,row2) && !( (col==col2) && (row == row2) ))  {
									int points = 0;
									if (model.pieceAt(col2,row2)!=null) {
										if ( model.pieceAt(col2,row2) instanceof Queen )
											
											points = 8;
										else if ( model.pieceAt(col2,row2) instanceof Rook )
											points = 5;
										else if ( model.pieceAt(col2,row2) instanceof Knight )
											points = 4;
										else if ( model.pieceAt(col2,row2) instanceof Bishop )
											points = 3;
										else if ( model.pieceAt(col2,row2) instanceof Pawn )
											points = 1;
										
									}
									Model clonedModel = model.cloneModel();
									clonedModel.movePiece(clonedModel.getPieces()[col][row], col,row,col2,row2);
									
									//swap back later as this isn't a real move
									swapCurrentPlayer();
									
									
									if (inCheckMate(clonedModel,currentPlayer))
										points = 10;
									
									//maybe take next line out. Taking a pawn could be worth more than getting a player in check
									if (inCheck(clonedModel,currentPlayer))
										points = 1;
									
									swapCurrentPlayer();
									
									risk = evaluateRisk(clonedModel, clonedModel.pieceAt(col2, row2));
									
									points = points-risk;
									if (points > maxPoints) {	 
										maxPoints = points;
									}
								}
				
							}
						}
				}
			}
		}
		return maxPoints;
	}
	
	public int evaluateRisk( Model model, Piece piece ) {
		//won't foresee check mate!
		int risk = 0;
		Piece attackingPiece;
		for (col = 0; col<8;col++) { 
			for (row = 0;row<8;row++) {
			
				attackingPiece = model.pieceAt(col, row);
				if (attackingPiece !=null && attackingPiece.canCapture(model, piece.getColumn(), piece.getRow())  ) {
					if ( piece instanceof Queen )
						
						risk = 8;
					else if ( piece  instanceof Rook )
						risk = 5;
					else if ( piece instanceof Knight )
						risk = 4;
					else if ( piece instanceof Bishop )
						risk = 3;
					else if ( piece instanceof Pawn )
						risk = 2;
				}
			}
		}
		return risk;
	}
	
	public Move getRandomMove(Model model) {
		//pick a piece at random and check it has legal moves
		int col = -1;
		int row = -1;
		Random generator = new Random();
		Piece piece=null;
		String pieceColour=null; 
		int col2 = -1;
		int row2 = -1;
					
		int randomX = -1;
		int randomY = -1;
		view.removeAllHighlights();
		while (col<0 && row <0) {
			randomX = generator.nextInt(8);
			randomY = generator.nextInt(8);
			piece = model.getPieces()[randomX][randomY];
			if (piece !=null) {
				pieceColour = piece.getColour();
				if (pieceColour.equals( currentPlayer.getColour()) )
					if (piece.hasLegalMoves(model)) {
						col = randomX;
						row = randomY;
					}
				
			}
		}
						
		randomX = -1;
		randomY=-1;
						
		while (col2<0 && row2 <0) {
			randomX = generator.nextInt(8);
			randomY = generator.nextInt(8);
							
			if (pieceColour.equals( currentPlayer.getColour()) )
				if (player2.isValidMove(model,col,row,randomX,randomY) && !( (col==randomX) && (row == randomY) ))  {
					col2 = randomX;
					row2 = randomY;
				}
		}
		return new Move(col,row,col2,row2);
	}
	
	
	
	public void getCoordinates( ActionEvent e) {
		
		JButton b = (JButton)e.getSource();
				
		for (int i = 0; i < 8; i++) 
			for (int j = 0; j < 8; j++) 
				if (view.getSquares()[i][j] == b) {
					
					if ( firstClick ) {
						col = i;
						row = j;
					}
					else {
						col2 = i;
						row2 = j;
					}		
				}
	}

	public void checkGameState() {
		//TODO need to terminate the game
		//or eg a restart button
		if ( inCheckMate( model, currentPlayer) ) {
			checkState=("Check Mate!");
			
		}
		else if ( inCheck( model, currentPlayer) ) {
			checkState=("Check!");
		}
			
		else if ( inStaleMate( model, currentPlayer) ) {
			
			checkState=("Stale Mate!");
		}
		else
			checkState=("");
		view.addComment(checkState);
	}
	
	public void swapCurrentPlayer() {
		if (currentPlayer ==player1)
			currentPlayer =player2;
		else
			currentPlayer = player1;	
	}
	
	private boolean inCheckMate (Model model, Player player){
		Piece[][] pieces = model.getPieces();
		
		if (inCheck(model, player) ) {
			
			//loop through each possible opponent piece {
			for (int x = 0; x<8; x++) 
				for (int y=0; y<8; y++) 
					
					//if there is a piece at the location and it is the colour of player's colour
					if (pieces[x][y] != null && pieces[x][y].getColour().equals( player.getColour() )  ) 
						
						//loop through each possible move for that piece
						for (int x1 = 0; x1<8; x1++) 
							for (int y1=0; y1<8; y1++) 
								if ( pieces[x][y].canMoveTo(model,x1,y1) ) 
									return false;
			//if come out of the loop return true as there are no possible moves
			return true;
		}
		else 
			return false;
	}
	
	
	private boolean inCheck (Model model, Player player){
		Piece[][] pieces = model.getPieces();
		
		String opponentColour="";
		
		if ( player.getColour() == "black" )
			opponentColour = "white";
		else if ( player.getColour() == "white" )
			opponentColour = "black";
		
		
		int kingX=-1;
		int kingY=-1;
		
		for (int x = 0; x<8; x++) 
			for (int y=0; y<8; y++) 
				if ( pieces[x][y] !=null && pieces[x][y].getColour().equals(player.getColour()) && (pieces[x][y] instanceof King) ) {
					kingX = x;
					kingY = y;
				}
		
		//loop through each possible opponent piece {
		for (int x = 0; x<8; x++) 
			for (int y=0; y<8; y++) 
				if (pieces[x][y] != null && pieces[x][y].getColour().equals( opponentColour )  ) 
					//if piece can move to currentPlayer's King
					if ( pieces[x][y].canMoveTo(model,kingX,kingY)) 
						return true;					
	
		//if come out of the loop then return false as no piece can "take" your king
		return false;
				
	}
	
	private boolean inStaleMate (Model model, Player player){
		
		Piece[][] pieces = model.getPieces();

		if (!(inCheck(model, player)) ) {
			//loop through each possible opponent piece {
			for (int x = 0; x<8; x++) 
				for (int y=0; y<8; y++) 
					if (pieces[x][y] != null && pieces[x][y].getColour().equals( player.getColour() )  ) 
					
						//loop through each possible move for that piece							
						for (int x1 = 0; x1<8; x1++) 
							for (int y1=0; y1<8; y1++) 
								if ( pieces[x][y].canMoveTo(model,x1,y1) ) 
									return false;
			return true;
		}
		else
			return false;
	}
	
}

