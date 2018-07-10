package dEMDRC;

public class DisplayArray {
	//our shite
	private int direction = 1;
	
	//getters/setters (retentive for this, to be sure)
	public int getDirection() {
		return this.direction;
	}
	
	public int toggleDirection(int dir) {
		this.direction *= -1;
		return this.direction;
	}
	
	//general methods
	public void initDisplay() {
		for (int ouahX = 25; ouahX < determineEyesInArray(Options.MaxX - 25); ouahX += 50) {
			//draw our 25x25 block and skip to m' lou _HAIGH_
			
		}
	}
	
	private int determineEyesInArray(int xSize) {
		int curX = 25;	//start with a padding of 25
		int eyes;
		
		for (eyes = 0; curX < (xSize - 25); curX += 25) {
			eyes++;
		}
		
		return eyes;
	}
}
