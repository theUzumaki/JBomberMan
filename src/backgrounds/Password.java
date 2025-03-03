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
 * the password screen, its update and draw
 */
public class Password {
	/**
	 * coordinate of the arrow
	 */
	public int xPointer, yPointer;
	/**
	 * holds a random value for the password
	 */
	int cod1, cod2, cod3, cod4;
	/**
	 * slows down the input receiving
	 */
	int pressIntervalTimer;
	/**
	 * password shown
	 */
	String psswrd= "0000";
	GamePanel gamePanel;
	GameOver gameOver;
	/**
	 * image variable
	 */
	BufferedImage blackScreen, pointer, number1, number2, number3, number4;
	/**
	 * used to build random functions
	 */
	Random rndm= new Random();

/**
 * assigns the values and loads the sprites
 */
	public Password(GamePanel gamePanel, GameOver gameOver) {
		this.gamePanel= gamePanel;
		this.gameOver= gameOver;
		
		xPointer= 3*gamePanel.biggerTile;
		yPointer= 7*gamePanel.biggerTile;
		
		try {
			blackScreen= 	ImageIO.read(getClass().getResourceAsStream("/Background/game over/black screen.png"));
			pointer= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/pointer.png"));
			
			number1= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+0+".png"));
			number2= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+0+".png"));
			number3= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+0+".png"));
			number4= 		ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+0+".png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

/**
 * called by gamePanel's method update, manages the movement of the pointer and triggers gameOver's method passwordValid when pressed enter and based on the return sets the level
 */
	public void update(KeyHandler keys, HUD hud) {
		if (keys.select== true) {
			if (gameOver.passwordValid(psswrd)) {
				
				gamePanel.setLevel(gameOver.level);
				hud.setLevel();
			}
			else {
				gamePanel.setLevel(0);
			}
			keys.select= false;
		}
		else if (keys.up== true && pressIntervalTimer>= 20) {
			passwordModifier("up");
			pressIntervalTimer= 0;
		}
		else if (keys.down== true && pressIntervalTimer>= 20) {
			passwordModifier("down");
			pressIntervalTimer= 0;
		}
		
		pressIntervalTimer++;
	}

/**
 * called by gamePanel's method paintComponent, draws the images selected
 */
	public void draw(Graphics brush) {
		brush.drawImage(blackScreen, 2*gamePanel.biggerTile, 1*gamePanel.biggerTile, 12*gamePanel.biggerTile, 12*gamePanel.biggerTile, null);
		brush.drawImage(number1, 4*gamePanel.biggerTile+ gamePanel.biggerTile/ 2, 7*gamePanel.biggerTile, gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(number2, 6*gamePanel.biggerTile+ gamePanel.biggerTile/ 2, 7*gamePanel.biggerTile, gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(number3, 8*gamePanel.biggerTile+ gamePanel.biggerTile/ 2, 7*gamePanel.biggerTile, gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(number4, 10*gamePanel.biggerTile+ gamePanel.biggerTile/ 2, 7*gamePanel.biggerTile, gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
		brush.drawImage(pointer, xPointer, yPointer, 1*gamePanel.biggerTile, 1*gamePanel.biggerTile, null);
	}

/**
 * called by player's update method when dies, it converts the password received from the gameOver into images
 */
	public void passwordStringIntoImages(String psswrd) {
		
		try {
			for (int i= 0; i< 4; i++) {
				int number= Integer.parseInt(Character.toString(psswrd.charAt(i)));
				
				switch (i) {
				case 0:
					cod1= number;
					number1= ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+number+".png"));
					break;
				case 1:
					cod2= number;
					number2= ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+number+".png"));
					break;
				case 2:
					cod3= number;
					number3= ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+number+".png"));
					break;
				case 3:
					cod4= number;
					number4= ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+number+".png"));
					break;
				}
			}		
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.psswrd= ""+cod1+cod2+cod3+cod4;
	}
/**
 * called by the update, modifies the number of the password when the arrows on keyboard are pressed 
 */
	private void passwordModifier(String verse) {
		
		if (verse== "up") {
			cod1= (cod1 + 1) % 8;	// the % serves to keep the numbers in range
		}
		else if (verse== "down") {
			if (cod1> 0) {
				cod1= cod1 - 1;
			}
			else {
				cod1= 7;
			}
		}
		
		try {
			number1= ImageIO.read(getClass().getResourceAsStream("/Background/game over/numbers/"+cod1+".png"));
		} catch (IOException e){
			e.printStackTrace();
		}
		
		this.psswrd= ""+cod1+cod2+cod3+cod4;
	}
}
