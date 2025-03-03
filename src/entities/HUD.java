package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * the scoreboard on the top, its update, draw and animation
 */
public class HUD extends Entity{
	BufferedImage[] sprites;
	/**
	 * the string representing the score
	 */
	String board;
	int score;
	/**
	 * coordinate on which draw the score
	 */
	int livesCounterX;
/**
 * inherits superconstructor from Entity, sets values and loads images
 */
	public HUD(GamePanel gamePanel, int level) {
		super(gamePanel, level);
		active= true;
		dfX= 6*gamePanel.biggerTile + gamePanel.biggerTile/2;
		livesCounterX= dfX - 5*gamePanel.biggerTile;
		dfY= gamePanel.biggerTile/2 + gamePanel.scale;
		dfWidth= gamePanel.biggerTile/2;
		dfHeight= gamePanel.biggerTile - 2*gamePanel.scale;
		imgLoader();
	}
/**
 * called by the gamePanel's method paintComponent, draws the currents points and lives left
 */
	public void draw(Graphics brush) {
		int offset= 0;
		board= Integer.toString(totalScore);
		
		// score board
		for (int i= board.length()-1; i>= 0; i--) {
			img= sprites[Integer.parseInt(Character.toString(board.charAt(i)))];
			brush.drawImage(img,dfX-(offset*dfWidth),dfY,null);
			offset++;
		}		
		
		// lives
		img= sprites[livesCounter];
		brush.drawImage(img,livesCounterX,dfY,null);
	}
/**
 * called by the gameOver's method update, is used to reset lives when dead
 */
	public void setLevel() {
		livesCounter= 5;
	}
/**
 * called by the constructor, loads all the sprites and puts them in specific arrays
 */
	private void imgLoader(){
		try {
			sprites= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/0.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/score board/9.png")),
			};
			imgResizer(sprites, dfWidth, dfHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
