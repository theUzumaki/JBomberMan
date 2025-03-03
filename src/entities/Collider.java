package entities;

import main.GamePanel;
import main.KeyHandler;

import java.util.stream.IntStream;

/**
 * the linked boxes to the entities, the ones being checked for collision, contains also the basic methods of collision checking
 */
public class Collider {
	GamePanel gamePanel;
	KeyHandler keys;
	
	/**
	 * stream of coordinates to check overlaying
	 */
	IntStream range;
	boolean orizontalAxis, verticalAxis;
	
	public int x, y, width, height;
	int leftSide, rightSide, top, bottom, xCenter, yCenter;
	int xOffset, yOffset;
	/**
	 * tells if the collider is to be controlled or not
	 */
	public boolean active;
	
	/**
	 * to which object the collider belongs
	 */
    public String type;
/**
 * used for the borders, explosions and flames, assigns values passed and adds the collider to an array containing all the collider for the specified level
 * @param type (the class of the collider's object) 
 */
    public Collider(int level, int x, int y, int width, int height, String type, GamePanel gamePanel){	// borders, explosions and flames

    	this.gamePanel= gamePanel;
        this.x= x;
        this.y= y;
        this.type = type;
        this.width= width;
        this.height= height;
        
        if (level== 1) {
        	gamePanel.colliders1.add(this);
        }
        else if (level== 2) {
        	gamePanel.colliders2.add(this);
        }
        else {
        	gamePanel.colliders1.add(this);
        	gamePanel.colliders2.add(this);
        }
    }
/**
 * used for the player, assigns values passed
 * @param level
 * @param x
 * @param y
 * @param type
 * @param gamePanel
 * @param keys
 */
    public Collider(int level, int x, int y, String type, GamePanel gamePanel, KeyHandler keys){	// player
    	xOffset= 24*gamePanel.scale;
    	yOffset= 7*gamePanel.scale; 	
    	
    	this.gamePanel= gamePanel;
    	this.keys= keys;
        this.x= x+xOffset;
        this.y= y+yOffset+gamePanel.yStage;
        this.type = type;
        width= gamePanel.biggerTile-gamePanel.scale;
        height= gamePanel.biggerTile-gamePanel.scale;
    }
/**
 * used for enemies, soft walls, power ups and bombs, assigns values passed and adds the collider to an array containing all the collider for the specified level
 * @param type (the class of the collider's object)
 */
    public Collider(int level, int x, int y, String type, GamePanel gamePanel){		// enemies, soft walls, power ups and bombs
    	this.gamePanel= gamePanel;
        this.x= x;
        this.y= y;
        this.type = type;
        width= gamePanel.biggerTile;
        height= gamePanel.biggerTile;

        if (level== 1) {
        	gamePanel.colliders1.add(this);
        }
        else if (level== 2) {
        	gamePanel.colliders2.add(this);
        }
        else {
        	gamePanel.colliders1.add(this);
        	gamePanel.colliders2.add(this);
        }
    }
/**
 * called by the entity's method enCollisionManager, sets the coordinates of the box's sides and calls a specified method that checks overlapping with the entity which calls the method
 * @param box (collider box to check on other than the one calling the method)
 */
    public String checkOverlaying(Collider box) {
    	
    	orizontalAxis= false;
    	verticalAxis= false;

    	leftSide= x;
        rightSide= x+width;
        xCenter= (leftSide+rightSide)/ 2;
        
        top= y;
        bottom= y+height;
        yCenter= (top+bottom)/ 2;
        
        range= IntStream.rangeClosed(box.x, box.x+box.width);
        range.forEach(n -> rangeEnclosingChecker(n, leftSide, rightSide, "x"));
        
        range= IntStream.rangeClosed(box.y, box.y+box.height);
        range.forEach(n -> rangeEnclosingChecker(n, top, bottom, "y"));
        
        
        if (orizontalAxis && verticalAxis) {
        	return "impact";
        }
    	return null;
    }
/**
 * called by checkFlameCollision, checks overlapping
 * @param n (coordinate being controlled)
 * @param upperLimit (left side or top of the box)
 * @param bottomLimit (right side or bottom of the box)
 * @param axis (tells which axis it's checking)
 */
    private void rangeEnclosingChecker(int n, int upperLimit, int bottomLimit, String axis) {
    	
    	if (upperLimit< n && n< bottomLimit) {
    		if (axis== "x") {    			
    			orizontalAxis= true;
    		}
    		else if (axis== "y") {    			
    			verticalAxis= true;
    		}
    		range.close();
	    }
    	else {
    		
    	}
    }
/**
 * called by the entity's method enCollisionManager, checks collision and the side of it, which returns
 * @param box (collider box to check on other than the one calling the method)
 */
    public String checkEnCollision(Collider box){	// collision checking for enemies
    	
        leftSide= x;
        rightSide= x+width;
        top= y;
        bottom= y+height;
        
        int leftOfObstacle= box.x;
        int rightOfObstacle= box.x+box.width;
        int topOfObstacle= box.y;
        int bottomOfObstacle= box.y+box.height;

        boolean collidingBottomRight= false;
        boolean collidingTopRight= false;
        boolean collidingBottomLeft= false;
        boolean collidingTopLeft= false;
        
        String collision= null;
        
        // corner of collision
        // the positioning of the sides reflects the real one (x-y -> x is on the left of y; bottom-top -> bottom is above top)
        if (leftOfObstacle< rightSide && rightSide< rightOfObstacle){
            if (topOfObstacle< top && top< bottomOfObstacle){
                collidingTopRight= true;
            }
            else if (topOfObstacle< bottom && bottom< bottomOfObstacle){
                collidingBottomRight= true;
            }
        }
        if (leftOfObstacle< leftSide && leftSide< rightOfObstacle){
        	if (topOfObstacle< top && top< bottomOfObstacle){
                collidingTopLeft= true;
            }
            else if (topOfObstacle< bottom && bottom< bottomOfObstacle){
                collidingBottomLeft= true;
            }
        }
        
        // collision with allignated sides
        if (leftOfObstacle== leftSide && rightOfObstacle== rightSide && top< topOfObstacle && topOfObstacle< bottom) {
        	collision= "on bottom";
        	return  collision;
        }
        else if (leftOfObstacle== leftSide && rightOfObstacle== rightSide && topOfObstacle< top && top< bottomOfObstacle) {
        	collision= "on top";
        	return  collision;
        }
        else if (topOfObstacle== top && bottomOfObstacle== bottom && leftOfObstacle< leftSide && leftSide< rightOfObstacle) {
        	collision= "on the left";
        	return  collision;
        }
        else if (topOfObstacle== top && bottomOfObstacle== bottom && leftSide< leftOfObstacle && leftOfObstacle< rightSide) {
        	collision= "on the right";
        	return  collision;
        }
        
        // inside collision
        if (leftSide<= leftOfObstacle && rightOfObstacle<= rightSide && top<= topOfObstacle && bottomOfObstacle<= bottom) {
        	collision= "object inside";
        	return collision;
        }
        
        // outside collision
        if (leftOfObstacle<= leftSide && rightSide<= rightOfObstacle && topOfObstacle<= top && bottom<= bottomOfObstacle) {
        	collision= "object outside";
        	return collision;
        }
        
        
        
        // side of collision
        if ((collidingTopLeft || collidingTopRight) && bottomOfObstacle< top+4*gamePanel.scale){
            collision=  "on top";
            return  collision;
        }
        else if ((collidingBottomLeft || collidingBottomRight) && bottom-4*gamePanel.scale< topOfObstacle){
            collision=  "on bottom";
            return  collision;
        }
        else if (collidingTopLeft || collidingBottomLeft){
            collision= "on the left";
            return  collision;
        }
        else if (collidingTopRight || collidingBottomRight){
            collision= "on the right";
            return  collision;
        }
        
        return  collision;
    }
/**
 * called by entity's method plCollisionManager, checks collision and the side of it, which returns, it implements also a sliding feature (between walls) to ease player movement
 * @param box (collider box to check on other than the one calling the method)
 */
    public String checkPlCollision(Collider box) {	// collision checking for the player, it is separated because the player has different needs of response for fluidity
        
        leftSide= x;
        rightSide= x+width;
        top= y;
        bottom= y+height;
    	
        int leftOfObstacle= box.x;
        int rightOfObstacle= box.x+box.width;
        int topOfObstacle= box.y;
        int bottomOfObstacle= box.y+box.height;

        boolean collidingBottomRight= false;
        boolean collidingTopRight= false;
        boolean collidingBottomLeft= false;
        boolean collidingTopLeft= false;
        
        String collision= null;
        
    	// corner of collision
        if (leftOfObstacle< rightSide && rightSide< rightOfObstacle){
            if (topOfObstacle< top && top< bottomOfObstacle){
                collidingTopRight= true;
            }
            else if (topOfObstacle< bottom && bottom< bottomOfObstacle){
                collidingBottomRight= true;
            }
        }
        if (leftOfObstacle< leftSide && leftSide< rightOfObstacle){
        	if (topOfObstacle< top && top< bottomOfObstacle){
                collidingTopLeft= true;
            }
            else if (topOfObstacle< bottom && bottom< bottomOfObstacle){
                collidingBottomLeft= true;
            }
        }
        
        if (leftSide< leftOfObstacle && rightOfObstacle< rightSide) {
        	//	collision inside the side of playerBox from over or under
        	
        	if (topOfObstacle< top && top< bottomOfObstacle){
                return "on bottom";
            }
            else if (topOfObstacle< bottom && bottom< bottomOfObstacle){
                return "on top";
            }
        }
        
    	if (keys.up || keys.down) {
    		// side of collision
    		if ((collidingTopLeft || collidingBottomLeft) && rightOfObstacle< leftSide+ gamePanel.biggerTile/ 2){
    			collision= "on the left";
    		}
    		else if ((collidingTopRight || collidingBottomRight) && rightSide- gamePanel.biggerTile/ 2< leftOfObstacle){
    			collision= "on the right";
    		}
    		else if (collidingTopLeft || collidingTopRight){
                collision=  "on top";
            }
            else if (collidingBottomLeft || collidingBottomRight){
                collision=  "on bottom";
            }
    	}
    	else if (keys.left || keys.right) {
    		// side of collision
            if ((collidingTopLeft || collidingTopRight) && bottomOfObstacle< top+ gamePanel.biggerTile/ 2){
                collision=  "on top";
            }
            else if ((collidingBottomLeft || collidingBottomRight) && bottom- gamePanel.biggerTile/ 2< topOfObstacle){
                collision=  "on bottom";
            }
            else if (collidingTopLeft || collidingBottomLeft){
                collision= "on the left";
            }
            else if (collidingTopRight || collidingBottomRight){
                collision= "on the right";
            }
    	}
    	else {
    		//		if the player isn't moving then it doesn't need to be checked in a different way than the enemies
    		collision= checkEnCollision(box);
    	}
    	
    	return collision;
    }
/**
 * called by entity's method surroundingsManager, checks if there are other colliders around the one calling it and returns the side in which are found
 * @param box (collider box to check on other than the one calling the method)
 */
    public String checkSurroundings(Collider box) {
    	
        leftSide= x;
        rightSide= x+width;
        top= y;
        bottom= y+height;
        
    	int leftOfObstacle= box.x;
        int rightOfObstacle= box.x+box.width;
        int topOfObstacle= box.y;
        int bottomOfObstacle= box.y+box.height;
        
        String near= "";
        
    	if (topOfObstacle< bottom-(height/2) && bottom-(height/2)< bottomOfObstacle) {
    		if (leftOfObstacle< rightSide+(width/2) && rightSide+(width/2)< rightOfObstacle) {    			
    			near= "on the right";
    		}
    		else if (leftOfObstacle< leftSide-(width/2) && leftSide-(width/2)< rightOfObstacle) {
    			near= "on the left";
    		}		
    	}
    	else if (leftOfObstacle< leftSide+(width/2) && leftSide+(width/2)< rightOfObstacle) {
    		if (topOfObstacle< top-(height/2) && top-(height/2)< bottomOfObstacle) {
    			near= "on top";
    		}
    		else if (topOfObstacle< bottom+(height/2) && bottom+(height/2)< bottomOfObstacle) {
    			near= "under";
    		}
    	}
        
        return near;
    }
/**
 * used to update collider position for static object when activated (bomb for example)
 */
    public void moveCollision(int newX, int newY){
    	x= newX;
    	y= newY;
    }
/**
 * used to set collider position and size
 */
    public void setValues(int x, int y, int width, int height) {
    	this.x= x;
    	this.y= y;
    	this.width= width;
    	this.height= height;
    }
/**
 * used to change the type of the collider (for example between bomb and exploded bomb)
 */
    public void setType(String newType){
    	this.type= newType;
    }
}
