package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import backgrounds.Stage;
import main.GamePanel;
import main.KeyHandler;

/**
 * the bomberman, its update, draw and animation
 */
public class Player extends Entity{
	Bomb bomb1, bomb2, bomb3, bomb4;
	/**
	 * amount of bombs the player can spawn at once
	 */
	int bombLimiter= 1;
	/**
	 * start coordinate of player
	 */
	int ogX, ogY;
	int bombX, bombY;
	/**
	 * amount of bombs on screen
	 */
	int bombNumber;
	/**
	 * slows down the input receive
	 */
	int bombPlacementTimer= 100;
	/**
	 * used to regulate the flashing of the player after being hit
	 */
	int invulnerabilityTimer, flashingTimer1, flashingTimer2;
	/**
	 * moves the index of the array
	 */
	int flashingOffset;
	/**
	 * stops the update of the player for the victory screen
	 */
	int changeLevelTimer;
	boolean flashing, walking1sound, walking2sound;
	
	BufferedImage[] sprites1;
	BufferedImage[] sprites2;
	BufferedImage[] sprites3;
	BufferedImage[] sprites4;
	BufferedImage[] sprites5;
	
/**
 * inherits the superconstructor, assign the variables, loads the sprites and creates the bombs and the collider for the player
 */
	public Player(GamePanel gamePanel, Stage stage, KeyHandler keys, HUD hud, int playerX, int playerY) {
		super(gamePanel, stage, keys);
		this.hud= hud;
		kind= "player";
		dfX= playerX+xOffset;
		dfY= playerY+gamePanel.yStage;
		ogX= playerX+xOffset;
		ogY= playerY+gamePanel.yStage;
		dfWidth= gamePanel.biggerTile;
		dfHeight= gamePanel.biggerTile+4*gamePanel.scale;
		dfSpeed= 1*gamePanel.scale;
		
		active= true;
		entityBox= new Collider(0, dfX, dfY+yCollOffset-2*gamePanel.scale, kind, gamePanel, keys);
		entityBox.active= true;
		bomb1= new Bomb(gamePanel, hud);
		bomb2= new Bomb(gamePanel, hud);
		bomb3= new Bomb(gamePanel, hud);
		bomb4= new Bomb(gamePanel, hud);
		
		imgLoader();
	}
/**
 * called by the method update, resets the player to his original coordinates and stats (removes the powerUps)
 */
	public void setOgPosition() {
		verse= 1;
		dfX= ogX;
		dfY= ogY;		
		
		if (hit) {			
			hit= false;
			bombLimiter= 1;
			dfSpeed= 1*gamePanel.scale;
		}
		
		if (diyng) {
			dead= true;
		}
	}
/**
 * called by the gamePanel's method update, manages the movement and condition of level passed and death of the player
 */
	public void update() {
		
		/*
		 Updates the status of the player
		 */
		
		if (exit) {
			gamePanel.stopMusic();
			
			if (changeLevelTimer> 200) {				
				setOgPosition();
				gamePanel.setLevel(2);
				exit= false;
				changeLevelTimer= 0;
			}
			else {
				changeLevelTimer++;
			}
		}
		else if (dead) {
			gamePanel.gameOver.setLevel(level);
			gamePanel.setLevel(-1);
			gamePanel.psswrd.passwordStringIntoImages(gamePanel.gameOver.passwordGenerator());
			dead= false;
		}
		else {			
			invulnerabilityManager();
			
			imgSequencer();
			
			movement();
			
			entityBox.moveCollision(dfX, dfY+8*gamePanel.scale);
			collisionManager(entityBox);
			
			bombPlacementTimer++;
		}
	}
/**
 * called by the gamePanel's method paintComponent, draws the sprite selected
 */
	public void draw(Graphics brush) {
		
		brush.drawImage(img,dfX,dfY, null);
	}
/**
 * called by the powerUp's method activePowerUp, change the stats of player based on the power up activated
 * @param type (the type of power up)
 */
	public void activePowerUp(String type) {
		if (type== "bomb up") {
			bombLimiter++;
		}
		else if (type== "skate") {
			dfSpeed+= gamePanel.scale / 3;
		}
		else if (type== "armor") {
			invulnerability= true;
		}
	}
/**
 * called by the update, manages the time of invulnerability after being hit
 */
	private void invulnerabilityManager() {
		if (invulnerability) {
			invulnerabilityTimer++;
		}
		
		if (invulnerabilityTimer> 500) {
			invulnerability= false;
			invulnerabilityTimer= 0;
		}
	}
/**
 * called by the update, manages the sequence of images to display
 */
	private void imgSequencer() {
		
		if (!hit) {			// mechanism of switching images for regular action
			
			if (keys.up || keys.left || keys.down || keys.right) {
				if (imgInterval>= 12) {	

					gamePanel.playSE(13);
					
					if (imgSelector== 0) {
						imgSelector= 2;
					}
					else if (imgSelector== 2) {
						imgSelector= 0;
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
				imgSelector= 1;
			}
		}
		else {				// mechanism of switching images for death sequence
			if (imgInterval> 22) {				
				if (imgSelector< 8) {
					imgSelector++;
				}
				else {
					imgSelector= 2;
				}
				imgInterval= 0;
			}
			else {
				imgInterval++;
			}
		}
	}
/**
 * called by the update, manages the change of coordinates based on the key pressed and also the spawn of bombs
 */
	private void movement() {
		if (!hit) {
			dead= false;
			diyng= false;
			
			if (invulnerability) {		// the flashing offset is used to switch image (between colored and white) when the player respawn
				if (flashingTimer1> 15) {
					flashingOffset= 3;
					flashing= true;
					flashingTimer1= 0;
				}
				else if (flashing) {
					flashingTimer2++;					
				}
			}

			if (keys.up== true) {
				verse= 4;
				dfY-= dfSpeed;
				img= sprites4[imgSelector + flashingOffset];
			}
			if (keys.left== true) {
				verse= 2;
				dfX-= dfSpeed;
				img= sprites2[imgSelector + flashingOffset];
			}
			if (keys.down== true) {
				verse= 1;
				dfY+= dfSpeed;
				img= sprites1[imgSelector + flashingOffset];
			}
			if (keys.right== true) {
				verse= 3;
				dfX+= dfSpeed;
				img= sprites3[imgSelector + flashingOffset];
			}
			if (keys.bomb== true && bombPlacementTimer> 10) {
				bombX= (dfX+(dfWidth/2)) - ((dfX+(dfWidth/2) - xOffset) % gamePanel.biggerTile);
				bombY= (dfY+yOffset) - ((dfY+dfHeight) % gamePanel.biggerTile);
				
				if (!bomb1.active) {
					gamePanel.playSE(8);
					bomb1.setPosition(bombX, bombY+4*gamePanel.scale);
				}
				else if (!bomb2.active && bombLimiter> 1) {
					gamePanel.playSE(8);
					bomb2.setPosition(bombX, bombY+4*gamePanel.scale);
				}
				else if (!bomb3.active && bombLimiter> 2) {
					gamePanel.playSE(8);
					bomb3.setPosition(bombX, bombY+4*gamePanel.scale);
				}
				else if (!bomb4.active && bombLimiter> 3) {
					gamePanel.playSE(8);
					bomb4.setPosition(bombX, bombY+4*gamePanel.scale);
				}
				
				bombNumber++;
				bombPlacementTimer= 0;
			}
			
			if (!keys.right && !keys.left && !keys.down && !keys.up && !keys.bomb) {
				
				switch(verse) {
				case 1:
					img= sprites1[imgSelector + flashingOffset];
					break;
				case 2:
					img= sprites2[imgSelector + flashingOffset];
					break;
				case 3:
					img= sprites3[imgSelector + flashingOffset];
					break;
				case 4:
					img= sprites4[imgSelector + flashingOffset];
					break;
				}
			}
			
			if (invulnerability) {
				if (flashingTimer2> 15) {
					flashingOffset = 0;
					flashing= false;
					flashingTimer2= 0;
				}
				else if (!flashing){
					flashingTimer1++;
				}
				else {
				}
			}
			else {
				flashingOffset= 0;
			}
		}
		
		else {
			if (imgSelector< 7) {
				img= sprites5[imgSelector];
			}
			else {
				imgSelector= 1;
				setOgPosition();
			}
		}
	}
/**
 * called by the gamePanel's method setLevel, adds the player to the current level's entities array
 */
	public void setLevel(int level) {
		this.level= level;
		
		gamePanel.entities.add(this);
		gamePanel.colliders.add(entityBox);
		
		bomb1.setLevel(level);
		bomb2.setLevel(level);
		bomb3.setLevel(level);
		bomb4.setLevel(level);
	}
/**
 * called by the constructor, loads all the sprites in specified arrays
 */
	private void imgLoader() {
		try {
			sprites1= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to bottom-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to bottom-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to bottom-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to bottom-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to bottom-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to bottom-3.png"))
			};
			imgResizer(sprites1, dfWidth, dfHeight);
			
			sprites2= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to left-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to left-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to left-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to left-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to left-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to left-3.png"))
			};
			imgResizer(sprites2, dfWidth, dfHeight);
			
			sprites3= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to right-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to right-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to right-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to right-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to right-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to right-3.png"))
			};
			imgResizer(sprites3, dfWidth, dfHeight);
			
			sprites4= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to top-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to top-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber to top-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to top-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to top-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/flashing/white bomber to top-3.png"))
			};
			imgResizer(sprites4, dfWidth, dfHeight);
			
			sprites5= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber dead-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber dead-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber dead-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber dead-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber dead-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber dead-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/white bomber dead-7.png"))
			};
			imgResizer(sprites5, dfWidth, dfHeight);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}