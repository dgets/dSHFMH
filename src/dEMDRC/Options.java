package dEMDRC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
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
	public int TotalIterations;
	
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
	public HashMap<String, Integer> debugging = new HashMap<String, Integer>();	//can't figure out declaration syntax :|
																				//so I guess it'll go in a constructor
	
	//enums
	public static enum StereoSide { LEFT, RIGHT };
	public static enum ControlType { SLIDER, SPECTRUM, NUMERIC, TOGGLE, TEXT };
	
	public String[] optionText = { "Bar Width", "Bar Height", "Background Color", "Foreground Color", "Total Duration",
		    					   "Display Speed", "Beep", "Stereo Audio", "Tone Frequency", "Tone Duration" };
	public ControlType[] optionControl = { ControlType.SLIDER, ControlType.SLIDER, ControlType.SPECTRUM,
										   ControlType.SPECTRUM, ControlType.NUMERIC, ControlType.SLIDER,
										   ControlType.TOGGLE, ControlType.TOGGLE, ControlType.NUMERIC,
										   ControlType.NUMERIC };
	public static ArrayList<UserPrefsHandler.ControlGrid> controlStruct = new ArrayList<UserPrefsHandler.ControlGrid>();	
	
	//constructor(s)
	public Options() {
		//this is basically just needed at this point because I can't figure out the Map.Entry K+V syntax in order to
		//set the keys & values for the new 'debugging' HashMap above.  whatever, it's all good; can only figure out
		//so much with no good physical books handy and no internet available
		debugging.put("general", 1);
		debugging.put("testing", 1);
		debugging.put("fileio", 1);
		
		//yeah this is redundant.  fuggoff
		if (debugging.get("testing") == 1) {
			TotalIterations = 500;
		} else {
			TotalIterations = (HeadsUp.uSet.customizedSettings.get("Total Duration") * 60 * 
								(1000 / HeadsUp.uSet.customizedSettings.get("Display Speed")));
		}
	}
	
	/**
	 * Method tells us if we're doing general debugging
	 * 
	 * @return
	 */
	public boolean debuggingGen() {
		return (debugging.get("general") == 1);
	}
	
	/**
	 * Method informs us if we're doing general _or_ testing debugging
	 * 
	 * @return
	 */
	public boolean debuggingGenTest() {
		return ((debugging.get("general") == 1) || (debugging.get("testing") == 1));
	}
	
	/**
	 * Method just informs about testing debugging
	 * 
	 * @return
	 */
	public boolean debuggingTest() {
		return (debugging.get("testing") == 1);
	}
	
	/**
	 * Method that lets us know if we're doing fileIO debugging
	 * 
	 * @return
	 */
	public boolean debuggingFileIO() {
		return (debugging.get("fileio") == 1);
	}
	
	//user modifiable values
	public class UserSet implements Serializable {
		private static final long serialVersionUID = 4271985271438883908L;

		//available options
		public HashMap<String, ControlType> availableOptions = new HashMap<String, ControlType>();
		
		//display options
		//NOTE: we'll be putting window sizes in here at some point, but initially our defaults are good enough; this can
		//be saved for a beta version
		//NOTE: we're also changing this to use a HashMap now, because eff this
		
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
			} else if (!uSettings.exists()) {	//no uSettings :'C
				foundUserSettings = false;
				
				//set to defaults, or remain there, and run along
				
			} else {
				foundUserSettings = true;		//I'm really starting to think that we don't need this variable
				try {
					if (debuggingFileIO()) {
						System.out.println("In UserSet() constructor\nsettingsPath: " + settingsPath + 
										   "\nuSettings general info: " + uSettings.toString() + 
										   "uSettings.length() = " + uSettings.length() +
										   "\t\tuSettings.canRead() = " + uSettings.canRead() +
										   "\t\tuSettings.canWrite() = " + uSettings.canWrite() + "\n");
					}	
					HeadsUp.uSet.customizedSettings = readUserSet(uSettings);
				} catch (Exception ex) {
					//our hangup is coming in above 8o|
					System.err.println("Issues reading/unpacking data from " + uSettings.getName() + " into " +
									   "HeadsUp.userPrefsDisplay.uSet\nMsg: " + ex.getMessage());
				}
			}
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
		private HashMap<String, Integer> readUserSet(File uSetFile) throws Exception, NullPointerException {
			Object tmpUserSettings = null;
			FileInputStream dataBarf = null;
			
			try {
				if (HeadsUp.opts.debuggingFileIO()) {
					System.out.println("Attempting to instantiate & open FileInputStream 'dataBarf'\n" +
									   "dataBarf target: " + uSetFile.getAbsolutePath() + "\n\n");
				}
				dataBarf = new FileInputStream(uSetFile);
			} catch (IOException ex) {
				if (HeadsUp.opts.debuggingFileIO()) {
					System.err.println("* Error opening InputStream to " + uSetFile.getName() + "\nMsg: " + 
									   ex.getMessage());
				}
				throw new Exception("Unable to open InputStream to uSetFile due to IO issues");
			} catch (NullPointerException ex) {
				System.err.println("* Null pointer working with instaniating 'dataBarf' in " +
								   "Options.UserSet.readUserSet()\nMsg: " + ex.getMessage());
				dataBarf.close();
				throw new Exception("Unable to open InputStream to uSetFile due to null pointer issues");
			}
			
			//might want to do some error checking here based on the # of bytes .available() compared to the size of our
			//UserSet object
			try {
				if (HeadsUp.opts.debuggingFileIO()) {
					System.out.println("Attempting read from FIS 'dataBarf'\ntmpUserSet general details: " +
									   tmpUserSettings.toString() + "\ndataBarf general details: " +
									   dataBarf.toString() + "\ndataBarf bytes remaining: " + dataBarf.available());
				}
				dataBarf.read((byte[])tmpUserSettings);
			} catch (IOException ex) {
				System.err.println("* Error reading from FileInputStream to " + uSetFile.getName() + "\nMsg: " +
								   ex.getMessage());
				throw new Exception("Error reading from uSetFile (IOException)");
			} catch (NullPointerException ex) {
				//here's where we've got the hangup right now
				System.err.println("* Null pointer working with reading from 'dataBarf' in " +
								   "Options.UserSet.readUserSet()\nMsg: " + ex.getMessage() + "\ndataBarf: " +
								   dataBarf.toString() + "\ntmpUserSet: " + ((UserSet)tmpUserSettings).toString());
				throw new Exception("Error reading from uSetFile (NullPointerException)");
			} finally {
				dataBarf.close();
			}
			
			if (HeadsUp.opts.debuggingFileIO()) {
				System.out.println("tmpUserSet has deserialized contents: " + ((UserSet)tmpUserSettings).toString());
			}
			
			return (HashMap<String, Integer>)tmpUserSettings;
		}
		
		/**
		 * Method initializes the data structures used to hold the user preferences panel labels & controls' data
		 */
		public void initStructs() {
			int cntr = 0;
			int min, max, cur;
			
			if (HeadsUp.opts.debuggingGen()) {
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
			
			File uSettings = new File(HeadsUp.uSet.settingsPath);
			FileInputStream rawGush;
			ObjectInputStream gush = null;
			if (uSettings.exists() && uSettings.canRead()) {
				try {
					rawGush = new FileInputStream(uSettings);
					gush = new ObjectInputStream(rawGush);
				} catch (FileNotFoundException ex) {
					System.err.println("So yeah, a case I just ruled out by algorithmic testing somehow just happened.\n" +
									   "Msg: " + ex.getMessage());
				} catch (IOException ex) {
					System.err.println("IO issues while trying to open streams to read defaults!\nMsg: " + ex.getMessage());
				}
				
				try {
					HeadsUp.uSet.readObject(gush);
				} catch (ClassNotFoundException ex) {
					System.err.println("Class not found exception!\nMsg: " + ex.getMessage());
				} catch (IOException ex) {
					System.err.println("IO issues while trying to load defaults!\nMsg: " + ex.getMessage());
				}
			}
			
			
			//this is not the optimal way to do this 8o|
			//also we should put this bit in a separate method, so that I don't have to go through the above loop & switch/case
			//when I'm initializing everything due to a bogus serialization stream read attempt
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
			
			if (HeadsUp.opts.debuggingTest()) {
				System.out.println(HeadsUp.uSet.customizedSettings.toString());
			}
		}
		
		/**
		 * Method is required for writing objects during serialization
		 * 
		 * @param java.io.ObjectOutputStream out
		 * @throws IOException
		 */
		private void writeObject(java.io.ObjectOutputStream out) throws IOException {
			//out.defaultWriteObject();	//think I'll just do this manually for now
			try {
				out.writeObject(HeadsUp.uSet.customizedSettings);
			} catch (IOException ex) {
				System.out.println("Err writing serialized settings!\nMsg: " + ex.getMessage());
				throw ex;
			} finally {
				//maybe we should wipe the file if it wasn't written to disk properly?
				out.close();
			}
		}
		
		/**
		 * Method is required for reading objects from serialized storage
		 * 
		 * @param java.io.ObjectInputStream in
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		@SuppressWarnings("unchecked")
		private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
			try {
				HeadsUp.uSet.customizedSettings = (HashMap<String, Integer>)in.readObject();
			} catch (IOException ex) {
				System.out.println("Err reading/deserializing settings!\nMsg: " + ex.getMessage());
				throw ex;
			} catch (ClassNotFoundException ex) {
				System.out.println("Err reading/deserializing settings!\nMsg: " + ex.getMessage());
				throw ex;
			} finally {
				in.close();
			}
		}
		
		/**
		 * So yeah I guess this one is for initializing HeadsUp.uSet.customizedSettings in case of an incomplete or tampered
		 * with serialization stream
		 * 
		 * @throws ObjectStreamException
		 */
		@SuppressWarnings("unused")
		private void readObjectNoData() throws ObjectStreamException {
			HeadsUp.uSet.initStructs();
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
