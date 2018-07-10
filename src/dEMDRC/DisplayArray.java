package dEMDRC;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DisplayArray {
	//our shite
	private static int direction = 1;
	private static int location = 1;	//1 - (determineEyesInArray(xSize) - 1)
	private static int remaining = Options.TotalIterations;
	
	//getters/setters (retentive for this, to be sure)
	public int getDirection() {
		return this.direction;
	}
	
	public static int toggleDirection() {
		direction *= -1;
		return direction;
	}
	
	public int getLocation() {
		return this.location;
	}
	
	public static boolean moreRemaining() {
		if (remaining < 1) {
			remaining = Options.TotalIterations;
			return false;
		} else {
			remaining--;
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
		} else {
			location += direction;
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
		
		//erase the old - first find the location to wipe
		/*if ((direction == 1) && (location < Options.LitEyes)) {	//going right, none to erase
			continue;
			//return gc;
		} else*/
		if ((direction == 1) && (location >= Options.LitEyes)) {	//going right, start wiping
			curLoc = location - (Options.LitEyes - 1);
		} /*else if ((direction == -1) && (location > (determineEyesInArray(Options.MaxX) - (Options.LitEyes - 1)))) {
			//going left, none to erase
			continue;
			//return gc;
		} */ else if ((direction == -1) && (location <= (determineEyesInArray(Options.MaxX) - (Options.LitEyes - 1)))) {
			//))){	//going left, erase this one
			curLoc = location + (Options.LitEyes - 1);
		} else {
			return gc;
		}
		
		//aaand I want to paint it blaaack...
		gc.setFill(Color.BLACK /*Options.bgColor*/);
		gc.fillRect((curLoc * 50), 50, Options.BoxMaxX, Options.BoxMaxY);
		
		return gc;
	}
	
	//general methods
	/**
	 * This method is probably more for testing than anything else, at this point; it simply
	 * fills in the eye pixels of all that will fit on the display bar (demo?).  This may
	 * still have a purpose, so we won't chop it just yet
	 * 
	 * @param gc GraphicsContext
	 * @return
	 */
	public static GraphicsContext initDisplay(GraphicsContext gc) {
		if (Options.debugging) {
			System.out.println("DBG: determineEyesInArray(" + Options.MaxX + ") = " + 
				determineEyesInArray(Options.MaxX)); 
		}
		for (int ouahX = 1; (ouahX * 50) <= (Options.MaxX - 25); ouahX++) {
			//NOTE: fillRect(X (left inc), Y (down inc), width, height)
			//draw our 25x25 block and skip to m' lou _HAIGH_
			gc.setFill(Color.RED);
			gc.fillRect((ouahX * 50), 50, Options.BoxMaxX, Options.BoxMaxY);
			
			if (Options.debugging) {
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
	private static int determineEyesInArray(int xSize) {
		return (int)(xSize / 50);
	}
}
