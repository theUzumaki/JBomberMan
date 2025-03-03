package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * the bombs placed by the player, their update, draw and animation
 */
public class Bomb extends Entity{
	/**
	 * index limiter to not exceed the array
	 */
	int selectorLimiter= 8; // this limiter is needed to not exceed the number of images in the directory, and start the reverse
	/**
	 * sets the num of frames to change sprite
	 */
	int intervalLimiter= 12; // the number of frames each image takes to change will be this number plus one
	/**
	 * time until the bomb deactives itself
	 */
	int lifeTimer= 0;
	int colliderX, colliderY;
	/**
	 * used to change sprite array
	 */
	boolean exploded;
	String source= "/Bomb/bomb ";
	public Collider expBox1, expBox2, expBox3, expBox4; // for the explosion
	
	/**
	 * image variable
	 */
	BufferedImage imgCentre, imgTop, imgLeft, imgRight, imgBottom;
	
	BufferedImage[] sprites1;
	BufferedImage[] sprites2;
	BufferedImage[] sprites3;
	BufferedImage[] sprites4;
	BufferedImage[] sprites5;
	BufferedImage[] sprites6;
/**
 * inherits the superconstructor from Entity and sets the kind of entity to bomb
 */
	public Bomb(GamePanel gamePanel, HUD hud) {
		super(gamePanel, hud);
		kind= "bomb";
		colliderX= dfX-xOffset;
		colliderY= dfY-yOffset-gamePanel.yStage;
		
		imgLoader();
	}
/**
 * called by the gamePanel's method update, manages the update of the entity
 */
	public void update() {
		
		animation();
			
		collisionManager(entityBox);			
		
		if (lifeTimer== 127) {
			makeBomb();
			lifeTimer= 0;
		}
		else {			
			lifeTimer++;
		}
	}
/**
 * called by the gamePanel's method paintComponent, manages the draw of the entity
 */
	public void draw(Graphics brush) {
		if (!exploded) {			
			brush.drawImage(img,dfX,dfY,null);
		}
		else {
			brush.drawImage(imgCentre,dfX,dfY,null);
			if (exploded) {				
				brush.drawImage(imgTop,dfX-gamePanel.biggerTile,dfY-gamePanel.biggerTile,null);
				brush.drawImage(imgLeft,dfX-gamePanel.biggerTile,dfY-gamePanel.biggerTile,null);
				brush.drawImage(imgRight,dfX+gamePanel.biggerTile,dfY-gamePanel.biggerTile,null);
				brush.drawImage(imgBottom,dfX-gamePanel.biggerTile,dfY+gamePanel.biggerTile,null);
			}
		}
	}
/**
 * called by the constructor, depending on the level in which the object is generated adds the object to an entity list used to call the updates
 */
	public void setLevel(int level) {
		int sideOfExplosionCollider= gamePanel.biggerTile - 2 * gamePanel.scale;
		
		gamePanel.entities.add(this);
		
		entityBox= new Collider(level, 0, 0, kind, gamePanel);
		expBox1= new Collider(level, 0, 0, sideOfExplosionCollider, sideOfExplosionCollider, "exploded bomb", gamePanel);
		expBox2= new Collider(level, 0, 0, sideOfExplosionCollider, sideOfExplosionCollider, "exploded bomb", gamePanel);
		expBox3= new Collider(level, 0, 0, sideOfExplosionCollider, sideOfExplosionCollider, "exploded bomb", gamePanel);
		expBox4= new Collider(level, 0, 0, sideOfExplosionCollider, sideOfExplosionCollider, "exploded bomb", gamePanel);

		gamePanel.colliders.add(entityBox);
		gamePanel.colliders.add(expBox1);
		gamePanel.colliders.add(expBox2);
		gamePanel.colliders.add(expBox3);
		gamePanel.colliders.add(expBox4);
	}
/**
 * called by the draw method of the object, manages which images display based on the surroundings and on the status of exploded or not
 */
	private void animation() {
		imgSelector();
		
		if (!exploded) {
			img= sprites1[imgSelector];
		}
		else {
			
			imgCentre= sprites2[imgSelector];
			
			if (!objOnTop) {				
				imgTop= sprites3[imgSelector];
			}
			else {
				imgTop= null;
			}
			if (!objOnTheLeft) {				
				imgLeft= sprites4[imgSelector];
			}
			else {
				imgLeft= null;
			}
			if (!objOnTheRight) {				
				imgRight= sprites5[imgSelector];
			}
			else {
				imgRight= null;
			}
			if (!objUnder) {				
				imgBottom= sprites6[imgSelector];
			}
			else {
				imgBottom= null;
			}
			
		}
	}
/**
 * called by the animation method of the object, manages the sequence of images and therefore the timing of explosion
 */
	private void imgSelector() {
		if (imgInterval> intervalLimiter) {
			if (imgSelector< 7) {
				imgSelector++;
			}
			else {
				makeExplosion();
			}
			imgInterval= 0;
		}
		else {
			imgInterval++;
		}
	}
/**
 * called by the imgSelector method of the object, change the type of entity into exploded bomb and actives all the colliders for the flames of explosion
 */
	private void makeExplosion() {
		gamePanel.playSE(3);
		entityBox.setType("exploded bomb");
		kind= "exploded bomb";
		exploded= true;
		imgSelector= 0;
		intervalLimiter= 6;
				
		expBox1.active= true;
		expBox1.moveCollision(dfX-gamePanel.biggerTile+gamePanel.scale, dfY+gamePanel.scale);
		expBox2.active= true;
		expBox2.moveCollision(dfX+gamePanel.biggerTile+gamePanel.scale, dfY+gamePanel.scale);
		expBox3.active= true;
		expBox3.moveCollision(dfX+gamePanel.scale, dfY-gamePanel.biggerTile+gamePanel.scale);
		expBox4.active= true;
		expBox4.moveCollision(dfX+gamePanel.scale, dfY+gamePanel.biggerTile+gamePanel.scale);
	}
/**
 * called by the update method of the object, resets the type of entity into bomb and sets off all the colliders for the flames of explosion
 */
	private void makeBomb() {
		kind= "bomb";
		entityBox.setType("bomb");
		active= false;
		exploded= false;
		imgSelector= 0;
		intervalLimiter= 12;
		
		entityBox.active= false;
		expBox1.active= false;
		expBox2.active= false;
		expBox3.active= false;
		expBox4.active= false;
		
		objOnTop= false;
		objOnTheRight= false;
		objUnder= false;
		objOnTheLeft= false;
	}
/**
 * called by the superconstructor, loads all the sprites and puts them in specific arrays
 */
	private void imgLoader() {
		try {
			sprites1= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Bomb/bomb 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/bomb 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/bomb 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/bomb 4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/bomb 5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/bomb 6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/bomb 7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/bomb 8.png"))
			};
			imgResizer(sprites1, gamePanel.biggerTile, gamePanel.biggerTile);
			
			sprites2= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 1.png")).getSubimage(16, 16, 16, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 2.png")).getSubimage(16, 16, 16, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 3.png")).getSubimage(16, 16, 16, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 4.png")).getSubimage(16, 16, 16, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 5.png")).getSubimage(16, 16, 16, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 6.png")).getSubimage(16, 16, 16, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 7.png")).getSubimage(16, 16, 16, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 8.png")).getSubimage(16, 16, 16, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 9.png")).getSubimage(16, 16, 16, 16)
			};
			imgResizer(sprites2, gamePanel.biggerTile, gamePanel.biggerTile);
			
			sprites3= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 1.png")).getSubimage(0, 0, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 2.png")).getSubimage(0, 0, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 3.png")).getSubimage(0, 0, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 4.png")).getSubimage(0, 0, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 5.png")).getSubimage(0, 0, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 6.png")).getSubimage(0, 0, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 7.png")).getSubimage(0, 0, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 8.png")).getSubimage(0, 0, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 9.png")).getSubimage(0, 0, 48, 16)
			};
			imgResizer(sprites3, 3*gamePanel.biggerTile, gamePanel.biggerTile);
			
			sprites4= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 1.png")).getSubimage(0, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 2.png")).getSubimage(0, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 3.png")).getSubimage(0, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 4.png")).getSubimage(0, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 5.png")).getSubimage(0, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 6.png")).getSubimage(0, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 7.png")).getSubimage(0, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 8.png")).getSubimage(0, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 9.png")).getSubimage(0, 0, 16, 48)
			};
			imgResizer(sprites4, gamePanel.biggerTile, 3*gamePanel.biggerTile);
			
			sprites5= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 1.png")).getSubimage(32, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 2.png")).getSubimage(32, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 3.png")).getSubimage(32, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 4.png")).getSubimage(32, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 5.png")).getSubimage(32, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 6.png")).getSubimage(32, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 7.png")).getSubimage(32, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 8.png")).getSubimage(32, 0, 16, 48),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 9.png")).getSubimage(32, 0, 16, 48)
			};
			imgResizer(sprites5, gamePanel.biggerTile, 3*gamePanel.biggerTile);
			
			sprites6= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 1.png")).getSubimage(0, 32, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 2.png")).getSubimage(0, 32, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 3.png")).getSubimage(0, 32, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 4.png")).getSubimage(0, 32, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 5.png")).getSubimage(0, 32, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 6.png")).getSubimage(0, 32, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 7.png")).getSubimage(0, 32, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 8.png")).getSubimage(0, 32, 48, 16),
					ImageIO.read(getClass().getResourceAsStream("/Bomb/explosion/explosion 9.png")).getSubimage(0, 32, 48, 16)
			};
			imgResizer(sprites6, 3*gamePanel.biggerTile, gamePanel.biggerTile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
