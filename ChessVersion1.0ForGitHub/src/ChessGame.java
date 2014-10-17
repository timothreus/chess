import javax.swing.JFrame;

public class ChessGame {
	private GUI gui;
	private Model model; //the game state consisting of a 2d array of pieces.
	private Controller controller;

	public ChessGame() {
		
		model = new Model();
		gui = new GUI(model);
		controller = new Controller(model,gui);
		gui.registerListener(controller);
				
		//TO DO what size?
		gui.setSize(700,700);		
		gui.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );		
		gui.setVisible( true );
		gui.updateGUI(model);
		
	}
	
	
	public static void main(String[] args) {
		ChessGame game = new ChessGame();
	}

}
