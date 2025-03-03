package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * the flames fired by the senshiyan, their update, draw and animation
 */
public class Flames extends Entity{
	int colliderX, colliderY;
	BufferedImage imgFire1, imgFire2, imgFire3, imgFire4, imgFire5, imgFire6;
	BufferedImage[] sprites5, sprites6, sprites7, sprites8, sprites9, sprites10, sprites11, sprites12, sprites13, sprites14, sprites15, sprites16,
						sprites17, sprites18, sprites19, sprites20, sprites21, sprites22, sprites23,sprites24, sprites25, sprites26, sprites27, sprites28;
/**
 * inherits superconstructor from Entity, assigns values, loads sprites and creates collider
 */
	public Flames(GamePanel gamePanel, HUD hud, int level) {
		super(gamePanel, hud);
		this.level= level;
		kind= "flames";
		
		entityBox= new Collider(level, 0, 0, 0, 0, "flames", gamePanel);
		imgLoader();
	}
/**
 * called by the gamePanel's method update, manages the sequence of images
 */
	public void update() {
		if (imgInterval== 4) {
			if (imgSelector< 4) {
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
		
		switch (oldVerse) {
		case 1:
			imgFire1= sprites23[imgSelector];
			imgFire2= sprites24[imgSelector];
			imgFire3= sprites25[imgSelector];
			imgFire4= sprites26[imgSelector];
			imgFire5= sprites27[imgSelector];
			imgFire6= sprites28[imgSelector];
			break;
		case 2:
			imgFire1= sprites17[imgSelector];
			imgFire2= sprites18[imgSelector];
			imgFire3= sprites19[imgSelector];
			imgFire4= sprites20[imgSelector];
			imgFire5= sprites21[imgSelector];
			imgFire6= sprites22[imgSelector];
			break;
		case 3:
			imgFire1= sprites5[imgSelector];
			imgFire2= sprites6[imgSelector];
			imgFire3= sprites7[imgSelector];
			imgFire4= sprites8[imgSelector];
			imgFire5= sprites9[imgSelector];
			imgFire6= sprites10[imgSelector];
			break;
		case 4:
			imgFire1= sprites11[imgSelector];
			imgFire2= sprites12[imgSelector];
			imgFire3= sprites13[imgSelector];
			imgFire4= sprites14[imgSelector];
			imgFire5= sprites15[imgSelector];
			imgFire6= sprites16[imgSelector];
			break;
		}
	}
/**
 * called by the gamePanel's method paintComponent, draws the images
 */
	public void draw(Graphics brush) {
		brush.drawImage(imgFire1,dfX,dfY,null);
		brush.drawImage(imgFire2,dfX,dfY,null);
		brush.drawImage(imgFire3,dfX,dfY,null);
		brush.drawImage(imgFire4,dfX,dfY,null);
		brush.drawImage(imgFire5,dfX,dfY,null);
		brush.drawImage(imgFire6,dfX,dfY,null);
	}
/**
 * called by the constructor, loads the sprites
 */
	private void imgLoader() {
		try {
			sprites5= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big1/big 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big1/big 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big1/big 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big1/big 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big1/big 1-5.png"))
			};
			imgResizer(sprites5, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites6= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big2/big 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big2/big 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big2/big 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big2/big 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/big2/big 2-5.png"))
			};
			imgResizer(sprites6, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites7= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid1/mid1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid1/mid1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid1/mid1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid1/mid1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid1/mid1-5.png"))
			};
			imgResizer(sprites7, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites8= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid2/mid 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid2/mid 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid2/mid 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid2/mid 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/mid2/mid 2-5.png"))
			};
			imgResizer(sprites8, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites9= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small1/small 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small1/small 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small1/small 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small1/small 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small1/small 1-5.png"))
			};
			imgResizer(sprites9, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites10= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small2/small 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small2/small 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small2/small 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small2/small 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal1/small2/small 2-5.png"))
			};
			imgResizer(sprites10, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites11= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big1/big 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big1/big 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big1/big 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big1/big 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big1/big 1-5.png"))
			};
			imgResizer(sprites11, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites12= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big2/big 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big2/big 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big2/big 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big2/big 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/big2/big 2-5.png"))
			};
			imgResizer(sprites12, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites13= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid1/mid1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid1/mid1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid1/mid1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid1/mid1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid1/mid1-5.png"))
			};
			imgResizer(sprites13, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites14= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid2/mid 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid2/mid 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid2/mid 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid2/mid 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/mid2/mid 2-5.png"))
			};
			imgResizer(sprites14, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites15= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small1/small 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small1/small 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small1/small 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small1/small 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small1/small 1-5.png"))
			};
			imgResizer(sprites15, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites16= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small2/small 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small2/small 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small2/small 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small2/small 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/orizontal2/small2/small 2-5.png"))
			};
			imgResizer(sprites16, 4*gamePanel.biggerTile, 3*gamePanel.biggerTile/2);
			
			sprites17= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big1/big 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big1/big 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big1/big 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big1/big 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big1/big 1-5.png"))
			};
			imgResizer(sprites17, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites18= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big2/big 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big2/big 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big2/big 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big2/big 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/big2/big 2-5.png"))
			};
			imgResizer(sprites18, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites19= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid1/mid 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid1/mid 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid1/mid 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid1/mid 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid1/mid 1-5.png"))
			};
			imgResizer(sprites19, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites20= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid2/mid 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid2/mid 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid2/mid 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid2/mid 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/mid2/mid 1-5.png"))
			};
			imgResizer(sprites20, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites21= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small1/small 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small1/small 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small1/small 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small1/small 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small1/small 1-5.png"))
			};
			imgResizer(sprites21, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites22= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small2/small 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small2/small 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small2/small 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small2/small 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical1/small2/small 2-5.png"))
			};
			imgResizer(sprites22, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites23= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big1/big 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big1/big 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big1/big 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big1/big 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big1/big 1-5.png"))
			};
			imgResizer(sprites23, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites24= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big2/big 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big2/big 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big2/big 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big2/big 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/big2/big 2-5.png"))
			};
			imgResizer(sprites24, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites25= new BufferedImage[]{
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid1/mid 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid1/mid 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid1/mid 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid1/mid 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid1/mid 1-5.png"))
			};
			imgResizer(sprites25, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites26= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid2/mid 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid2/mid 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid2/mid 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid2/mid 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/mid2/mid 1-5.png"))
			};
			imgResizer(sprites26, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites27= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small1/small 1-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small1/small 1-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small1/small 1-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small1/small 1-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small1/small 1-5.png"))
			};
			imgResizer(sprites27, gamePanel.biggerTile, 4*gamePanel.biggerTile);
			
			sprites28= new BufferedImage[] {
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small2/small 2-1.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small2/small 2-2.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small2/small 2-3.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small2/small 2-4.png")),
					ImageIO.read(getClass().getResourceAsStream("/Senshiyan/fires/vertical2/small2/small 2-5.png"))
			};
			imgResizer(sprites28, gamePanel.biggerTile, 4*gamePanel.biggerTile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
