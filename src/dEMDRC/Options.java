package dEMDRC;

import com.sun.prism.paint.Color;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

@SuppressWarnings("restriction")
public class Options {
	//behind the scenes - _HAIGH_
	private static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	
	//display options
	public static final int MaxX = (int)primaryScreenBounds.getMaxX();
	public static final int MaxY = (int)(primaryScreenBounds.getMaxY() * 0.2);
	//I think for now we're going to try 25x25 squares for kitt's eyes
	public static final int BoxMaxX = 25;
	public static final int BoxMaxY = 25; 
	public static final int LitEyes = 4;
	//color prefs
	public static final Color bgColor = Color.BLACK;
	public static final Color fgColor = Color.RED;
	//timing and duration constants
	public static final int SessionDurationInMin = 3;
	public static final int DefaultPauseInMS = 10;
	public static final int MinimumPauseInMS = 5;
	public static final int MaximumPauseInMS = 150;
	public static final int TotalIterations = 500;	//testing purposes
	//public static final int TotalIterations = (SessionDurationInMin * 60 * (1000 / PauseInMS));	//production
	
	//audio options
	public static final boolean BeepForAudio = true;
	public static final int AStimFreq = 432;	//we'll probably want a higher chakra ;)
	public static final int AStimDurInMS = 15;
	public static final int ASampleRate = 16 * 1024;
	public static final boolean StereoAudio = true;
	
	//ouah
	public static final boolean debugging = true;
	public static final boolean testing = true;
	
	//enums
	public static enum StereoSide { LEFT, RIGHT };
}
