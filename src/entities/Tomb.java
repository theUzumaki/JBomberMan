package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * created by the enemies when dying, updates the score and starts the animation of death
 */
public class Tomb extends Entity{
	/**
	 * sets the num of frames to change sprite
	 */
	int intervalLimiter= 2;
	int imgWidth;
	/**
	 * the score to print on screen
	 */
	int score;
	boolean scoreShowing;
	
	BufferedImage[] sprites1;
	BufferedImage[] sprites2;

/**
 * inherits the superconstructor, loads the sprites and sets the variables based on the kind of tomb (puropen or senshiyan)
 */
	public Tomb(GamePanel gamePanel, HUD hud, String type, int level, int x, int y, int width, int height) {
		super(gamePanel, hud);
		this.level= level;
		kind= "tomb";
		dfX= x;
		dfY= y;
		dfWidth= width;
		dfHeight= height;
		
		if (type== "puropen") {
			score= 100;
			source= "/Puropen/explosion";
			imgWidth= 2*gamePanel.biggerTile;
		}
		else if (type== "senshiyan") {
			score= 1600;
			source= "/Senshiyan/explosion";
			imgWidth= 3*gamePanel.biggerTile;
		}
		
		imgLoader();
	}
/**
 * called by gamePanel's method update, manages the sprites sequence and the time of life of the entity
 */
	public void update() {
		deathTimer++;
		
		// timing of animation
		imgSequencer();
		
		if (!scoreShowing) {
			img= sprites1[imgSelector];
		}
		else {
			img= sprites2[imgSelector];
		}
		
		if (deathTimer== 200) {
			active= false;
		}
	}
/**
 * called by the gamePanel's method paintComponent, draws the image selected
 */
	public void draw(Graphics brush) {
		brush.drawImage(img,dfX,dfY,null);
	}
/**
 * called by update, manages the sequence of sprites
 */
	private void imgSequencer() {
		if (imgInterval> intervalLimiter) {
			if (imgSelector== 8) {
				imgSelector= 0;
				imgInterval= 4;
				scoreShowing= true;
			}
			else if(scoreShowing && imgSelector== 1) {
				imgSelector= 0;
				dfY-= 2*gamePanel.scale;
			}
			else {				
				imgSelector++;
			}
			imgInterval= 0;
			
			if (scoreShowing) {
				
			}
		}
		else {
			imgInterval++;
		}
	}
/**
 * called by the constructor, loads all the sprites and puts them in specified arrays
 */
	private void imgLoader() {
		try {
			sprites1= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 1.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 2.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 3.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 4.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 5.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 6.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 7.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 8.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/explosion 9.png")),
			};
			imgResizer(sprites1, dfWidth, dfHeight);
			
			sprites2= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream(source+"/score 1.png")),
					ImageIO.read(getClass().getResourceAsStream(source+"/score 2.png"))
			};
			imgResizer(sprites2, imgWidth, gamePanel.biggerTile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}