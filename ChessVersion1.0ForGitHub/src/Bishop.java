
import java.lang.Math;

public class Bishop extends Piece{

	public Bishop( String colour, int column, int row) {
		super(colour,column,row);
	}
	
	private boolean withinBounds( int num) {
		if ( num >=0 && num <=7)
			return true;
		else
			return false;
	}
	
	private boolean noObstacles( Model model, int x1, int x2, int y1, int y2 ) {
		
		int xStep;
		int yStep;
		if (x2>x1)
			xStep = 1;
		else
			xStep = -1;
		
		if (y2>y1)
			yStep = 1;
		else
			yStep = -1;
		
		
		int x = x1+xStep;
		int y = y1+yStep;
		Piece[][] pieces = model.getPieces();
		
		while (x != x2) {
			if ( withinBounds(x) && withinBounds(y) ) 
				if (pieces[x][y] != null)
					return false;
			
			x+=xStep;
			y+=yStep;
		}		
		return true;
	}
	
	
	public boolean canMoveTo(Model model, int col, int row ) {
		
		Piece[][] pieces = model.getPieces();
		
		//first check the move isn't to the current position (eg a1 to a1!)
		if (this.getRow() == row && this.getColumn() == col) {	
			return false;
		}
		
		//check difference between row and this.getRow() is the same as the distance between col and this.col
		if ( 	( Math.abs( col - this.getColumn()) == Math.abs( row - this.getRow() )) &&
				( noObstacles( model,this.getColumn(), col, this.getRow(), row ) ) )	{
			if ( (pieces[col][row] != null) && ( pieces[col][row].getColour().equals( this.getColour() ) ) ) 
				return false;
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
		
		//check difference between row and this.getRow() is the same as the distance between col and this.col
		if ( 	( Math.abs( col - this.getColumn()) == Math.abs( row - this.getRow() )) &&
				( noObstacles( model,this.getColumn(), col, this.getRow(), row ) ) )	
			if ( (pieces[col][row] != null) && ( pieces[col][row].getColour().equals( this.getColour() ) ) )
				return false;
			else 					
				return true;
		else 
			return false;
	}
	
	public String toString() {
		return colour + " bishop";
	}
}
