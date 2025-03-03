package main;

import javax.swing.JFrame;

/**
 * main, creates the window, the gamePanel and calls its method gamePanel.start()
 */
public class JBomberMan {

/**
 * main of the program, instantiate window in order to display the game and gamePanel in order to start the game calling its method start
 */
	public static void main(String[] args) {
		// window settings
		JFrame window= new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to close on click of the "x"
		window.setResizable(false); // blocks the resizing of the window
		window.setTitle("Bomber-Man"); // title of window
		
		GamePanel gamePanel= new GamePanel();
		window.add(gamePanel);
		window.pack(); // cause the window to appear as its subcomponents (gamePanel)
		
		window.setLocationRelativeTo(null); // to make it appear on center
		window.setVisible(true); // to display it
		
		gamePanel.start();
	}
}
