import java.awt.*;
//import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.*;
import java.awt.event.*;

public class GUI extends JFrame {
		
	private JPanel board;
	private JButton[][] squares;
	private JTextField commentBar;
	private Model model;
	private String currentView;
	

	private Icon pawnB = new ImageIcon( getClass().getResource( "pawnb.gif") );
	private Icon pawnW = new ImageIcon( getClass().getResource( "pawnw.gif") );

	private Icon rookB = new ImageIcon( getClass().getResource( "rookb.gif") );
	private Icon rookW = new ImageIcon( getClass().getResource( "rookw.gif") );
	
	private Icon knightB = new ImageIcon( getClass().getResource( "knightb.gif") );
	private Icon knightW = new ImageIcon( getClass().getResource( "knightw.gif") );

	private Icon bishopB = new ImageIcon( getClass().getResource( "bishopb.gif") );
	private Icon bishopW = new ImageIcon( getClass().getResource( "bishopw.gif") );
	
	private Icon queenB = new ImageIcon( getClass().getResource( "queenb.gif") );
	private Icon queenW = new ImageIcon( getClass().getResource( "queenw.gif") );
	
	private Icon kingB = new ImageIcon( getClass().getResource( "kingb.gif") );
	private Icon kingW = new ImageIcon( getClass().getResource( "kingw.gif") );
	
	public GUI(Model model) {
		super("Welshiko Chess version 1.0. Programmed by Tim Welsh 2014");
		this.model = model; 
		squares = new JButton[8][8];
		
		
		
		board = new JPanel();
		board.setLayout( new GridLayout(8,8) );
		
		for (int column = 0; column<squares.length;column++)
			for (int row = 0; row<squares[column].length;row++) {
				
				JButton button = new JButton();
				
				//if row + column = even set the background colour to white
				if ( (row+column)%2 ==1)
					button.setBackground(Color.RED);
				else
					button.setBackground(Color.WHITE);
				squares[column][row] = button;
				
			}
		
		for (int row = 0; row<squares.length;row++)
			for (int column = 0; column<squares[row].length;column++)
				board.add(squares[column][row] );
		
		currentView = "white";
		
		commentBar = new JTextField(20);
		commentBar.setEditable( false );
		
		
		Container container = getContentPane();
		container.add( board, BorderLayout.CENTER );
		container.add( commentBar, BorderLayout.SOUTH );
		
		

		

	}
	public void highlightButton(int column, int row) {
		squares[column][row].setBorder( new LineBorder(Color.BLUE,3) );
	}
	
	public void removeHighlight(int column, int row) {
		squares[column][row].setBorder( BorderFactory.createEmptyBorder() );
		
	}	
	
	public void removeAllHighlights() {
		for (int x = 0; x<squares.length; x++)
			for (int y = 0; y<squares[x].length; y++) {
				
				squares[x][y].setBorder( BorderFactory.createEmptyBorder() );
			}
				
	}
	
	public void actionPerformed(ActionEvent e) {}
	
	public void registerListener(Controller listener) {
		for (int column = 0; column<squares.length;column++)
			for (int row = 0; row<squares[column].length;row++) 
				squares[column][row].addActionListener(listener);
			

	}

	public void enableUserInput () {
		for (int column = 0; column<squares.length;column++)
			for (int row = 0; row<squares[column].length;row++) 
				squares[column][row].setEnabled(true);
		
	}
	
	public void disableUserInput () {
		for (int column = 0; column<squares.length;column++)
			for (int row = 0; row<squares[column].length;row++) 
				squares[column][row].setEnabled(false);
		
	}
	
	public JButton[][] getSquares() {
		return squares;
	}
	
	public void addComment( String message ) {
		commentBar.setText(message);
	}
	
	
	
	
	public Icon getIcon ( String colour, Piece piece) {
		//TO DO this is not nice!
		if ( colour.equals("white") ) {
			if ( piece instanceof Pawn) 
				return(pawnW);
			if ( piece instanceof Rook) 
				return(rookW);
			if ( piece instanceof Knight) 
				return(knightW);
			if ( piece instanceof Bishop) 
				return(bishopW);
			if ( piece instanceof Queen) 
				return(queenW);
			if ( piece instanceof King) 
				return(kingW);
		
		}
		
		if ( colour.equals("black") ) {
			if ( piece instanceof Pawn) 
				return(pawnB);
			if ( piece instanceof Rook) 
				return(rookB);
			if ( piece instanceof Knight) 
				return(knightB);
			if ( piece instanceof Bishop) 
				return(bishopB);
			if ( piece instanceof Queen) 
				return(queenB);
			if ( piece instanceof King) 
				return(kingB);
			
		}
		return null;
	}
	
	public void addIcon( int column, int row, String colour, Piece piece) {
		
		squares[column][row].setIcon( getIcon(colour,piece) );
		
	}

	public void removeIcon( int column, int row) {
		squares[column][row].setIcon(null);
	}
	
	//update the gui i.e. draw the board
	public void updateGUI(Model model) {
		this.model = model;
		for ( int col = 0; col<model.getPieces().length; col++ )
			for ( int row = 0; row<model.getPieces()[col].length; row++ ) {
				if (model.getPieces()[col][row] !=null)
					addIcon( col,row,model.getPieces()[col][row].getColour(), model.getPieces()[col][row] );
				else
					//inefficient because usually there will be no piece to remove 
					removeIcon(col,row);
			}

	}
	
	public void showBlackView( Model model ) {
		this.model = model;
		for (int row = 0; row<squares.length;row++)
			for (int column = 0; column<squares[row].length;column++)
				board.add(squares[7-column][7-row] );
	}
	public void showWhiteView( Model model ) {
		this.model = model;
		for (int row = 0; row<squares.length;row++)
			for (int column = 0; column<squares[row].length;column++)
				board.add(squares[column][row] );
	}
	
	public void flipGUI( Model model ) {
		if (currentView.equals("black") ) {
			showWhiteView(model);
			currentView = "white";
		}
		else {
			showBlackView(model);
			currentView = "black";
		}
	}
}
