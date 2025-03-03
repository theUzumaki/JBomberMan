package backgrounds;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import entities.HUD;
import main.GamePanel;
import main.KeyHandler;

/**
 * the game over screen, its update and draw
 */
public class GameOver {
	/**
	 * coordinate of the arrow
	 */
	public int xPointer, yPointer;
	/**
	 * the level to be reloaded
	 */
	int level;
	/**
	 * holds a random value for the password
	 */
	int cod1, cod2, cod3, cod4;
	String psswrd;
	GamePanel gamePanel;
	/**
	 * image variable
	 */
	BufferedImage blackScreen, pointer, cont, no, yes, number1, number2, number3, number4;
	/**
	 * used to build random functions
	 */
	Random rndm= new Random();

/**
 * assigns the values and loas the sprites
 * @param gamePanel
 */
	public GameOver(GamePanel gamePanel) {
		this.gamePanel= gamePanel;
		xPointer= 4*gamePanel.biggerTile;
		yPointer= 6*gamePanel.biggerTile;
		
		try {
			blackScreen= 	ImageIO.read(getClass().getResourceAsStream("/Background/game over/black screen.png"));
			cont= 			ImageIO.read(getClass().getResourceAsStream("/Background/game over/continue?.png"));
			no= 			ImageIO.read(getClass().getResourceAsStream("/Background/game over/no.png"));
			yes= 			ImageIO.read(getClass().getResourceAsStream("/Background/game over/yes.png"));
			pointer= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/pointer.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

/**
 * called by gamePanel's method update, manages the movement of the pointer and triggers the setLevel method in gamePanel and hud based on when pressed enter on keyboard
 */
	public void update(KeyHandler keys, HUD hud) {
		if (keys.select== true) {
			hud.setLevel();
			
			if (xPointer== 4*gamePanel.biggerTile) {
				// YES
				gamePanel.setLevel(level);
			}
			else {
				// NO
				gamePanel.setLevel(0);
			}
			keys.select= false;
		}
		else if (keys.right== true) {
			if (xPointer!= 9*gamePanel.biggerTile) {
				xPointer+= 5*gamePanel.biggerTile;
			}
			keys.right= false;
		}
		else if (keys.left== true) {
			if (xPointer!= 4*gamePanel.biggerTile) {
				xPointer-= 5*gamePanel.biggerTile;			
			}
			keys.left= false;
		}
	}

/**
 * called by gamePanel's method paintComponent, draws the images selected
 */
	public void draw(Graphics brush) {
		brush.drawImage(blackScreen, 2*gamePanel.biggerTile, 1*gamePanel.biggerTile, 12*gamePanel.biggerTile, 12*gamePanel.biggerTile, null);
		brush.drawImage(cont, 5*gamePanel.biggerTile, 5*gamePanel.biggerTile, 6*gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(yes, 5*gamePanel.biggerTile, 6*gamePanel.biggerTile, 2*gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(no, 10*gamePanel.biggerTile, 6*gamePanel.biggerTile, 4*gamePanel.biggerTile/gamePanel.scale, 1*gamePanel.biggerTile, null);
		brush.drawImage(number1, 4*gamePanel.biggerTile+ gamePanel.biggerTile/ 2, 8*gamePanel.biggerTile, gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(number2, 6*gamePanel.biggerTile+ gamePanel.biggerTile/ 2, 8*gamePanel.biggerTile, gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(number3, 8*gamePanel.biggerTile+ gamePanel.biggerTile/ 2, 8*gamePanel.biggerTile, gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(number4, 10*gamePanel.biggerTile+ gamePanel.biggerTile/ 2, 8*gamePanel.biggerTile, gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(pointer, xPointer, yPointer, 1*gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
	}
	
/**
 * called by player's method update when death, generates random numbers using java.Utils.Random and loads the sprites for those numbers and returns the password
 */
	public String passwordGenerator() {
		cod1= rndm.nextInt(8);
		cod2= rndm.nextInt(8);
		cod3= rndm.nextInt(8);
		cod4= rndm.nextInt(8);
		
		try {
			number1= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+cod1+".png"));
			number2= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+cod2+".png"));
			number3= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+cod3+".png"));
			number4= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+cod4+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		psswrd= ""+cod1+cod2+cod3+cod4;
		
		return psswrd;
	}
/**
 * called by password's method update, checks if the passed password equals the password generated by gameOver's method passwordGenerator and returns a boolean
 */
	public boolean passwordValid(String psswrd) {
		
		if (this.psswrd!= null && this.psswrd.equals(psswrd)) {
			return true;
		}
		else {
			return false;
		}
	}
/**
 * called by player's method update when death, is used to assign the current level to the gameOver, it will be utilized to restart the level when player dies
 */
	public void setLevel(int level) {
		this.level= level;
	}
}
