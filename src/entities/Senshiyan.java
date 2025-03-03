package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * the green tank enemies, their update, draw and animation
 */
public class Senshiyan extends Entity{
	/**
	 * regulates the change of action: walking, aiming, firing
	 */
	int timer;
	/**
	 * index limiter to not exceed the array
	 */
	int selectorLimiter= 1; // this limiter serves to not exceed the number of images in the directory, and start the reverse
	/**
	 * sets the num of frames to change sprite
	 */
	int intervalLimiter= 2; // the number of frames each image takes to change will be this number plus one
	/**
	 * value for the flames
	 */
	int imgX, imgY, imgWidth, imgHeight, imgBoxXOffset, imgBoxYOffset;			// value to print on screen the flames
	/**
	 * the level to which add the object
	 */
	int level;
	/**
	 * amount of times it takes to beat the enemy
	 */
	int lives= 2;
	/**
	 * used in the switch of sprites
	 */
	boolean aiming, firing, fireStart, fireEnd, hitTaken;
	Collider fireBox;
	Flames flames;
	BufferedImage imgFire1, imgFire2, imgFire3, imgFire4, imgFire5, imgFire6;
	
	BufferedImage[] sprites1;
	BufferedImage[] sprites2;
	BufferedImage[] sprites3;
	BufferedImage[] sprites4;
	BufferedImage[] sprites29;
	BufferedImage[] sprites30;
	BufferedImage[] sprites31;
	BufferedImage[] sprites32;

/**
 * inherits the superconstructor, creates the flames and the future tomb, loads all the sprites
 */
	public Senshiyan(GamePanel gamePanel, HUD hud, int level, int x, int y) {
		super(gamePanel, hud, level);
		this.level= level;
		active= true;
		kind= "senshiyan";
		
		widthOffset= 4 * gamePanel.scale;
		heightOffset= 8 * gamePanel.scale;
		dfWidth= gamePanel.biggerTile + widthOffset;
		dfHeight= gamePanel.biggerTile + heightOffset;
		
		dfX= x + xOffset - (widthOffset / 2);
		dfY= y + gamePanel.yStage + yOffset - heightOffset;
		dfSpeed= 1*gamePanel.scale/3;
		
		tomb= new Tomb(gamePanel, hud, "senshiyan", level, dfX, dfY, dfWidth, dfHeight);
		flames= new Flames(gamePanel, hud, level);
		entityBox= new Collider(level, dfX + (widthOffset / 2), dfY + heightOffset, kind, gamePanel);
		entityBox.active= true;
		
		imgLoader();
	}
/**
 * called by the gamePanel's method update, runs the surroundings and collision checks, sorts the direction of the entity, manages the image sequence and actives the tomb if the entity dies
 */
	public void update() {
		int tries= 0; // times the entity can try to change direction
		int[] listOfTries= {0, 0, 0, 0};
		
		surroundedStateManager();	// check of the blocked state in which the entity doesn't move
		
		if (deathTimer> 10) {
			if (lives== 0) {				
				totalScore+= 1600;
				gamePanel.playSE(5);
				tomb.setPosition(dfX, dfY);
				
				active= false;
				entityBox.active= false;
			}
			else {
				hit= false;
				hitTaken= false;
				deathTimer= 0;
			}
		}
		
		// timing of animation
		animation();
		
		// moving
		movement();
		
		// collision with sorting
		// the ghost movement is strictly of Senshiyan for their slow speed which interferes with the collision check
		ghostMovementAdder();
		while (collisionManager(entityBox) && !blocked) {
			if (verse!= 0) {
				listOfTries[verse-1]= verse;				
			}
			int criticalCounter= 0;

			ghostMovementRemover();
			
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
			ghostMovementAdder();
			
			tries++;
		}
		ghostMovementRemover();
	}
/**
 * called by the gamePanel's method paintComponent, draws the image selected and actives the flames if the senshiyan is in firing state
 */
	public void draw(Graphics brush) {
		brush.drawImage(img,dfX,dfY,null);
		if (firing) {	// enables the drawing of the fire png
			if (!hit) {				
				switch (oldVerse) {
				case 1:		// down
					imgWidth= gamePanel.biggerTile-9*gamePanel.scale;
					imgHeight= 4*gamePanel.biggerTile-2*gamePanel.scale;
					imgX= dfX;
					imgY= dfY+gamePanel.biggerTile;
					imgBoxXOffset= 4*gamePanel.scale;
					imgBoxYOffset= 2*gamePanel.scale;
					break;
				case 2:		// up
					imgWidth= gamePanel.biggerTile-9*gamePanel.scale;
					imgHeight= 4*gamePanel.biggerTile-4*gamePanel.scale;
					imgX= dfX;
					imgY= dfY-imgHeight-5*gamePanel.scale;
					imgBoxXOffset= 4*gamePanel.scale;
					imgBoxYOffset= 3*gamePanel.scale;
					break;
				case 3:		// right
					imgWidth= 4*gamePanel.biggerTile-2*gamePanel.scale;
					imgHeight= gamePanel.biggerTile-3*gamePanel.scale;
					imgX= dfX+gamePanel.biggerTile;
					imgY= dfY;
					imgBoxXOffset= 0;
					imgBoxYOffset= gamePanel.biggerTile/3+4*gamePanel.scale;
					break;
				case 4:		// left
					imgWidth= 4*gamePanel.biggerTile-2*gamePanel.scale;
					imgHeight= gamePanel.biggerTile-3*gamePanel.scale;
					imgX= dfX-imgWidth;
					imgY= dfY;
					imgBoxXOffset= 0;
					imgBoxYOffset= gamePanel.biggerTile/3+4*gamePanel.scale;
					break;
				}
			}
			else {
				flames.active= false;
				flames.entityBox.active= false;
			}
		}
		
		// creation of colliding box for the flames, it is constructed here because only at this time the value of img are known
		if (fireStart) {
			flames.setPosition(imgX + (widthOffset / 2), imgY);
			flames.entityBox.setValues(imgX + imgBoxXOffset + (widthOffset / 2), imgY + imgBoxYOffset, imgWidth, imgHeight);
			flames.oldVerse= oldVerse;
			fireStart= false;
		}
		else if (fireEnd) {
			flames.active= false;
			flames.entityBox.active= false;
			fireEnd= false;
		}
			
	}
/**
 * called by update, manages the image sequence and the timing of actions (aiming, firing, moving9
 */
	private void animation() {
		if (imgInterval>= intervalLimiter) {
			imgInterval= 0;
			imgSequencer();	
			actionTimer();
		}
		else {
			imgInterval++;
		}
	}
/**
 * called by animation, manages the sequence of sprites
 */
	private void imgSequencer() {
		if (!hit && !blocked) {			
			if (imgSelector< selectorLimiter) {
				imgSelector++;
			}
			else {
				if (aiming) {
					imgSelector= 1;
				}
				else if (firing) {
					imgSelector= 0;
				}
				else {
					imgSelector= 0;
				}
			}
		}
		else if (blocked){
			
		}
		else {
			if (!hitTaken) {
				lives--;
				hitTaken= true;
			}
			
			deathTimer++;
		}
	}
/**
 * called by animation, manages the timing of the actions of the senshiyan (aiming, firing, moving) 
 */
	private void actionTimer() {
		if (!hit) {
			if (firing) {
				if (timer> 40) {
					timer= 0;
					firing= false;
					fireEnd= true;
					selectorLimiter= 1;
					intervalLimiter= 2;
				}
				else {
					timer++;
				}
			}
			else if (aiming) {
				if (timer> 7) {
					timer= 0;
					firing= true;
					aiming= false;
					fireStart= true;
					imgSelector= 1;
					selectorLimiter= 4;
					intervalLimiter= 4;
				}
				else {
					timer++;
				}
			}
			else {
				if (timer> 100 && !blocked) {
					timer= 0;
					aiming= true;
					oldVerse= verse;
					verse= 0;
					imgSelector= 1;
					selectorLimiter= 8;
					intervalLimiter= 3;
				}
				else {
					timer++;
				}
			}
		}
		else {
			timer= 0;
			firing= false;
			fireEnd= true;
			selectorLimiter= 1;
			intervalLimiter= 2;
		}
	}
/**
 * called by update, moves the coordinates of senshiyan and manages when hit
 */
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
			else if (verse== 0) { // bomb
				if (aiming) {			// exit the countdown and explosion, restarts to move
					switch (oldVerse) {
					case 1:
						img= sprites1[imgSelector];
						break;
					case 2:
						img= sprites2[imgSelector];
						break;
					case 3:
						img= sprites3[imgSelector];
						break;
					case 4:
						img= sprites4[imgSelector];
						break;
					}
				}
				else if (!firing){
					verse= oldVerse;
				}
			}
		}
		else {
			if (verse== 0) {
				verse= oldVerse; // if hit while firing
				if (lives== 0) {					
					gamePanel.colliders.remove(fireBox);
				}
			}
			
			if (verse== 1) { // down
				img= sprites29[imgSelector];
			}
			else if (verse== 2) { // up
				img= sprites30[imgSelector];
			}
			else if (verse== 3) { // right
				img= sprites31[imgSelector];
			}
			else if (verse== 4) { // left
				img= sprites32[imgSelector];
			}
		}

		entityBox.moveCollision(dfX + (widthOffset / 2), dfY + heightOffset);
	}
/**
 * called by update, adds a phantom step to improve the collision check due to the slow movement of senshiyan
 */
	private void ghostMovementAdder() {
		if (verse== 1) { // down
			dfY+=1;
		}
		else if (verse== 2) { // up
			dfY-=1;
		}
		else if (verse== 3) { // right
			dfX+=1;
		}
		else if (verse== 4) { // left
			dfX-=1;
		}
		entityBox.moveCollision(dfX + (widthOffset / 2), dfY + heightOffset);
	}
/**
 * called by update, removes a phantom step added before to improve the collision check due to the slow movement of senshiyan
 */
	private void ghostMovementRemover() {
		if (verse== 1) { // down
			dfY-=1;
		}
		else if (verse== 2) { // up
			dfY+=1;
		}
		else if (verse== 3) { // right
			dfX-=1;
		}
		else if (verse== 4) { // left
			dfX+=1;
		}
		entityBox.moveCollision(dfX + (widthOffset / 2), dfY + heightOffset);
	}
/**
 * called by the constructor, loads all the sprites and puts them in specified arrays
 */
	private void imgLoader() {
		try {
			sprites1= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan down-9.png"))
			};
			imgResizer(sprites1, dfWidth, dfHeight);
			
			sprites2= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan up-9.png"))
			};
			imgResizer(sprites2, dfWidth, dfHeight);
			
			sprites3= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan right-9.png"))
			};
			imgResizer(sprites3, dfWidth, dfHeight);
			
			sprites4= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/Senshiyan left-9.png"))
			};
			imgResizer(sprites4, dfWidth, dfHeight);
			
			sprites29= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan down-9.png"))
			};
			imgResizer(sprites29, dfWidth, dfHeight);
			
			sprites30= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan up-9.png"))
			};
			imgResizer(sprites30, dfWidth, dfHeight);
			
			sprites31= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan right-9.png"))
			};
			imgResizer(sprites31, dfWidth, dfHeight);
			
			sprites32= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-5.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-6.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-7.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-8.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/hit/Senshiyan left-9.png"))
			};
			imgResizer(sprites32, dfWidth, dfHeight);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
