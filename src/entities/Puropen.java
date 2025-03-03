package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * the red helicopter enemies, their update, draw and animation
 */
public class Puropen extends Entity {
	BufferedImage[] sprites1;
	BufferedImage[] sprites2;
	BufferedImage[] sprites3;
	BufferedImage[] sprites4;
	BufferedImage[] sprites5;
	BufferedImage[] sprites6;
	BufferedImage[] sprites7;
	BufferedImage[] sprites8;
	BufferedImage[] sprites9;
/**
 * inherits the superconstructor, loads the sprites and creates its collider and future tomb
 */
	public Puropen(GamePanel gamePanel, HUD hud, int level, int x, int y) {
		super(gamePanel, hud, level);
		kind= "puropen";
		heightOffset= 7 * gamePanel.scale;
		dfWidth= gamePanel.biggerTile;
		dfHeight= gamePanel.biggerTile + heightOffset;
		dfX= x + xOffset;
		dfY= y + gamePanel.yStage + yOffset - heightOffset;
		dfSpeed= 2 * gamePanel.scale / 3;
		
		active= true;
		tomb= new Tomb(gamePanel, hud, "puropen", level, dfX, dfY, dfWidth, dfHeight);
		entityBox= new Collider(level, dfX, dfY + heightOffset, kind, gamePanel);
		entityBox.active= true;
		imgLoader();
	}
/**
 * called by the gamePanel's method update, runs the surroundings and collision checks, sorts the direction of the entity, manages the image sequence and actives the tomb if the entity dies
 */
	public void update() {
		int tries= 0; // times the entity can try to change direction
		int[] listOfTries= {0, 0, 0, 0};
		
		surroundedStateManager(); // check of the blocked state in which the entity doesn't move
		
		if (deathTimer> 10) {
			totalScore+= 100;
			gamePanel.playSE(5);
			tomb.setPosition(dfX, dfY);
						
			active= false;
			entityBox.active= false;
		}

		// timing of animation
		imgSequencer();
		
		// moving
		movement();
		
		// collision with sorting
		while (collisionManager(entityBox) && !blocked) {
				listOfTries[verse-1]= verse;
				int criticalCounter= 0;
				
				verse= directionSorter(verse, listOfTries);
				
				if (tries> 5) {					
					for (int i= 0; i< 4; i++) {
						if (listOfTries[i]== i+1) {
							criticalCounter++;
						}
					}
					if (criticalCounter== 4) {
						break;
					}
				}

				movement();
				tries++;
		}
	}
/**
 * called by the gamePanel's method paintComponent, draws the sprite selected
 */
	public void draw(Graphics brush) {
		brush.drawImage(img,dfX,dfY,null);
	}
/**
 * called by the constructor, loads all the sprites and puts them in specified arrays
 */
	private void imgLoader() {
		try {
			sprites1= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen 4.png"))
			};
			imgResizer(sprites1, dfWidth, dfHeight);
			
			sprites2= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen back 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen back 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen back 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen back 4.png"))
			};
			imgResizer(sprites2, dfWidth, dfHeight);
			
			sprites3= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen right 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen right 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen right 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen right 4.png"))
			};
			imgResizer(sprites3, dfWidth, dfHeight);
			
			sprites4= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen left 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen left 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen left 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/puropen left 4.png"))
			};
			imgResizer(sprites4, dfWidth, dfHeight);
			
			sprites5= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen 4.png"))
			};
			imgResizer(sprites5, dfWidth, dfHeight);
			
			sprites6= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen back 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen back 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen back 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen back 4.png"))
			};
			imgResizer(sprites6, dfWidth, dfHeight);
			
			sprites7= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen right 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen right 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen right 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen right 4.png"))
			};
			imgResizer(sprites7, dfWidth, dfHeight);
			
			sprites8= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen left 1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen left 2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen left 3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Puropen/hit/puropen left 4.png"))
			};
			imgResizer(sprites8, dfWidth, dfHeight);
			
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void imgSequencer() {
		if (!hit) {			
			if (imgInterval>= 0) {
				if (imgSelector< 3) {
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
		else {	// stops the sequence of images
			deathTimer++;
		}
	}
	private void movement() {
		if (!hit) {			
			if (verse== 1) { // down
				dfY+=dfSpeed;
				img= sprites1[imgSelector];
			}
			else if (verse== 2) { // up
				dfY-=dfSpeed;
				img= sprites2[imgSelector];
			}
			else if (verse== 3) { // right
				dfX+=dfSpeed;
				img= sprites3[imgSelector];
			}
			else if (verse== 4) { // left
				dfX-=dfSpeed;
				img= sprites4[imgSelector];
			}
		}
		else {		
			if (verse== 1) { // down
				img= sprites5[imgSelector];
			}
			else if (verse== 2) { // up
				img= sprites6[imgSelector];
			}
			else if (verse== 3) { // right
				img= sprites7[imgSelector];
			}
			else if (verse== 4) { // left
				img= sprites8[imgSelector];
			}
		}

		entityBox.moveCollision(dfX, dfY + heightOffset);
	}
}
