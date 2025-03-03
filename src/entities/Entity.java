package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import backgrounds.Stage;
import main.GamePanel;
import main.KeyHandler;

/**
 * the father class of all entities, their basic variables and methods: collision managing, interactions, direction sorting, images resizing
 */
public class Entity {
	GamePanel gamePanel;
	KeyHandler keys;
	Stage stage;
	HUD hud;
	Collider entityBox;
	
	// default position and movement
	static boolean invulnerability;
	public int dfX,dfY;
	int dfWidth, dfHeight, widthOffset, heightOffset;
	int xOffset;
	int yOffset;
	int xCollOffset;
	int yCollOffset;
	/**
	 * tells the direction of movement
	 */
	int verse= 1;
	/**
	 * holds the old direction to not sort again it
	 */
	int oldVerse;
	/**
	 * the level to which add the object
	 */
	int level;
	public int dfSpeed;
	
	// default appearances
	BufferedImage img;
	Tomb tomb;
	/**
	 * timer to death sprites sequence and deactivation of the entity
	 */
	int deathTimer;
	
	// bomb and flames boolean variables 
	/**
	 * variable for surroundings
	 */
	boolean objOnTop, objOnTheLeft, objOnTheRight, objUnder;
	/**
	 * tells if the object is to be considere by the gamePanel
	 */
	public boolean active;
	
	// static variables
	/**
	 * amount of total points
	 */
	static int totalScore;
	/**
	 * player lives
	 */
	public static int livesCounter= 5;

	// variables
	/**
	 * sets the num of frames to change sprite
	 */
	int imgInterval= 0;
	/**
	 * index for the sprites array
	 */
	int imgSelector= 1;
	/**
	 * directory for the files
	 */
	String source;
	/**
	 * directory for the files
	 */
	public String folder, subFolder;
	/**
	 * class of the object
	 */
	public String kind;
	/**
	 * states of the entity
	 */
	boolean hit, dead, diyng;
	/**
	 * activated when the player collides with the exit
	 */
	public boolean exit;
	/**
	 * activated when an entity is surrounded and can't move
	 */
	boolean blocked;
	/**
	 * activated when a powerUp finds a collision with the player
	 */
	boolean pressed;
	
/**
 * super constructor for the Player, sets the offset for images display and collider creation
 */
	public Entity(GamePanel gamePanel, Stage stage, KeyHandler keys) {
		
		
		this.gamePanel= gamePanel;
		this.stage= stage;
		this.keys= keys;
		
		xOffset= 3*gamePanel.biggerTile/2;	// the offset for the lateral border, to align on the tile map
		yOffset= 7*gamePanel.scale;			// the offset which adds (other than the hud y) the y of the little border
		xCollOffset= 1*gamePanel.scale;
		yCollOffset= 10*gamePanel.scale;
	}
/**
 * super constructor for the bomb, the flames, and the tomb, adds the entity to the list of entities for that level and sets the specific offsets for images display and collider creation
 */
	public Entity(GamePanel gamePanel, HUD hud) {
		
		this.gamePanel= gamePanel;
		this.hud= hud;
		
		if (level== 1) {
			gamePanel.entities1.add(this);
		}
		else if (level== 2) {
			gamePanel.entities2.add(this);
		}
		else {
			gamePanel.entities1.add(this);
			gamePanel.entities2.add(this);
		}
		
		xOffset= 3*gamePanel.biggerTile/2;	// the offset for the lateral border, to align on the tile map
		yOffset= 7*gamePanel.scale;			// the offset which adds (other than the hud y) the y of the little border
		xCollOffset= 1*gamePanel.scale;
		yCollOffset= 6*gamePanel.scale;
	}
/**
 * Super constructor for the hud, adds the entity to the list of entities for that level and sets the specific offsets for images display and collider creation
 */
	public Entity(GamePanel gamePanel, int level) {
		
		if (level== 1) {
			gamePanel.entities1.add(this);
		}
		else if (level== 2) {
			gamePanel.entities2.add(this);
		}
		
		this.level= level;
		this.gamePanel= gamePanel;
		
		xOffset= 24*gamePanel.scale;
		yOffset= 7*gamePanel.scale;
		xCollOffset= 1*gamePanel.scale;
		yCollOffset= 6*gamePanel.scale;
	}
/**
 * Super constructor for the other entities, adds the entity to the list of entities for that level and sets the specific offsets for images display and collider creation
 */
	public Entity(GamePanel gamePanel, HUD hud, int level) {
		
		if (level== 1) {
			gamePanel.entities1.add(this);
		}
		else if (level== 2) {
			gamePanel.entities2.add(this);
		}
		
		this.level= level;
		this.gamePanel= gamePanel;
		this.hud= hud;
		
		xOffset= 3*gamePanel.biggerTile/2;	// the offset for the lateral border, to align on the tile map
		yOffset= 7*gamePanel.scale;			// the offset which adds (other than the hud y) the y of the little border
		xCollOffset= 1*gamePanel.scale;
		yCollOffset= 2*gamePanel.scale;
	}
/**
 * called by the entities in the method update, returns a boolean of impact, it calls enCollisionManager, plCollisionManager or surroundingsManager based on the request
 * @param subject (the entity of which collision are checked)
 */
	public boolean collisionManager(Collider subject) { // 
		int selector= 0; // to block the selection of the same step back two times in a row
		
		if (subject.type== "player") {		// here is checking collision for the player
			
			for (Collider collider: gamePanel.colliders) {

				if (collider.active) {
					selector= plCollisionManager(subject, collider, selector);
				}
			}
		}
		else if (subject.type== "bomb" ) {				// here is checking in which directions the bomb can draw the explosion

			for (Collider collider: gamePanel.colliders) {
				
				if (collider.active && (collider.type== "wall" || collider.type== "soft wall")) {
					surroundingsManager(subject, collider);
				}
				
			}
		}
		else {					// here is checking collision of the entities that are not the player (soft walls and enemies)
			
			for (Collider collider: gamePanel.colliders) {

				if (collider.active) {					
					selector= enCollisionManager(subject, collider, selector);			
					
					if (subject.type!= "soft wall" && subject.type!= "exploded bomb" && subject.type!= "power up") {
						// checking if the entity is blocked from moving
						if (collider.type!= "flames" && collider.type!= "puropen" && collider.type!= "senshiyan") {
							surroundingsManager(subject, collider);
						}
					}
				}
			}
		}
		if (selector!= 0) {
			return true;
		}
		else {
			return false;
		}
		
	}
/**
 * called in other entities method when needed to active another entity, sets the position of the entity that calls the method and sets it on active
 */
	public void setPosition(int x, int y) {
		active= true;
		dfX= x;
		dfY= y;

		if (entityBox!= null) {			
			entityBox.active= true;
			entityBox.moveCollision(x, y);
		}		
	}
/**
 * called by the method update of enemies manages their surroundings, it's utilized to reset the state of the surroundings near the entity calling the method
 */
	protected void surroundedStateManager() {
		if (objOnTop && objUnder && objOnTheRight && objOnTheLeft) {
			blocked= true;
			objOnTop= false; objUnder= false; objOnTheRight= false; objOnTheLeft= false;
			verse= 1;
		}
		else if (blocked) {
			blocked= false;
		}
		else {
			objOnTop= false; objUnder= false; objOnTheRight= false; objOnTheLeft= false;
		}
	}
/**
 * called by the imgLoader method of the entities, scales the images before run time
 * @param sprites (Array of BufferedImage to resize)
 */
	protected void imgResizer(BufferedImage[] sprites, int width, int height) {	// scales immediately the images to avoid doing it at runtime
		for (int i= 0; i< sprites.length; i++) {
			BufferedImage scaledImage= new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g= scaledImage.createGraphics();
			g.drawImage(sprites[i], 0, 0, width, height, null);
			sprites[i]= scaledImage;
		}
	}
/**
 * called by the update method of enemies, returns a randomized direction for the entity calling the method
 * @param verse (old verse of movement, used to not sort again the same one)
 * @param listOfTries (used to prevent an infinite loop if entity is blocked)
 */
	protected int directionSorter(int verse, int[] listOfTries) {
		int tries= 1;
		
		while (isIntInArray(verse, listOfTries)) {
			int criticalCounter= 0;			// prevent infinite loop if entity is blocked
			
			verse= (int)(Math.random()*4)+1;
			if (tries> 4) {
				for (int i= 0; i< 4; i++) {
					if (listOfTries[i]== i+1) {
						criticalCounter++;
					}
				}
				if (criticalCounter== 4) {
					break;
				}
			}
			tries++;
		}
		return verse;
	}
/**
 * called by the directionSorter, returns true if the specified int is in the specified array, used for directionSorter
 * @param element (int to search for)
 * @param array (array to search)
 */
	private boolean isIntInArray(int element, int[] array) {
		if (element> 0 && array[element-1]== element) {
			return true;
		}
		return false;
	}
/**
 * called by the collisionManager, calls the player's collider's method checkPlCollision used to check collision based on the situation, manages invulnerability, hits and returns an int linked to the side of impact
 * @param subject (used to pass the collider box of player)
 * @param collider (used to pass the collider box of the other entity)
 * @param selector (prevents to catch two consequent impacts on the same collision)
 */
	private int plCollisionManager(Collider subject, Collider collider, int selector) {
		String impact;
		
		if (invulnerability && (collider.type== "puropen" || collider.type== "senshiyan" || collider.type== "flames")) {
			// avoids colliding with enemies when immune
		}
		else if (collider.type== "puropen" || collider.type== "senshiyan") {
			impact= subject.checkPlCollision(collider);
			
			if (impact!= null  &! invulnerability) {
				hit= true;
				gamePanel.playSE(4);
				invulnerability= true;
				if (livesCounter> 0) {							
					livesCounter--;
				}
				else {
					diyng= true;
				}
			}
		}
		else if (collider.type== "flames" || collider.type== "exploded bomb") {
			impact= subject.checkOverlaying(collider);
						
			if (impact!= null  &! invulnerability) {
				hit= true;
				gamePanel.playSE(4);
				invulnerability= true;
				if (livesCounter> 0) {							
					livesCounter--;
				}
				else {
					diyng= true;
				}
			}
		}
		else if (collider.type== "bomb") {
			// avoids the player to impact the bomb
		}
		else if (collider.type== "exit") {
			impact= subject.checkPlCollision(collider);
			
			if (impact!= null) {
				gamePanel.playSE(9);
				exit= true;						
			}
		}
		else if (collider.type== "power up") {
			// avoids the player to impact the power up
		}
		else {
			impact= subject.checkPlCollision(collider);
			
			if (impact== "on top" && selector!=1){
				dfY+=dfSpeed;
				selector= 1;
			}
			if (impact== "on the left" && selector!=2){
				dfX+=dfSpeed;
				selector= 2;
			}
			if (impact== "on bottom" && selector!=3){
				dfY-=dfSpeed;
				selector= 3;
			}
			if (impact== "on the right" && selector!=4){
				dfX-=dfSpeed;
				selector= 4;
			}
		}
		return selector;
	}
/**
 * called by the collisionManager, calls the entitie's (except the player) collider's methods used to check collision based on the situation and returns an int linked to the side of impact
 * @param subject (used to pass the collider box of entity)
 * @param collider (used to pass the collider box of the other entity)
 * @param selector (prevents to catch two consequent impacts on the same collision)
 */
	private int enCollisionManager(Collider subject, Collider collider, int selector) {
		String impact;			
		
		if (subject.equals(collider)) {					
			// avoids auto check
		}
		else if (subject.type== "power up" && collider.type== "player") {
			impact= subject.checkOverlaying(collider);
			
			if (impact!= null) {
				gamePanel.playSE(6);
				pressed= true;
			}
		}
		else if (collider.type== "flames" || collider.type== "exit") {
			// collision the enemies don't have to catch
		}
		else if (collider.type== "exploded bomb") {
			impact= subject.checkOverlaying(collider);	
			
			if (impact!= null) {
				hit= true;
			}
		}
		else if (collider.type== "player") {
			// avoids to check on the player to permit the player to check on hit by enemies and to pass over the unexploded bomb
		}
		else if (collider.type== "power up") {
			// avoids the enemies to impact the power up
		}
		else {
			impact= subject.checkEnCollision(collider);

			if (impact== "on top" && selector!=1){
				dfY+=dfSpeed;
				selector= 1;
			}
			if (impact== "on the left" && selector!=2){
				dfX+=dfSpeed;
				selector= 2;
			}
			if (impact== "on bottom" && selector!=3){
				dfY-=dfSpeed;
				selector= 3;
			}
			if (impact== "on the right" && selector!=4){
				dfX-=dfSpeed;
				selector= 4;
			}
			
			
		}
		return selector;
	}
/**
 * called by the collisionManager, calls the enemy's collider's method checkSurroundings to review the surroundings and manages the values of the entity about the circumnstances
 * @param subject
 * @param collider
 */
	private void surroundingsManager(Collider subject, Collider collider) {
		String surroundings= "";
		
		if (collider.type!= "exit") {			
			surroundings= subject.checkSurroundings(collider);
		}
		
		if (surroundings== "on the right") {
			objOnTheRight= true;
		}
		else if (surroundings== "on the left") {	
			objOnTheLeft= true;
		}
		else if (surroundings== "on top") {	
			objOnTop= true;
		}
		else if (surroundings== "under") {	
			objUnder= true;
		}
	}
	public void update() {
		// TODO Auto-generated method stub
		
	}
	public void draw(Graphics brush) {
		// TODO Auto-generated method stub
		
	}
}