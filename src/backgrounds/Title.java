package backgrounds;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

/**
 * the title screen, its update and draw
 */
public class Title {
	/**
	 * coordinate of the arrow
	 */
	public int xPointer, yPointer;
	GamePanel gamePanel;
	/**
	 * image variable
	 */
	BufferedImage titleScreen, pointer;
/**
 * assigns the values and loads the sprites
 */
	public Title(GamePanel gamePanel) {
		this.gamePanel= gamePanel;
		xPointer= 7*gamePanel.scale*gamePanel.tile+16;
		yPointer= 18*gamePanel.scale*gamePanel.tile+6*gamePanel.scale;
		
		try {
			titleScreen= ImageIO.read(getClass().getResourceAsStream("/Background/stage 0.png"));
			pointer= ImageIO.read(getClass().getResourceAsStream("/Background/pointer.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
/**
 * called by gamePanel's method update, manages the movement of the pointer and triggers gamePanel's method setLevel based on when pressed enter
 */
	public void update(KeyHandler keys) {
		
		if (keys.select== true) {
			gamePanel.playSE(12);
			if (yPointer== 18*gamePanel.scale*gamePanel.tile+6*gamePanel.scale) {
				gamePanel.setLevel(1);
			}
			else if (yPointer== 18*gamePanel.scale*gamePanel.tile+38*gamePanel.scale) {
				gamePanel.setLevel(-2);
			}
			keys.select= false;
		}
		else if (keys.up== true) {
			if (yPointer!= 18*gamePanel.scale*gamePanel.tile+6*gamePanel.scale) {
				gamePanel.playSE(11);
				yPointer-= gamePanel.scale*gamePanel.tile*2;		
			}
			keys.up= false;
		}
		else if (keys.down== true) {
			if (yPointer!= 18*gamePanel.scale*gamePanel.tile+38*gamePanel.scale) {
				gamePanel.playSE(11);
				yPointer+= gamePanel.scale*gamePanel.tile*2;				
			}
			keys.down= false;
		}
	}
/**
 * called by gamePanel's method paintComponent, draws the images selected
 */
	public void draw(Graphics brush) {
		brush.drawImage(titleScreen,0,0,gamePanel.xScreen,gamePanel.yScreen,null);
		brush.drawImage(pointer,xPointer,yPointer,gamePanel.tile*gamePanel.scale,gamePanel.tile*3/2*gamePanel.scale,null);
	}
}
