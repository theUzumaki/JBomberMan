package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * the hidden power ups, their update and draw
 */
public class PowerUp extends Entity {
	BufferedImage[] sprites1, sprites2, sprites3;
	/**
	 * kind of power up loaded
	 */
	String powerUpType;

/**
 * inherits the superconstructor and sets the coordinates, also creates its collider and loads the sprites
 */
	public PowerUp(GamePanel gamePanel, HUD hud, String powerUpType, int level, int x, int y) {
		super(gamePanel, hud, level);
		
		dfX= x + xOffset;
		dfY= y + yOffset + gamePanel.yStage;
		
		kind= "power up";
		this.powerUpType= powerUpType;
		
		// adding to lists and creation of box
		active= false;
		entityBox= new Collider(level, dfX, dfY, kind, gamePanel);
		entityBox.active= false;
		
		imgLoader();
	}
/**
 * called by the gamePanel's method update, checks if the player passes on itself
 */
	public void update() {
		collisionManager(entityBox);

		if (pressed) {
			active= false;
			entityBox.active= false;
			activePowerUp();
		}		
	}
/**
 * called by the gamePanel's method paintComponent, draws the sprite on screen
 */
	public void draw(Graphics brush) {
		if (powerUpType== "bomb up") {
			brush.drawImage(sprites1[0], dfX, dfY, null);
		}
		else if (powerUpType== "skate") {
			brush.drawImage(sprites2[0], dfX, dfY, null);
		}
		else if (powerUpType== "armor") {
			brush.drawImage(sprites3[0], dfX, dfY, null);
		}
	}
/**
 * called by update, triggers the change of stats of the player calling its method activePowerUp
 */
	private void activePowerUp() {
		gamePanel.bomber.activePowerUp(powerUpType);
	}
/**
 * called by update, loads all the sprites and puts them in speified arrays
 */
	private void imgLoader() {
		try {			
			sprites1= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/PowerUps/bomb up.png"))
			};
			imgResizer(sprites1, gamePanel.biggerTile, gamePanel.biggerTile);
			
			sprites2= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/PowerUps/skate.png"))
			};
			imgResizer(sprites2, gamePanel.biggerTile, gamePanel.biggerTile);
			sprites3= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/PowerUps/armor.png"))
			};
			imgResizer(sprites3, gamePanel.biggerTile, gamePanel.biggerTile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
