package dEMDRC;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
	public static final boolean BeepForAudio = false;
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
	public static ArrayList<UserPrefsHandler.ControlGrid> controlStruct = new ArrayList<UserPrefsHandler.ControlGrid>();	
	
	//user modifiable values
	public class UserSet {
		//available options
		public HashMap<String, ControlType> availableOptions = new HashMap<String, ControlType>();
		
		//display options
		//NOTE: we'll be putting window sizes in here at some point, but initially our defaults are good enough; this can
		//be saved for a beta version
		//NOTE: we're also changing this to use a HashMap now, because eff this
		/*public int MyKittWidth, MyKittHeight;
		//we can work with window positions at that point, too
		public Color MyBgColor, MyFgColor;
		
		//timings
		public int MySessionDuration, MyPauseInMS, MyTotalIterations;
		
		//audio
		public boolean MyBeepForAudio, MyStereoAudio;
		public int MyAStimFreq, MyAStimDurInMS;*/
		
		//new structure for the customized settings
		//obviously, with this rudimentary implementation, we're going to have to deal with conversion of Colors & booleans
		//to and from integers.  why do I have the feeling that that won't make it through more than a week of code iterations?
		public HashMap<String, Integer> customizedSettings = new HashMap<String, Integer>();
		
		//user settings location
		public final String settingsPath = new String(".dEMDRrc");
		
		//internal schitt
		private boolean foundUserSettings;
		
		//constructor(s)
		public UserSet() {			
			File uSettings = new File(settingsPath);
			
			if (uSettings.exists() && !uSettings.canRead()) {
				foundUserSettings = false;
				System.err.println("uSettings exists, but cannot be read!");
			} else if (!uSettings.exists()) {
				foundUserSettings = false;
				
				//set to defaults and run along
				//NOTE: this really needs to be changed to a HashMap 8o| (see initStructs())
				/*MyKittWidth = MaxX; MyKittHeight = MaxY;
				MyBgColor = bgColor; MyFgColor = fgColor;
				MySessionDuration = SessionDurationInMin;	//used as the base for MyTotalIterations
				MyPauseInMS = DefaultPauseInMS;
				MyBeepForAudio = BeepForAudio;
				MyStereoAudio = StereoAudio;
				MyAStimFreq = AStimFreq; MyAStimDurInMS = AStimDurInMS;*/
			} else {
				//foundUserSettings = true;		//I'm really starting to think that we don't need this variable
				try {
					HeadsUp.uSet = readUserSet(uSettings);
				} catch (Exception ex) {
					System.err.println("Issues reading/unpacking data from " + uSettings.getName() + " into " +
									   "HeadsUp.userPrefsDisplay.uSet\nMsg: " + ex.getMessage());
				}
			}
			
			//determine timing details
			//MyTotalIterations = (int)((MySessionDuration * 60 * 1000) / MyPauseInMS);
		}
		
		//getters/setters
		public boolean getFoundUserSettings() {	//really not sure if I'm gonna need this or not
			return foundUserSettings;
		}
		
		/**
		 * Method will attempt to load the user's settings
		 * 
		 * @param uSetFile
		 */
		@SuppressWarnings("unused")
		private UserSet readUserSet(File uSetFile) throws Exception {
			Object tmpUserSet = null;
			FileInputStream dataBarf = null;
			
			try {
				dataBarf = new FileInputStream(uSetFile);
			} catch (IOException ex) {
				if (debugging || testing) {
					System.err.println("* Error opening InputStream to " + uSetFile.getName() + "\nMsg: " + 
									   ex.getMessage());
				}
			}
			
			//might want to do some error checking here based on the # of bytes .available() compared to the size of our
			//UserSet object
			
			try {
				dataBarf.read((byte[])tmpUserSet);
			} catch (IOException ex) {
				System.err.println("* Error reading from FileInputStream to " + uSetFile.getName() + "\nMsg: " +
								   ex.getMessage());
				throw new Exception("Error reading from " + uSetFile.getName());
			}
			
			dataBarf.close();
			return (UserSet)tmpUserSet;
		}
		
		/**
		 * Method initializes the data structures used to hold the user preferences panel labels & controls' data
		 */
		public void initStructs() {
			int cntr = 0;
			int min, max, cur;
			
			if (debugging) {
				System.out.println("Loading structs . . .");
			}
			
			for (String ouah : optionText) {
				min = -1; max = -1; cur = -1;
				
				//setting individual control specifics
				switch (ouah) {
					case "Bar Width":
						min = 640;
						max = MaxX;
						cur = max;
						break;
					case "Bar Height":
						min = (BoxMaxY * 3);
						max = MaxY;
						cur = max;
						break;
					case "Background Color":
						min = 0;	//Color.BLACK.getIntArgbPre();	//not sure about this...
						max = Integer.MAX_VALUE;
						cur = 0;	//MyBgColor.getIntArgbPre();	//again, :-?(beep)
						break;
					case "Foreground Color":
						min = 0;	//Color.BLACK.getIntArgbPre();	//not sure about this...
						max = Integer.MAX_VALUE;
						cur = 0;	//MyFgColor.getIntArgbPre();	//again, :-?(beep)
						break;
					case "Total Duration":
						min = 1;
						max = 12;	//arbitrary; will need to look up medical data for EMDR for this value to be proper
						cur = SessionDurationInMin;
						break;
					case "Display Speed":
						min = MinimumPauseInMS;
						max = MaximumPauseInMS;
						cur = DefaultPauseInMS;
						break;
					case "Beep":
					case "Stereo Audio":
						min = 0;
						max = 1;
						cur = 0;
						break;
					case "Tone Frequency":
						min = MinAStimFreq;
						max = MaxAStimFreq;
						cur = AStimFreq;
						break;
					case "Tone Duration":
						min = MinAStimDur;
						max = MaxAStimDur;
						cur = AStimDurInMS;
						break;
				}
				
				controlStruct.add(HeadsUp.userPrefsDisplay.new ControlGrid(ouah, optionControl[cntr++], min, max, cur));
			}
			
			//this is not the optimal way to do this 8o|
			HeadsUp.uSet.customizedSettings.put("KittWidth", MaxX);
			HeadsUp.uSet.customizedSettings.put("KittHeight", MaxY);
			HeadsUp.uSet.customizedSettings.put("BgColor", bgColor.getIntArgbPre());	//bogus, almost
			HeadsUp.uSet.customizedSettings.put("FgColor", fgColor.getIntArgbPre());	//certainly here
			HeadsUp.uSet.customizedSettings.put("SessionDuration", SessionDurationInMin);
			HeadsUp.uSet.customizedSettings.put("PauseInMS", DefaultPauseInMS);
			HeadsUp.uSet.customizedSettings.put("BeepAudio", 0);
			HeadsUp.uSet.customizedSettings.put("StereoAudio", 1);
			HeadsUp.uSet.customizedSettings.put("AStimFrequency", AStimFreq);
			HeadsUp.uSet.customizedSettings.put("AStimDuration", AStimDurInMS);
			
			if (testing) {
				System.out.println(HeadsUp.uSet.toString());
			}
		}
		
		/**
		 * Method implements the same toString() functionality that most objects have; should be useful for debugging, but
		 * probably not a whole lot else
		 * 
		 * @return String
		 */
		public String toString() {
			//okay fuck this shit, we're switching to a HashMap for the customizable settings
			return "\nAudio Options\n-------------\n" + 
				   "Audio stim duration: " + HeadsUp.uSet.customizedSettings.get("AStimDuration") + 
				   "\t\tAudio stim frequency: " + HeadsUp.uSet.customizedSettings.get("AStimFrequency") +
				   "\tBeep: " + HeadsUp.uSet.customizedSettings.get("BeepAudio") + "\n" +
				   "Stereo audio: " + HeadsUp.uSet.customizedSettings.get("StereoAudio") +
				   "\n\nSession Options\n---------------\n" +
				   "Pause duration: " + HeadsUp.uSet.customizedSettings.get("PauseInMS") +
				   "\tSession duration: " + HeadsUp.uSet.customizedSettings.get("SessionDuration") +
				   "\n\nDisplay Options\n---------------\n" +
				   "Kitt width: " + HeadsUp.uSet.customizedSettings.get("KittWidth") + "\tKitt height: " +
				   HeadsUp.uSet.customizedSettings.get("KittHeight") + "\nBackground color: " +
				   HeadsUp.uSet.customizedSettings.get("BgColor") + "\tForeground color: " +
				   HeadsUp.uSet.customizedSettings.get("FgColor") + "\n-=-=-=-=-";
		}
	}
}
