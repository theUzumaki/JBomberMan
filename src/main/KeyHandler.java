package main;

import java.awt.event.KeyEvent;

import java.awt.event.KeyListener;

/**
 * manages the keyboard input
 */
public class KeyHandler implements KeyListener{
	public boolean up,down,right,left,select,bomb;

	@Override
	public void keyTyped(KeyEvent e) { 
	}

/**
 * registers the key pressed
 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code= e.getKeyCode();

		if (code==KeyEvent.VK_W) {
			up= true;
		}
		else if (code==KeyEvent.VK_A) {
			left= true;
		}
		else if (code==KeyEvent.VK_S) {
			down= true;
		}
		else if (code==KeyEvent.VK_D) {
			right= true;
		}
		else if (code==KeyEvent.VK_ENTER) {
			select= true;
		}
		else if (code==KeyEvent.VK_SPACE) {
			bomb= true;
		}
	}

/**
 * registers the key released
 */
	@Override
	public void keyReleased(KeyEvent e) {
		int code= e.getKeyCode();
		
		if (code==KeyEvent.VK_W) {
			up= false;
		}
		else if (code==KeyEvent.VK_A) {
			left= false;
		}
		else if (code==KeyEvent.VK_S) {
			down= false;
		}
		else if (code==KeyEvent.VK_D) {
			right= false;
		}
		else if (code==KeyEvent.VK_ENTER) {
			select= false;
		}
		else if (code==KeyEvent.VK_SPACE) {
			bomb= false;
		}
	}

}
