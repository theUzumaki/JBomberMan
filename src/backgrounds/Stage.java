package backgrounds;

import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entities.Collider;
import main.GamePanel;

/**
 * the backgrounds for each stage, their building methods and draw
 */
public class Stage{
	// images
	/**
	 * image variable
	 */
	BufferedImage backG, HUD, wall, exit;
	public GamePanel gamePanel;
	
	// matrix
	/**
	 * holds the positioning of the colliders created by the object
	 */
	public String[][] tileMap= new String[11][13]; // rows and columns
	
	// collider
	/**
	 * offset of the tile map because in the game it is not centered
	 */
	int xOffset, yOffset, level;
	Collider box;

/**
 * assigns the values, loads the sprites and creates the collider for the border of the map and the unbreakable walls on the map
 * @param gamePanel
 * @param level
 */
	public Stage(GamePanel gamePanel, int level) {
		this.gamePanel= gamePanel;
		this.level= level;
		
		xOffset= 3*gamePanel.biggerTile/2;	// the offset for the lateral border, to align on the tile map
		yOffset= 7*gamePanel.scale;			// the offset which adds (other than the hud y) the y of the little border
		
		tileMap[0][0]= "player";
		
		imgLoader();
		
		//		-borders
		// top
		box= new Collider(level, 0, gamePanel.yStage - yOffset, gamePanel.xScreen, 2*yOffset, "wall", gamePanel);
		box.active= true;
		// bottom
		box= new Collider(level, 0, gamePanel.yScreen - (gamePanel.biggerTile - yOffset), gamePanel.xScreen, gamePanel.biggerTile - yOffset, "wall", gamePanel);
		box.active= true;
		// left
		box= new Collider(level, gamePanel.biggerTile / 2, gamePanel.yStage, gamePanel.biggerTile, gamePanel.yScreen - gamePanel.yStage, "wall", gamePanel);
		box.active= true;
		// right
		box= new Collider(level, gamePanel.xScreen - 3 * gamePanel.biggerTile / 2, gamePanel.yStage, gamePanel.biggerTile, gamePanel.yScreen - gamePanel.yStage, "wall", gamePanel);
		box.active= true;
		//		-walls
		collisionFinder(wall, "wall", level, xOffset, yOffset + gamePanel.yStage, gamePanel.biggerTile, gamePanel.biggerTile);
		//		-exit
		collisionFinder(exit, "exit", level, gamePanel.biggerTile/4 + xOffset, gamePanel.biggerTile/4 + yOffset + gamePanel.yStage, gamePanel.biggerTile/2, gamePanel.biggerTile/2);
	}

/**
 * called by the gamePanel's method paintComponent, draws the image selected
 */
	public void draw(Graphics brush) {
		brush.drawImage(backG,0,gamePanel.yStage,gamePanel.xScreen,gamePanel.yScreen-gamePanel.yStage, null);
		brush.drawImage(HUD,0,0,gamePanel.xScreen,gamePanel.yStage, null);
	}
/**
 * called by constructor, loads all the sprites
 */
	private void imgLoader() {
		try {
			backG= ImageIO.read(getClass().getResourceAsStream("/Background/stage "+level+".png"));
			HUD= ImageIO.read(getClass().getResourceAsStream("/Background/HUD.png"));
			wall= ImageIO.read(getClass().getResourceAsStream("/Background/wall.png"));
			exit= ImageIO.read(getClass().getResourceAsStream("/Background/exit.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
/**
 * called by constructor, searches the sprite of the map and places collision when found the image passed 
 * @param toFind (image to find)
 * @param typeOfCollider (the name of the image it is searching for)
 * @param level (to pass onto the collider creator)
 * @param xOffset (to pass onto the collider creator)
 * @param yOffset (to pass onto the collider creator)
 * @param width (to pass onto the collider creator)
 * @param heigth (to pass onto the collider creator)
 */
	private void collisionFinder(BufferedImage toFind, String typeOfCollider, int level, int xOffset, int yOffset, int width, int heigth) {
		int[] comparingRGB= new int[256];
		int[] dfRGB= new int[256];
		for (int j= 0; j< 11; j++) {
			
			for (int k= 0; k< 14; k++) {
				
				toFind.getRGB(0,0,16,16,dfRGB,0,16);
				backG.getRGB(k*gamePanel.tile*2+24,j*gamePanel.tile*2+7,16,16,comparingRGB,0,16);
				
				int valider= 0;
				for (int pixel= 0; pixel< 256; pixel++) {
					if (dfRGB[pixel]== comparingRGB[pixel]) {
						valider++;
					}
					else {
						break;
					}
					if (valider== 255) {
						int x= k*gamePanel.biggerTile+xOffset;
						int y= j*gamePanel.biggerTile+yOffset;
						
						box= new Collider(level,x,y,width,heigth,typeOfCollider,gamePanel);
						box.active= true;
						tileMap[j][k]= typeOfCollider;
					}
				}
			}
		}
	}
}
