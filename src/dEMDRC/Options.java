package dEMDRC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
	public static final int DefaultPauseInMS = 15;
	public static final int MinimumPauseInMS = 10;
	public static final int MaximumPauseInMS = 150;
	public static final int TotalIterations = 500;	//testing purposes
	//public static final int TotalIterations = (SessionDurationInMin * 60 * (1000 / PauseInMS));	//production
	
	//audio options
	public static final boolean BeepForAudio = true;
	public static final int AStimFreq = 432;	//we'll probably want a higher chakra ;)
	public static final int AStimDurInMS = 15;
	public static final int ASampleRate = 16 * 1024;
	public static final boolean StereoAudio = true;
	private static final int MinAStimFreq = 20;
	private static final int MaxAStimFreq = 20000;
	private static final int MinAStimDur = 10;
	private static final int MaxAStimDur = 250;
	
	//ouah
	public static final boolean debugging = true;
	public static final boolean testing = true;
	
	//enums
	public static enum StereoSide { LEFT, RIGHT };
	public static enum ControlType { SLIDER, SPECTRUM, NUMERIC, TOGGLE };
	
	public String[] optionText = { "Bar Width", "Bar Height", "Background Color", "Foreground Color", "Total Duration",
		    					   "Display Speed", "Beep", "Stereo Audio", "Tone Frequency", "Tone Duration" };
	public ControlType[] optionControl = { ControlType.SLIDER, ControlType.SLIDER, ControlType.SPECTRUM,
										   ControlType.SPECTRUM, ControlType.NUMERIC, ControlType.SLIDER,
										   ControlType.TOGGLE, ControlType.TOGGLE, ControlType.NUMERIC,
										   ControlType.NUMERIC };
	
	
	//user modifiable values
	public class UserSet {
		//available options
		public HashMap<String, ControlType> availableOptions = new HashMap<String, ControlType>();
		public ArrayList<UserPrefs.ControlGrid> controlStruct = new ArrayList<UserPrefs.ControlGrid>();
		
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
		private UserPrefs userPrefsTmp;
		
		//constructor(s)
		public void UserSet() throws Exception {
			File uSettings = new File(settingsPath);
			
			if (uSettings.exists() && !uSettings.canRead()) {
				foundUserSettings = false;
				throw new Exception("Unable to read file: " + settingsPath);
			} else if (!uSettings.exists()) {
				foundUserSettings = false;
				
				//set to defaults and run along
				//NOTE: this really needs to be changed to a HashMap 8o|
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
		
		/**
		 * Just some internal garbage here
		 * 
		 */
		private void initStructs() {
			int cntr = 0;
			UserPrefs.ControlGrid nakk = userPrefsTmp.new ControlGrid();
			
			//original (parallel) crap
			for (int ouah = 0; ouah < optionText.length; ouah++) {
				availableOptions.put(optionText[ouah], optionControl[ouah]);
			}
			
			//a better data structure (I think?)
			for (String ouah : optionText) {
				nakk.setName(ouah);
				nakk.setcType(optionControl[cntr]);
				
				//setting individual control specifics
				switch (ouah) {
					case "Bar Width":
						nakk.setMin(640);
						nakk.setMax(MaxX);
						nakk.setCurVal(MyKittWidth);
						break;
					case "Bar Height":
						nakk.setMin(BoxMaxY * 3);
						nakk.setMax(MaxY);
						nakk.setCurVal(MyKittHeight);
						break;
					case "Background Color":
						nakk.setMin(Color.BLACK.getIntArgbPre());	//not sure about this...
						nakk.setMax(Integer.MAX_VALUE);
						nakk.setCurVal(MyBgColor.getIntArgbPre());	//again, :-?(beep)
						break;
					case "Foreground Color":
						nakk.setMin(Color.BLACK.getIntArgbPre());	//not sure about this...
						nakk.setMax(Integer.MAX_VALUE);
						nakk.setCurVal(MyFgColor.getIntArgbPre());	//again, :-?(beep)
						break;
					case "Total Duration":
						nakk.setMin(1);
						nakk.setMax(12);	//arbitrary; will need to look up medical data for EMDR for this value to be proper
						nakk.setCurVal(MySessionDuration);
						break;
					case "Display Speed":
						nakk.setMin(MinimumPauseInMS);
						nakk.setMax(MaximumPauseInMS);
						nakk.setCurVal(MyPauseInMS);
						break;
					case "Beep":
					case "Stereo Audio":
						nakk.setMin(0);
						nakk.setMax(1);
						nakk.setCurVal(0);
						break;
					case "Tone Frequency":
						nakk.setMin(MinAStimFreq);
						nakk.setMax(MaxAStimFreq);
						nakk.setCurVal(MyAStimFreq);
						break;
					case "Tone Duration":
						nakk.setMin(MinAStimDur);
						nakk.setMax(MaxAStimDur);
						nakk.setCurVal(MyAStimDurInMS);
						break;
				}
				
				controlStruct.add(nakk);
			}
		}
	}
}
