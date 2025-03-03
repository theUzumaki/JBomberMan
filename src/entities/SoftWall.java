package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * the destructible walls, their update, draw and animation
 */
public class SoftWall extends Entity{
	/**
	 * the level to which add the object
	 */
	int level;
	int width, height;
	/**
	 * index of a second array other than the standard
	 */
	int imgSelector2;		// to load the sequence of explosion
	/**
	 * changes the kind of sprite loaded for this softWall
	 */
	boolean powerUp, shadow;
	/**
	 * changes the kind of sprite loaded for this softWall
	 */
	String lenght;
	/**
	 * the hidden power up (null if not hidden)
	 */
	PowerUp pwrUp;
	
	BufferedImage exitCover;
	BufferedImage[] sprites1;
	BufferedImage[] sprites2;
	BufferedImage[] sprites3;
	BufferedImage[] sprites4;

/**
 * inherits the superconstructor, loads the sprites and sets values
 * @param cover (a boolean to sign if the softwall is hiding the exit, if true loads also a tile to cover it)
 * @param powerUp (a boolean to sign if the softwall is hiding a power up)
 * @param shadow (a boolean to sign if the tile sprite to load is the one with the shadow or not)
 * @param lenght (a string that tells which sprite to load between the ones with walls below them or not)
 * @param pwrUp (type of power up hide)
 */
	public SoftWall(int level, int x, int y, boolean cover, boolean powerUp, boolean shadow, String lenght, GamePanel gamePanel, PowerUp pwrUp) {
		super(gamePanel, null, level);
		this.shadow= shadow;		// if the tile cover of the exit has a wall over it then it needs a tile cover with shadow
		this.powerUp= powerUp;
		this.pwrUp= pwrUp;

		kind= "soft wall";
		dfX= x + xOffset;
		dfY= y + gamePanel.yStage + yOffset;
		height= gamePanel.tile * 2 * gamePanel.scale;		
		
		// size of soft wall
		if (lenght== "long") {
			// differentiates between soft wall with walls under them and not
			height= (gamePanel.tile * 2 + gamePanel.scale)*gamePanel.scale;
		}
		
		// adding to lists and creation of box
		active= true;
		entityBox= new Collider(level, dfX, dfY, kind, gamePanel);
		entityBox.active= true;
		
		imgLoader();
		
		// loading the cover if true
		getCover(cover);
		
	}
/**
 * called by gamePanel's method update, runs the collision check, manages the images sequence and activates the power up if hit and the softwall is hiding one
 */
	public void update() {
		if (deathTimer== 1) {
			active= false;
			entityBox.active= false;
			
			if (powerUp) {
				pwrUp.active= true;
				pwrUp.entityBox.active= true;
			}
		}
		
		if (!hit) {			
			if (lenght== "long") {
				img= sprites1[imgSelector];
			}
			else {
				img= sprites2[imgSelector];
			}
			
			if (imgInterval>=6) {
				if (imgSelector<3) {
					imgSelector++;
				}
				else {
					imgSelector= 0;
				}
				imgInterval= 0;
			}
			else {
				imgInterval++;
			}
		}
		
		else {
			if (imgInterval>= 10) {
				if (imgSelector2<4) {
					imgSelector2++;
				}
				else {
					deathTimer= 1;
				}
				imgInterval= 0;
			}
			else {
				imgInterval++;
			}
			
			img= sprites4[imgSelector2];
		}
		
		collisionManager(entityBox);
	}
/**
 * called by gamePanel's method paintComponent, draws the sprites
 */
	public void draw(Graphics brush) {
		brush.drawImage(exitCover, dfX, dfY, null);
		brush.drawImage(img, dfX, dfY, null);
	}
/**
 * called by constructor, if cover is true loads the tile sprite to hide the exit
 */
	private void getCover(boolean cover) {
		if (cover) {
			if (shadow) {
				exitCover= sprites3[1];
				}
			else {
				exitCover= sprites3[0];
			}
		}
	}
/**
 * called by constructor, loads all the sprites and puts them in specified arrays
 */
	private void imgLoader() {
		try {			
			sprites1= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall 4.png"))
			};
			imgResizer(sprites1, gamePanel.biggerTile, height);
			
			sprites2= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall 1(short).png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall 2(short).png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall 3(short).png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall 4(short).png"))
			};
			imgResizer(sprites2, gamePanel.biggerTile, gamePanel.biggerTile);
			
			sprites3= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Background/tile.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/tile (shadow).png"))
			};
			imgResizer(sprites3, gamePanel.biggerTile, height);
			
			sprites4= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall explosion/1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall explosion/2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall explosion/3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall explosion/4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Background/soft wall explosion/5.png"))
			};
			imgResizer(sprites4, gamePanel.biggerTile, gamePanel.biggerTile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
