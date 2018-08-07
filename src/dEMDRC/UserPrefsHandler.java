package dEMDRC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import dEMDRC.Options.ControlType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
//import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserPrefsHandler implements EventHandler<ActionEvent> {
	private Stage userSetStage = new Stage();
	private GridPane userSettingsGrid = new GridPane();
	
	private double worldX = -1;
	private double worldY = -1;
	
	private ArrayList<UserPrefsHandler.ControlGrid> controlStruct = new ArrayList<UserPrefsHandler.ControlGrid>();

	@Override
	public void handle(ActionEvent arg0) {
		Button saveExit = new Button("Save & Exit");
		Button abandonExit = new Button("Abandon & Exit");
		Label lblSetting = new Label("Setting");
		Label lblValue = new Label("Value");
		
		//gotta make it PWETTY
		saveExit.setStyle("-fx-font-weight: bold;");
		abandonExit.setStyle("-fx-font-weight: bold;");
		lblSetting.setStyle("-fx-font-weight: bold;");
		lblValue.setStyle("-fx-font-weight: bold;");
		
		//these should be bundled; durrrr
		//HeadsUp.togglePause();	- significant issues to be fixed with this
		HeadsUp.blockInput();
		
		int cntr = 2;
		
		userSettingsGrid.setVgap(5); userSettingsGrid.setHgap(3);
		
		//setting description column
		userSettingsGrid.add(lblSetting, 0, 0);
		userSettingsGrid.add(lblValue, 1, 0);
		
		if (HeadsUp.opts.debuggingGen()) {
			System.out.println("\nInitializing prefControls . . ");
			System.out.println("Options.controlStruct.size() = " + controlStruct.size());
		}
		
		for (ControlGrid prefCtrl : controlStruct) {
			if (HeadsUp.opts.debuggingGenTest()) {
				System.out.print("Adding prefCtrl: " + prefCtrl.getName());
			}
			
			userSettingsGrid.add(new Label(prefCtrl.getName()), 0, cntr);
			switch (prefCtrl.getCType()) {
				case SLIDER:
					userSettingsGrid.add(new Slider(prefCtrl.getMin(), prefCtrl.getMax(), prefCtrl.getCurVal()), 1, cntr);
					break;
				case SPECTRUM:	//need to figure out how to turn an int into a Color? (lol not likely)
								//I really don't think that's gonna work, we're gonna need something else
								//unfortunately that means a more complex struct, methinks... or perhaps we could use
								//.min, .max, and .cur for the 3 integer values (RGB)...  ;)
					userSettingsGrid.add(new Label("In Progress"), 1, cntr);
					//userSettingsGrid.add(new ColorPicker(), 1, cntr);
					break;
				case NUMERIC:
					userSettingsGrid.add(new TextField(), 1, cntr);
					userSettingsGrid.add(new TextField(Integer.toString(prefCtrl.getCurVal())), 1, cntr);
					break;
				case TOGGLE:
					userSettingsGrid.add(new CheckBox(prefCtrl.getName()), 1, cntr);
					break;
				default:
					userSettingsGrid.add(new Label("Houston, we've had a problem"), 1, cntr);
			}
			
			if (HeadsUp.opts.debuggingTest()) {
				System.out.println(" is type " + prefCtrl.getCType().toString() + "\nAdding that type to Node's " +
								   "userData field for easier data retrieval/population");
			}
			userSettingsGrid.getChildren().get(cntr).setUserData(prefCtrl.getCType());
			
			cntr++;
		}
		
		saveExit.setOnAction(new SaveNExit());
		abandonExit.setOnAction(new AbandonNExit());
		userSettingsGrid.add(saveExit, 0, 13);
		userSettingsGrid.add(abandonExit, 2, 13);
		
		Scene userSetScene = new Scene(userSettingsGrid);
		userSetStage.setScene(userSetScene);
		userSetStage.setTitle("ya just couldn't leave good enough alone...");
		if ((worldX == -1) || (worldY == -1)) {
			//schitt has not been initialized properly
			userSetStage.setX(450); userSetStage.setY(150);
		} else {
			//it might be nice to actually center this in the display, instead of doing this half-assed staggering
			userSetStage.setX(worldX + (Options.MaxY * 3));
			userSetStage.setY(worldY + (Options.MaxY * 1.25));
		}
		userSetStage.show();
	}

	//getters/setters
	public void setWorldXY(double x, double y) {
		this.worldX = x;
		this.worldY = y;
	}
	
	//general methods, detc
	private void guhUpDown() {
		HeadsUp.restoreInput();
		userSetStage.close();
	}
	
	public void init() {
		int cntr = 0;
		int min, max, cur;
		
		for (String ouah : HeadsUp.opts.optionText) {
			if (HeadsUp.opts.debuggingGen()) {
				System.out.println("Populating UserPrefsHandler instance: " + ouah);
				System.out.println("HeadsUp.uSet.customizedSettings.get(ouah): " + HeadsUp.uSet.customizedSettings.get(ouah));
			}
			min = -1; max = -1; cur = -1;
			
			//setting individual control specifics
			switch (ouah) {
				case "Bar Width":
					min = 640;
					max = Options.MaxX;
					cur = HeadsUp.uSet.customizedSettings.get(ouah);
					break;
				case "Bar Height":
					min = (Options.BoxMaxY * 3);
					max = Options.MaxY;
					cur = HeadsUp.uSet.customizedSettings.get(ouah);
					break;
				case "Background Color":
					min = 0;	//Color.BLACK.getIntArgbPre();	//not sure about this...
					max = Integer.MAX_VALUE;
					cur = 0;	//MyBgColor.getIntArgbPre();	//see issue #11 regarding this crap
					break;
				case "Foreground Color":
					min = 0;	//Color.BLACK.getIntArgbPre();	//ditto
					max = Integer.MAX_VALUE;
					cur = 0;	//MyFgColor.getIntArgbPre();	//ditto
					break;
				case "Session Duration":
					min = 1;
					max = 12;	//arbitrary; will need to look up medical data for EMDR for this value to be proper
					cur = HeadsUp.uSet.customizedSettings.get(ouah);
					break;
				case "Display Speed":
					min = Options.MinimumPauseInMS;
					max = Options.MaximumPauseInMS;
					cur = HeadsUp.uSet.customizedSettings.get(ouah);
					break;
				case "Beep":
				case "Stereo Audio":
					min = 0;
					max = 1;
					cur = 0;
					break;
				case "Tone Frequency":
					min = Options.MinAStimFreq;
					max = Options.MaxAStimFreq;
					cur = HeadsUp.uSet.customizedSettings.get(ouah);
					break;
				case "Tone Duration":
					min = Options.MinAStimDur;
					max = Options.MaxAStimDur;
					cur = HeadsUp.uSet.customizedSettings.get(ouah);
					break;
			}
			
			if (HeadsUp.opts.debuggingGenTest()) {
				System.out.print("Tossing into HeadsUp.userPrefsDisplay via new ControlGrid(): ");
				System.out.println("HeadsUp.opts.optionControl[" + cntr + "]: " + HeadsUp.opts.optionControl[cntr].toString());
				System.out.println("min: " + min + "\t\tmax: " + max + "\t\tcur: " + cur + "\n");
			}
			controlStruct.add(HeadsUp.userPrefsDisplay.new ControlGrid(ouah, HeadsUp.opts.optionControl[cntr++], min, max, cur));
		}
	}
	
	//'regular' subclasses
	public class ControlGrid {
		private String name = new String();
		private ControlType cType;
		private int min, max, curVal;
		
		//getters/setters
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public ControlType getCType() {
			return cType;
		}
		
		public void setCType(ControlType cType) {
			this.cType = cType;
		}
		
		public int getMin() {
			return min;
		}
		
		public void setMin(int min) {
			this.min = min;
		}
		
		public int getMax() {
			return max;
		}
		
		public void setMax(int max) {
			this.max = max;
		}
		
		//omfg do not tell me I just recoded that much duplication from how much sleep I've missed & forgetting about this
		//set of accessors from the class m(  I'm signing off; time for nini
		public int getCurVal() {
			return curVal;
		}
		
		public void setCurVal(int curVal) {
			this.curVal = curVal;
			//this.curVal = HeadsUp.uSet.customizedSettings.get(this.getName());
		}
		
		//constructor(s)
		public ControlGrid(String name, ControlType controlType, int minValue, int maxValue, int curValue) {
			setName(name);
			setCType(controlType);
			setMin(minValue);
			setMax(maxValue);
			setCurVal(curValue);
		}
		
		//general methods
	}
	
	//event handlers
	private class SaveNExit implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			try {
				//now we can save it -- switching to XML
				//doSave();
				doXMLSave();
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
			//HeadsUp.gc.togglePause();
			guhUpDown();
		}
		
		private void doXMLSave() throws Exception {
			//WHY do we need to populate first?  This isn't handled right...
			//populateSettings();
			
			File uSettings = new File(HeadsUp.uSet.settingsPath);
			if (uSettings.exists()) {
				//wipe the old
				resetSaveFile(uSettings);
			}
			
			populateSettings();
			HeadsUp.uSet.saveXMLSettings();
		}
		
		private void resetSaveFile(File uSetFile) {
			try {
				uSetFile.delete();
				uSetFile.createNewFile();
				uSetFile.setWritable(true);
			} catch (IOException ex) {
				System.err.println("Issues trying to reset save file!\nMsg: " + ex.getMessage());
				//throw new Exception("Unable to save user settings");
			}		
		}
		
		/**
		 * Method serializes & saves HeadsUp.uSet.customizedSettings
		 * 
		 * @return
		 * @throws Exception
		 */
		/*private Options.UserSet doSave() throws Exception {
			//first we need to populate the settings, derp
			populateSettings();
			
			File uSettings = new File(HeadsUp.uSet.settingsPath);
			if (uSettings.exists()) {
				//wipe the old
				resetSaveFile(uSettings);
			}
			
			//waiting for testing feedback
			FileOutputStream rawGush = new FileOutputStream(uSettings);
			ObjectOutputStream gush = new ObjectOutputStream(rawGush);
			
			if (HeadsUp.opts.debuggingGenTest()) {
				System.out.println("\nuserSettingsGrid contents\n-=-=-=-\n");
			}
			for (int cntr = 2; cntr < userSettingsGrid.getChildren().size(); cntr++) {
				if (HeadsUp.opts.debuggingGenTest()) {
					System.out.println(userSettingsGrid.getChildren().get(cntr).toString());
				}
			}
			
			//here's the magic
			try {
				gush.writeObject(HeadsUp.uSet.customizedSettings);
			} catch (IOException ex) {
				System.err.println("Unable to write to ObjectOutputStream!\nMsg: " + ex.getMessage());
				throw new Exception("Unable to save settings configuration");
			} finally {
				gush.close();
			}
			
			//um, is this really necessary?  I really haven't slept for quite awhile now...
			return HeadsUp.uSet;	//pretty friggin' sure that's not necessary
		}*/
		
		/**
		 * Method populates user settings with data from the UserPrefs form
		 * 
		 */
		private void populateSettings() throws Exception {
			Options.ControlType tmpControlType = null;
			AccessibleRole ctrlRole = null;
			String tmpBlurb = null, tmpName = null;
			Node tmpNode = null;
			
			if (HeadsUp.opts.debuggingTest()) {
				System.out.println("\nUserPrefsDisplay.populateSettings\n-=-=-=-=-");
			}
			
			for (int cntr = 3; cntr < HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().size(); cntr++) {
				ctrlRole = HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().get(cntr).getAccessibleRole();
				if (HeadsUp.opts.debuggingTest()) {
					System.out.println(cntr + " role: " + ctrlRole);				
				}
				
				tmpNode = HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().get(cntr);
				switch (ctrlRole) {
					case TEXT:
						tmpBlurb = ((Label)tmpNode).getText();
						tmpName = tmpBlurb;
						tmpControlType = Options.ControlType.TEXT;
						break;
					case SLIDER:
						tmpBlurb = Integer.toString((int)((Slider)tmpNode).getValue());
						tmpControlType = Options.ControlType.SLIDER;
						break;
					case TEXT_FIELD:
						tmpBlurb = ((TextField)tmpNode).getText();
						tmpControlType = Options.ControlType.TEXT;
						break;
					case CHECK_BOX:
						if (((CheckBox)tmpNode).isSelected()) {
							tmpBlurb = "1";
						} else {
							tmpBlurb = "0";
						}
						tmpControlType = Options.ControlType.TOGGLE;
						break;
					case BUTTON:
						if (HeadsUp.opts.debuggingGenTest()) {
							System.out.println("Skipping entry for a [non-setting] Node (in this case a Button class)");
						}
						
						cntr++;
						continue;	//actually unless we change more on the form, buttons mean it's not a setting, it's form end
					default:
						tmpBlurb = "Shit's just all fucked";
				}
				
				if (HeadsUp.opts.debuggingGenTest()) {
					System.out.println("Role allows us to grab: " + tmpBlurb);
				}
				
				if ((cntr % 2) == 0) {
					switch (tmpControlType) {
						case SLIDER:
							HeadsUp.uSet.customizedSettings.put(tmpName, (int)Double.parseDouble(tmpBlurb));
							break;
						case NUMERIC:	//this is actually a TEXT_FIELD
							HeadsUp.uSet.customizedSettings.put(tmpName, Integer.parseInt(tmpBlurb));
							break;
						case TOGGLE:	//actually a CHECKBOX
							if ((Integer.parseInt(tmpBlurb) < 0) || (Integer.parseInt(tmpBlurb) > 1)) {
								System.err.println("* Weird error trying to determine a checkbox/toggle value");
								throw new Exception("Error parsing toggle value");
							} else if (Integer.parseInt(tmpBlurb) == 0) {
								HeadsUp.uSet.customizedSettings.put(tmpName, 0);
							} else {
								HeadsUp.uSet.customizedSettings.put(tmpName, 1);
							}
						case SPECTRUM:	//UNIMPLEMENTED CURRENTLY - will be the results of a color picker
							break;
						default:
							System.err.println("Invalid control data found in UserPrefsHandler.SaveNExit.populateSettings()");
							throw new Exception("Invalid control data");
					}
					
					if (HeadsUp.opts.debuggingTest()) {
						System.out.println("Changed setting: " + tmpName + " to value: " + tmpBlurb);
					}
				}
			}
		}
		
		/**
		 * Method will step through userSettingsGrid's kiddos, putting together a HashMap of the relevant shit for populateSettings()
		 * 
		 * I shamelessly stole this code from StackOverflow; can't say I like the algorithm much, though
		 * 
		 * @return HashMap<String, Integer>
		 */
		//@SuppressWarnings("deprecation")
		/*private HashMap<String, Integer> pullFromGrid() {
			HashMap<String, Integer> formData = new HashMap<String, Integer>();
			Object tmpClass = null;
					
			for (int x = 0; x < HeadsUp.userPrefsDisplay.userSettingsGrid.impl_getColumnCount(); x++) {
				for (int y = 0; y < HeadsUp.userPrefsDisplay.userSettingsGrid.impl_getRowCount(); y++) {
					for (Node ouah : HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren()) {
						if ((GridPane.getColumnIndex(ouah) == x) && (GridPane.getRowIndex(ouah) == y)) {
							int curX, curY;
							
							//if Class. ouah.;	//I don't think this is what I wanted it to be
						}
					}
				}
			}
		}*/
	}
	
	private class AbandonNExit implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			//TODO moar than testing code, detc
			//erm, actually this may be all that we need for the 'AbandonNExit' button handler
			//HeadsUp.togglePause(); - significant issues
			guhUpDown();
		}
		
	}
	
}
