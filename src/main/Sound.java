package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * contains the sound library and the methods to play and stop both SE and music
 */
public class Sound {
	
	/**
	 * holds the current music file
	 */
	Clip clip;
	/**
	 * holds the sound's files
	 */
	URL soundURL[]= new URL[14];
	/**
	 * holds the sound's file ready to play
	 */
	Clip[] clipList= new Clip[14];
	
/**
 * loads all the sound files into an array
 */
	public Sound() {
		soundURL[0]= getClass().getResource("/01. Explosive Beginnings (Main Title).wav");
		soundURL[1]= getClass().getResource("/04. World 1 - Peace Town, Green Village, Black Bomberman's Castle.wav");
		soundURL[2]= getClass().getResource("/05. World 2 - Robot Amusement Park (online-audio-converter.com).wav");
		soundURL[3]= getClass().getResource("/Bomb Explodes.wav");
		soundURL[4]= getClass().getResource("/Bomberman Dies.wav");
		soundURL[5]= getClass().getResource("/Enemy Dies.wav");
		soundURL[6]= getClass().getResource("/Item Get.wav");
		soundURL[7]= getClass().getResource("/Kick - Pressure Block.wav");
		soundURL[8]= getClass().getResource("/Place Bomb.wav");
		soundURL[9]= getClass().getResource("/Stage Clear.wav");
		soundURL[10]= getClass().getResource("/Stage Intro.wav");
		soundURL[11]= getClass().getResource("/Title Screen Cursor.wav");
		soundURL[12]= getClass().getResource("/Title Screen Select.wav");
		soundURL[13]= getClass().getResource("/Walking 1-.wav");
	}

/**
 * opens the sound file in order to play it and loads it into an array "clipList" (used for sound effects)
 * @param i (index of array)
 */
	public void setFile(int i) {
		try {
			AudioInputStream ais= AudioSystem.getAudioInputStream(soundURL[i]);
			clipList[i]= AudioSystem.getClip();
			clipList[i].open(ais);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * opens the sound file in order to play it and loads into the variable "clip" (used for music)
 * @param i (index of array)
 */
	public void setMusicFile(int i) {
		try {
			AudioInputStream ais= AudioSystem.getAudioInputStream(soundURL[i]);
			clip= AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * starts and loops the sound loaded in "clip"
 */
	public void play() {
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

/**
 * starts the specified sound loaded in "clipList"
 * @param i (index of array)
 */
	public void playSE(int i) {
		clipList[i].start();
	}
/**
 * stops the sound loaded in clip
 */
	public void stop() {
		clip.stop();
	}
}
