package dEMDRC;

import javax.sound.sampled.LineUnavailableException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DisplayArray {
	//our shite
	private static int direction = 1;
	private static int location = 1;	//1 - (determineEyesInArray(xSize) - 1)
	private static int remaining = HeadsUp.opts.TotalIterations;
	
	//getters/setters (retentive for this, to be sure)
	public int getDirection() {
		return direction;
	}
	
	public int getLocation() {
		return location;
	}
	
	/**
	 * Method toggles the direction of kitt's eye motion
	 * 
	 * @return int signifying the new direction of motion
	 */
	public static int toggleDirection() {
		direction *= -1;
		remaining--;
		
		return direction;
	}
	
	/**
	 * More bouncing in store?
	 * 
	 * @return boolean
	 */
	public static boolean moreRemaining() {
		if (remaining < 1) {
			remaining = HeadsUp.opts.TotalIterations;
			return false;
		} else {
			//remaining--;
			return true;
		}
	}
	
	/**
	 * Method will increment the location of the 'leading' eye pixel properly, reversing
	 * direction if necessary at either of the far bounds
	 */
	private static void incLocation() {
		if (((location == 1) && (direction == -1)) ||
			((location == (determineEyesInArray(Options.MaxX) - 1)) && (direction == 1))) {
			location += toggleDirection();
			
			/*
			 * other stim triggers go here, until we've got better structure in the OO
			 */
			try {
				if (!Options.StereoAudio) { 
					HeadsUp.blonk.playTone(null);
				} else if (direction == 1) {	//going right (play left)
					HeadsUp.blonk.playTone(Options.StereoSide.LEFT);
				} else {
					HeadsUp.blonk.playTone(Options.StereoSide.RIGHT);
				}
			} catch (LineUnavailableException ex) {
				System.err.println("Audio fucked up: " + ex.getMessage());
			}
			
		} else {
			location += direction;
		}
		
		//delay properly heah
		try {
			Thread.sleep((long)HeadsUp.getSliderSpeed());
		} catch (Exception ex) {
			System.err.println("Thread sleep(); issue: " + ex.getMessage());
		}
	}
	
	/**
	 * Method will send the eye to the right, bounce to the left, etc, with the appropriate
	 * number of trailing eyes remaining lit after start
	 * 
	 * @param gc GraphicsContext so that we've got the right object to work with for display
	 * @return GraphicsContext to be tossed on screen
	 */
	public static GraphicsContext swoosh(GraphicsContext gc) {
		gc = wipeOldAway(gc);
		
		incLocation();
		
		gc.setFill(Color.RED /*should be Options.fgColor*/);
		gc.fillRect((location * 50), 50, Options.BoxMaxX, Options.BoxMaxY);
		
		return gc;
	}
	
	/**
	 * Method will wipe the trailing eye for fade
	 * 
	 * @param gc
	 * @return
	 */
	private static GraphicsContext wipeOldAway(GraphicsContext gc) {
		int curLoc;
		
		if ((direction == 1) && (location >= Options.LitEyes)) {	//going right, start wiping
			curLoc = location - (Options.LitEyes - 1);
		}  else if ((direction == -1) && (location <= (determineEyesInArray(Options.MaxX) - (Options.LitEyes - 1)))) {
			curLoc = location + (Options.LitEyes - 1);
		} else {
			return gc;
		}
		
		//aaand I want to paint it blaaack...
		gc.setFill(Color.BLACK /*Options.bgColor*/);
		gc.fillRect((curLoc * 50), 50, Options.BoxMaxX, Options.BoxMaxY);
		
		return gc;
	}
	
	/**
	 * This method is probably more for testing than anything else, at this point; it simply
	 * fills in the eye pixels of all that will fit on the display bar (demo?).  This may
	 * still have a purpose, so we won't chop it just yet
	 * 
	 * @param gc GraphicsContext
	 * @return
	 */
	public static GraphicsContext initDisplay(GraphicsContext gc) {
		if (HeadsUp.opts.debuggingGen()) {
			System.out.println("DBG: determineEyesInArray(" + Options.MaxX + ") = " + 
				determineEyesInArray(Options.MaxX)); 
		}
		
		for (int ouahX = 1; (ouahX * 50) <= (Options.MaxX - 25); ouahX++) {
			//NOTE: fillRect(X (left inc), Y (down inc), width, height)
			gc.setFill(Color.RED);
			gc.fillRect((ouahX * 50), 50, Options.BoxMaxX, Options.BoxMaxY);
			
			if (HeadsUp.opts.debuggingGen()) {
				System.out.println("ouahX: " + ouahX);
			}
		}
		
		return gc;
	}
	
	/**
	 * Method returns the number of eyes that will fit in the given display bar
	 * 
	 * @param xSize maximum X dimensions of the display bar
	 * @return number of eye pixels that will fit
	 */
	public static int determineEyesInArray(int xSize) {
		return (int)(xSize / 50);
	}
}
