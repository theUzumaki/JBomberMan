package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entities.Collider;
import entities.Entity;
import entities.Player;
import entities.PowerUp;
import entities.Puropen;
import entities.Senshiyan;
import entities.SoftWall;
import entities.HUD;
import backgrounds.GameOver;
import backgrounds.Password;
import backgrounds.Stage;
import backgrounds.Title;



/**
 * core of the program, manages the speed of frame update, the level switching, the level creation and the sounds
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable {
/**
 * variable used for debug timeOfDrawingCronometer
 */
	long maxTime, averageTime, cumulativeTime, intervalCumulativeTime;
/**
 * variable used for debug timeOfDrawingCronometer
 */
	int timeCounts, intervalTimeCounts;
/**
 * the variable on which is based the level selection
 */
	public int level= 0;
	
/**
 * variable used to set the screen size
 */
	final public int tile= 8, scale= 3, biggerTile= tile*2*scale;
	
/**
 * variable used to set the screen size
 */
	public final int columns= 32, rows= 28, xScreen= scale*tile*columns, yScreen= scale*tile*rows, yStage= scale*tile*(rows-24);

/**
 * default variable of player stat
 */
	public int plSpeed= 2*scale/3;
/**
 * default variable of player stat
 */
	final public int plXSize= 16*scale, plYSize= 20*scale;
	
/**
 * frame per second, sets the amount of updates done in one second
 */
	final int FPS= 60;
	 
	// object creation:
/**
 * used to call the run method 
 */
	Thread gameThread; // once created this, it calls automatically the run method implemented by runnable
/**
 * used to manage the keyboard input, will be passed to all the objects which needs input
 */
	KeyHandler keys= new KeyHandler();
/**
 * used to manage the start and stop of music and sound effect
 */
	Sound sound= new Sound();
	
	//		-lists
/**
 * holds the colliders for the selected level
 */
	public ArrayList<Collider> colliders= new ArrayList<>();
/**
 * holds the colliders for the selected level
 */
	public ArrayList<Collider> colliders1= new ArrayList<>();
/**
 * holds the colliders for the selected level
 */
	public ArrayList<Collider> colliders2= new ArrayList<>();
/**
 * holds the entities for the selected level
 */
	public ArrayList<Entity> entities= new ArrayList<>();
/**
 * holds the entities for the selected level
 */
	public ArrayList<Entity> entities1= new ArrayList<>();
/**
 * holds the entities for the selected level
 */
	public ArrayList<Entity> entities2= new ArrayList<>();
	
	
	//		-utils
/**
 * used to build random functions
 */
	Random rndm= new Random();
	
	// 		-stages
	HUD hud;
	Stage stage;
	Title title= new Title(this);
	public GameOver gameOver= new GameOver(this);
	public Password psswrd= new Password(this, gameOver);
	public Stage stage1= new Stage(this, 1);
	public Stage stage2= new Stage(this, 2);
	
	//		-bomberman
	public Player bomber= 	new Player(this, stage1, keys, hud, 0, 0);
	
	// methods
/**
 * sets screensize and adds "keys", the KeyHandler object
 */
	public GamePanel() {
		this.setPreferredSize(new Dimension(xScreen,yScreen));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keys);
		this.setFocusable(true); // now it can focus on keys inputs
	}
/**
 * called by the main, instantiate the thread
 */
	public void start() {
		gameThread= new Thread(this);
		playMusic(0);
		gameThread.start (); // this calls the run method and really starts the thread
	}
/**
 * called by start, it's the real start of the thread and manages how much times per second the update and repaint method are called (the FPS)
 */
	@Override
	public void run() {
		double drawInterval= 1000000000/FPS;
		double delta= 0;
		long lastTime= System.nanoTime();
		long currentTime;
		while (gameThread!= null) {
			currentTime= System.nanoTime();
			
			delta+= (currentTime-lastTime)/drawInterval;
			lastTime= currentTime;
			
			if (delta>=1) {
				update();
				repaint(); // calls paint component 
				delta--;
			}
		}
	}
/**
 * called by the method run, based on the value of the var level calls the update of title, psswrd, gameOver or the entities linked to that level (so from 1 onwards)
 */
	public void update() {
		
		if (level> 0) {
			for (int i= 0; i< entities.size(); i++) {
				Entity entity= entities.get(i);
				if (entity.active) {

					entity.update();
				}
			}
			
		}
		else if (level== -1) {
			gameOver.update(keys, hud);
		}
		else if (level== -2) {
			psswrd.update(keys, hud);
		}
		else {
			title.update(keys);
		}
	}
/**
 * called by the method run, based on the value of the var level calls the draw of title, psswrd, gameOver or the entities linked to that level (so from 1 onwards)
 */
	public void paintComponent(Graphics brush) { // built-in method of JPanel to draw
		super.paintComponent(brush);
		
		if (level> 0) {
			long drawStart= System.nanoTime();
			
			// background
			stage.draw(brush);
			
			entities.sort(new DepthComparator());
			
			// entities
			for (int i= 0; i< entities.size(); i++) {
				Entity entity= entities.get(i);
				if (entity.active) {
					
					entity.draw(brush);
				}
			}
			
			if (bomber.exit) {
				BufferedImage img2 = null;
				
				try {
					img2= ImageIO.read(getClass().getResourceAsStream("/WhiteBomber/winning/victory.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				brush.drawImage(img2,xScreen/4,yScreen/4,xScreen/2,yScreen/2,null);
			}
			
			timeOfDrawingCronometer(drawStart, false);
			
		}
		else if (level== -1) {
			gameOver.draw(brush);
		}
		else if (level== -2) {
			psswrd.draw(brush);
		}
		else {
			title.draw(brush); 
		}

		drawCollision(brush, false);
		
		brush.dispose();
	}
/**
 * called by setLevel, calls the methods setMusicFile and play of the object "sound" in order to start music
 */
	public void playMusic(int i) {
		
		sound.setMusicFile(i);
		sound.play();
	}
/**
 * called by setLevel, calls the method stop of the object "sound", in order to stop music
 */
	public void stopMusic() {
		
		sound.stop();
	}
/**
 * called by entities, calls the method setFile and playSE of the object "sound", in order to play a sound effect
 */
	public void playSE(int i) {
		
		sound.setFile(i);
		sound.playSE(i);
	}
/**
 * called by setLevel, it contains all the info about number and kind of enemies for each level and it passes this info to another method which generates the entities
 */
	private void levelsCreator() {
		
		if (level== 1) {
			// HUD
			hud= 		new HUD(this, 1);
			
			// soft wall on exit
			new SoftWall (level,	9*biggerTile,	6*biggerTile,	true, false, true, "short", this, null);
			
			// power ups and covering soft walls
			entitiesGenerator(1, 2, 2, "power up- bomb up");
			entitiesGenerator(1, 2, 2, "power up- skate");
			entitiesGenerator(1, 2, 2, "power up- armor");
			
			// soft walls
			entitiesGenerator(17, 0, 0, "soft wall");
			
			// puropen
			entitiesGenerator(3, 3, 3, "puropen");
			
			// senshiyan
			entitiesGenerator(1, 3, 3, "senshiyan");
		}
	
		else if (level== 2) {
			// HUD 
			hud=		new HUD(this, 2);
			
			// soft wall on exit
			new SoftWall (level,	7*biggerTile,	0*biggerTile,	true, false, true, "short", this, null);
			
			// power ups and covering soft walls
			entitiesGenerator(1, 2, 2, "power up- bomb up");
			entitiesGenerator(1, 2, 2, "power up- skate");
			entitiesGenerator(1, 2, 2, "power up- armor");
			
			// soft walls
			entitiesGenerator(16, 0, 0, "soft wall");
			
			// puropen
			entitiesGenerator(3, 3, 3, "puropen");
			
			// senshiyan
			entitiesGenerator(1, 3, 3, "senshiyan");
		}
		
		if (level> 0) {			
			tileMapPrinter(true);
		}
	}
/**
 * called by levelsCreator, instantiates the entities requested giving them a random position
 * @param numberOfEntities (how many entities to generate)
 * @param columnOffset (used to set the random spawn after a specified column)
 * @param rowOffset (used to set the random spawn after a specified row)
 * @param entityKind (which entities is being generated)
 */
	private void entitiesGenerator(int numberOfEntities, int columnOffset, int rowOffset, String entityKind) {
		String lenght;
		
		for (int i= 0; i!= numberOfEntities; i++) {
			int randomColumn= columnOffset+rndm.nextInt(13-columnOffset);
			int randomRow= rowOffset+rndm.nextInt(11-rowOffset);
			
			if (stage.tileMap[randomRow][randomColumn]== null) {
				
				if (entityKind== "soft wall") {
					if (randomRow> 1 || randomColumn> 1) {
						
						if (randomRow+1== 11 || stage.tileMap[randomRow+1][randomColumn]== "wall") {
							lenght= "short";
						}
						else {
							lenght= "long";
						}
						
						new SoftWall (level,	randomColumn*biggerTile,	randomRow*biggerTile,	false,	false, false, lenght,	this, null);
						stage.tileMap[randomRow][randomColumn]= "soft";
					}
				}
				
				else if (entityKind== "puropen") {
					if (randomRow<10 && (stage.tileMap[randomRow+1][randomColumn]!= "puro" && stage.tileMap[randomRow+1][randomColumn]!= "sens")) {
						if (randomRow>0 && (stage.tileMap[randomRow-1][randomColumn]!= "puro" && stage.tileMap[randomRow-1][randomColumn]!= "sens")) {
							if (randomColumn<12 && (stage.tileMap[randomRow][randomColumn+1]!= "puro" && stage.tileMap[randomRow][randomColumn+1]!= "sens")) {
								if (randomColumn>0 && (stage.tileMap[randomRow][randomColumn-1]!= "puro" && stage.tileMap[randomRow][randomColumn-1]!= "sens")) {									
									new Puropen (this,	hud,	level,	randomColumn*biggerTile,	randomRow*biggerTile);
									stage.tileMap[randomRow][randomColumn]= "puro";
								}
							}
						}
					}
					if (stage.tileMap[randomRow][randomColumn]== null) {
						i--;
					}
				}
				
				else if (entityKind== "senshiyan") {
					if (randomRow<10 && (stage.tileMap[randomRow+1][randomColumn]!= "puro" && stage.tileMap[randomRow+1][randomColumn]!= "sens")) {
						if (randomRow>0 && (stage.tileMap[randomRow-1][randomColumn]!= "puro" && stage.tileMap[randomRow-1][randomColumn]!= "sens")) {
							if (randomColumn<12 && (stage.tileMap[randomRow][randomColumn+1]!= "puro" && stage.tileMap[randomRow][randomColumn+1]!= "sens")) {
								if (randomColumn>0 && (stage.tileMap[randomRow][randomColumn-1]!= "puro" && stage.tileMap[randomRow][randomColumn-1]!= "sens")) {									
									new Senshiyan (this,	hud,	level,	randomColumn*biggerTile,	randomRow*biggerTile);
									stage.tileMap[randomRow][randomColumn]= "sens";
								}
							}
						}
					}
					if (stage.tileMap[randomRow][randomColumn]== null) {
						i--;
					}
				}
				
				else if (entityKind== "power up- bomb up") {
					boolean shadow= false;
					
					if (stage.tileMap[randomRow-1][randomColumn]== "wall") {
						shadow= true;
					}
					
					if (randomRow+1== 11 || stage.tileMap[randomRow+1][randomColumn]== "wall") {
						lenght= "short";
					}
					else {
						lenght= "long";
					}
					
					PowerUp pwr= new PowerUp (this,		hud,	"bomb up",	level,	randomColumn*biggerTile,	randomRow*biggerTile);
					new SoftWall (level,	randomColumn*biggerTile,	randomRow*biggerTile,	true,	true,	shadow,	lenght,	this, pwr);
					stage.tileMap[randomRow][randomColumn]= "powr";
				}
				else if (entityKind== "power up- skate") {
					boolean shadow= false;
					
					if (stage.tileMap[randomRow-1][randomColumn]== "wall") {
						shadow= true;
					}
					
					if (randomRow+1== 11 || stage.tileMap[randomRow+1][randomColumn]== "wall") {
						lenght= "short";
					}
					else {
						lenght= "long";
					}
					
					PowerUp pwr= new PowerUp (this,		hud,	"skate",	level,	randomColumn*biggerTile,	randomRow*biggerTile);
					new SoftWall (level,	randomColumn*biggerTile,	randomRow*biggerTile,	true,	true,	shadow,	lenght,	this, pwr);
					stage.tileMap[randomRow][randomColumn]= "powr";
				}
				else if (entityKind== "power up- armor") {
					boolean shadow= false;
					System.out.println("INSIDE");
					
					if (stage.tileMap[randomRow-1][randomColumn]== "wall") {
						shadow= true;
					}
					
					if (randomRow+1== 11 || stage.tileMap[randomRow+1][randomColumn]== "wall") {
						lenght= "short";
					}
					else {
						lenght= "long";
					}
					
					PowerUp pwr= new PowerUp (this,		hud,	"armor",	level,	randomColumn*biggerTile,	randomRow*biggerTile);
					new SoftWall (level,	randomColumn*biggerTile,	randomRow*biggerTile,	true,	true,	shadow,	lenght,	this, pwr);
					stage.tileMap[randomRow][randomColumn]= "powr";
				}
				
			}
			else {
				i--;
			}
		}
	}
/**
 * debug function called by the paintComponent, draws all the collider box in different colors based on the object to which they are associated
 * @param active (a boolean which toggles on or off the method)
 */
	private void drawCollision(Graphics brush, boolean active) {
		// DEBUG FUNCTION
		
		if (active) {			
			for (Collider collider: colliders) {
				if (collider.active) {
					if (collider.type== "exploded bomb") {						
						brush.setColor(new Color(255, 0, 0, 100));
					}
					else if (collider.type== "senshiyan") {
						brush.setColor(new Color(0, 255, 0, 200));
					}
					else if (collider.type== "puropen") {
						brush.setColor(new Color(255, 45, 45, 200));
					}
					else if (collider.type== "player") {
						brush.setColor(new Color(220, 220, 220, 200));
					}
					else if (collider.type== "exit") {
						brush.setColor(new Color(45, 45, 255, 200));
					}
					else {
						brush.setColor(new Color(0, 0, 0, 200));
					}
					brush.fillRect(collider.x, collider.y, collider.width, collider.height);
				}
			}
		}
	}
/**
 * debug function called by the paintComponent, it measures how much time is needed to draw all the object and displays in the terminal an average on the total time, an average on an interval and the time for that frame
 * @param drawStart (time in which the drawings started)
 * @param active (a boolean which toggles on or off the method)
 */
	private void timeOfDrawingCronometer(long drawStart, boolean active) {
		if (active) {			
			long drawEnd= System.nanoTime();
			timeCounts++;
			intervalTimeCounts++;
			cumulativeTime+= drawEnd-drawStart;
			intervalCumulativeTime+= drawEnd-drawStart;
			System.out.print(intervalTimeCounts+"\tinstant: "+((drawEnd-drawStart)/1000)+"\tintervalAvg: "+
								(intervalCumulativeTime/intervalTimeCounts/1000)+"\t"+"\ttotalAvg: "+(cumulativeTime/timeCounts/1000)+"\t");
			if (intervalTimeCounts> 99) {
				intervalTimeCounts= 0;
				intervalCumulativeTime= 0;
			}
			if ((drawEnd-drawStart)>maxTime) {
				maxTime= drawEnd-drawStart;
				System.out.println("NEW MAX: "+maxTime);
			}
			else {
				System.out.println();
			}
		}
	}
/**
 * debug function called by setLevel, prints a tile map of where the entities have being spawned
 * @param active (a boolean which toggles on or off the method)
 */
	private void tileMapPrinter(boolean active) {
		if (active) {
			System.out.println();
			for (String[] i: stage1.tileMap) {
				for (String j: i) {
					if (j!= null) {						
						j= j.toUpperCase();
					}
					else {
						j= "NULL";
					}
					System.out.print("\t"+j);
				}
				System.out.println();
			}
			System.out.println();
		}
		
	}
/**
 * called by the title update, the gameOver update, the psswrd update and the player update, initialize the arrays that contains the entities and their colliders, instantiate the object stage and calls the level creator
 */
	public void setLevel(int level) {
		
		if (level>= 0) {				
			stopMusic();
			playMusic(level);
		}
		
		this.level= level;
		
		switch(this.level) {
		case 1:
			System.out.println("\n\t\t\t\t\tLEVEL-1\n");
			
			entities1.clear();
			colliders1.clear();
			stage1= new Stage(this, 1);
			stage= stage1;
			entities= entities1;
			colliders= colliders1;
			gameOver.setLevel(level);
			bomber.setLevel(level);
			break;
		case 2:
			System.out.println("\n\t\t\t\t\tLEVEL-2\n");
			
			entities2.clear();
			colliders2.clear();
			stage2= new Stage(this, 2);
			stage= stage2;
			entities= entities2;
			colliders= colliders2;
			gameOver.setLevel(level);
			bomber.setLevel(level);
			break;
		}

		levelsCreator();
	}
}
