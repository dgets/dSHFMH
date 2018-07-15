package dEMDRC;

import java.io.File;

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
	
	//color/shade spectrum
	//public Array Rainbow = new Array();
	
	//user modifiable values
	public class UserSet {
		//display options
		//NOTE: we'll be putting window sizes in here at some point, but initially our defaults are good enough; this can
		//be saved for a beta version
		public int MyKittWidth, MyKittHeight;
		//we can work with window positions at that point, too
		public Color MyBgColor, MyFgColor;
		
		//timings
		public int MySessionDuration, MyPauseInMS, MyTotalIterations;
		
		//audio
		public boolean MyBeepForAudio, MyStereoAudio;
		public int MyAStimFreq, MyAStimDurInMS;
		
		//user settings location
		private String settingsPath = new String(".dEMDRrc");
		
		//internal schitt
		private boolean foundUserSettings;
		
		//constructor(s)
		public void UserSet() throws Exception {
			File uSettings = new File(settingsPath);
			
			if (uSettings.exists() && !uSettings.canRead()) {
				foundUserSettings = false;
				throw new Exception("Unable to read file: " + settingsPath);
			} else if (!uSettings.exists()) {
				foundUserSettings = false;
				
				//set to defaults and run along
				MyKittWidth = MaxX; MyKittHeight = MaxY;
				MyBgColor = bgColor; MyFgColor = fgColor;
				MySessionDuration = SessionDurationInMin;	//used as the base for MyTotalIterations
				MyPauseInMS = DefaultPauseInMS;
				MyBeepForAudio = BeepForAudio;
				MyStereoAudio = StereoAudio;
				MyAStimFreq = AStimFreq; MyAStimDurInMS = AStimDurInMS;
			} else {
				foundUserSettings = true;
			}
			
			//determine timing details
			MyTotalIterations = (int)((MySessionDuration * 60 * 1000) / MyPauseInMS);
		}
	}
}
