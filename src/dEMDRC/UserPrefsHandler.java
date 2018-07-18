package dEMDRC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

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

	@Override
	public void handle(ActionEvent arg0) {
		Button saveExit = new Button("Save & Exit");
		Button abandonExit = new Button("Abandon & Exit");
		
		HeadsUp.blockInput();
		
		int cntr = 2;
		
		userSettingsGrid.setVgap(5); userSettingsGrid.setHgap(3);
		
		//setting description column
		//NOTE: these need to stand out a little bit; find out how to mess with the font & attributes
		userSettingsGrid.add(new Label("Setting"), 0, 0);
		userSettingsGrid.add(new Label("Value"), 1, 0);
		
		if (HeadsUp.opts.debuggingGen()) {
			System.out.println("\nInitializing prefControls . . ");
			System.out.println("Options.controlStruct.size() = " + Options.controlStruct.size());
			System.out.println("uSet.availableOptions.size() = " + HeadsUp.uSet.availableOptions.size() + "\n");
		}
		
		for (ControlGrid prefCtrl : Options.controlStruct) {
			if (HeadsUp.opts.debuggingGenTest()) {
				System.out.print("Adding prefCtrl: " + prefCtrl.getName());
			}
			
			userSettingsGrid.add(new Label(prefCtrl.getName()), 0, cntr);
			switch (prefCtrl.getCType()) {
				case SLIDER:
					userSettingsGrid.add(new Slider(prefCtrl.getMin(), prefCtrl.getMax(), prefCtrl.getCurVal()), 1, cntr);
					break;
				case SPECTRUM:	//need to figure out how to turn an int into a Color?
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
		}
		
		//construcor(s)
		public ControlGrid(String name, ControlType controlType, int minValue, int maxValue, int curValue) {
			setName(name);
			setCType(controlType);
			setMin(minValue);
			setMax(maxValue);
			setCurVal(curValue);
			
			if ((controlType == Options.ControlType.NUMERIC) || (controlType == Options.ControlType.SLIDER) ||
				(controlType == Options.ControlType.SPECTRUM) || (controlType == Options.ControlType.TOGGLE)) {
				
			}
		}
		
		//general methods
		/*private void setUserData(Options.ControlType thisCtrlType) {
			
		}*/
	}
	
	//event handlers
	private class SaveNExit implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			try {
				//first we need to set up everything in HeadsUp.uSet, etc
				
				
				//now we can save it
				doSave();
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
			guhUpDown();
		}
		
		/**
		 * Method serializes & saves HeadsUp.uSet.customizedSettings
		 * 
		 * @return
		 * @throws Exception
		 */
		private Options.UserSet doSave() throws Exception {
			//first we need to populate the settings, derp
			populateSettings();
			
			File uSettings = new File(HeadsUp.uSet.settingsPath);
			if (uSettings.exists()) {
				//wipe the old
				try {
					uSettings.delete();
					uSettings.createNewFile();
					uSettings.setWritable(true);
				} catch (IOException ex) {
					System.err.println("Issues trying to save settings!\nMsg: " + ex.getMessage());
					throw new Exception("Unable to save user settings");
				}
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
		}
		
		/**
		 * Method populates user settings with data from the UserPrefs form
		 * 
		 */
		private void populateSettings() {
			//.getAccessibleText() is probably our friend here
			Options.ControlType tmpControlType = null;
			AccessibleRole ctrlRole = null;
			String tmpBlurb = null;
			Node tmpNode = null;
			
			if (HeadsUp.opts.debuggingTest()) {
				System.out.println("\nUserPrefsDisplay.populateSettings\n-=-=-=-=-");
			}
			
			//for (int cntr = 3; cntr <= (HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().size() - 3); cntr += 2) {
			//neither one of those guesses on the proper indexing of that GridPane() were anywhere close to accurate; I'll
			//have to figure out the logic behind the shit that I'm implementing now at some point to make it more dynamic,
			//but I've got enough to improve it for now, at least
			
			for (int cntr = 0; cntr < HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().size(); cntr++) {
			//for (int cntr = 0; cntr <= 11; cntr++) {	//there are actually 11 entries, but the last 2 are the save/abandon buttons
				//tmpControlType = HeadsUp.uSet.availableOptions.get(
				//		HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().get(cntr).getUserData());
				
				ctrlRole = HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().get(cntr).getAccessibleRole();
				if (HeadsUp.opts.debuggingTest()) {
					System.out.print(cntr + " role: " + ctrlRole);				
				}
				
				/*Label ctrlText = (Label)HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().get(cntr - 1);
				tmpControlType = HeadsUp.uSet.availableOptions.get(ctrlText.getText());
				
				if (HeadsUp.opts.debuggingGenTest()) {
					System.out.println("Text for #" + cntr + ": " + tmpControlType);
				}
				
				switch (tmpControlType) {
				case Options.ControlType.:
					
					
				}*/
				
				tmpNode = HeadsUp.userPrefsDisplay.userSettingsGrid.getChildren().get(cntr);
				switch (ctrlRole) {
					case TEXT:
						tmpBlurb = ((Label)tmpNode).getText();
						break;
					case SLIDER:
						tmpBlurb = Integer.toString((int)((Slider)tmpNode).getValue());
						break;
					case TEXT_FIELD:
						tmpBlurb = ((TextField)tmpNode).getText();
						break;
						
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
			guhUpDown();
		}
		
	}
	
}
