package dEMDRC;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DisplayArray {
	//our shite
	private int direction = 1;
	private int location = 1;	//1 - (determineEyesInArray(xSize) - 1)
	
	//getters/setters (retentive for this, to be sure)
	public int getDirection() {
		return this.direction;
	}
	
	public int toggleDirection() {
		this.direction *= -1;
		return this.direction;
	}
	
	public int getLocation() {
		return this.location;
	}
	
	/**
	 * Method will increment the location of the 'leading' eye pixel properly, reversing
	 * direction if necessary at either of the far bounds
	 */
	public void incLocation() {
		if (((location == 1) && (direction == -1)) ||
			((location == (determineEyesInArray(Options.MaxX) - 1)) && (direction == 1))) {
			location += toggleDirection();
		} else {
			location += direction;
		}
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
