package dEMDRC;

import dEMDRC.Options.ControlType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserPrefs implements EventHandler<ActionEvent> {
	private Stage userSetStage = new Stage();
	private Options opts = new Options();
	public Options.UserSet uSet = opts.new UserSet();
	
	private static double worldX = -1;
	private static double worldY = -1;
	
	//private static Options godOuahGlobals = new Options();
	//private static Options.UserSet userOptions = godOuahGlobals.new UserSet();

	@Override
	public void handle(ActionEvent arg0) {
		GridPane userSettings = new GridPane();
		Button saveExit = new Button("Save & Exit");
		Button abandonExit = new Button("Abandon & Exit");
		
		/*Options opts = new Options();
		Options.UserSet uSet = opts.new UserSet();*/
		
		HeadsUp.blockInput();
		uSet.initStructs();
		//ControlGrid[] prefControls = new ControlGrid[opts.optionText.length];	//wtF-f-f not initialized?
		
		int cntr = 2;
		
		userSettings.setVgap(5); userSettings.setHgap(3);
		
		//setting description column
		userSettings.add(new Label("Setting"), 0, 0);
		userSettings.add(new Label("Value"), 1, 0);
		
		if (Options.debugging) {
			System.out.println("Initializing prefControls . . ");
			System.out.println("Options.controlStruct.size() = " + Options.controlStruct.size());
			//System.out.println("HeadsUp.userSettings.availableOptions.size() = " + HeadsUp.userSettings.availableOptions.size());
			System.out.println("uSet.availableOptions.size() = " + uSet.availableOptions.size());
		}
		
		//since the below for loop isn't working, let's try this (constructor doesn't seem to be initializing the individual
		//array members)
		/*for (int cntr2 = 0; cntr2 < HeadsUp.userSettings.availableOptions.size(); cntr2++) {
			
		}*/
		for (ControlGrid prefCtrl : Options.controlStruct) {
			if (Options.debugging) {
				System.out.println("Adding prefCtrl: " + prefCtrl.getName());
			}
			
			userSettings.add(new Label(prefCtrl.getName()), 0, cntr);
			switch (prefCtrl.getCType()) {
				case SLIDER:
					userSettings.add(new Slider(prefCtrl.getMin(), prefCtrl.getMax(), prefCtrl.getCurVal()), 1, cntr);
					break;
				case SPECTRUM:	//need to figure out how to turn an int into a Color?
					//userSettings.add(new Label("In Progress"), 1, (ouah + 2));
					userSettings.add(new ColorPicker(), 1, cntr);
					break;
				case NUMERIC:
					userSettings.add(new TextField(), 1, cntr);
					userSettings.add(new TextField(Integer.toString(prefCtrl.getCurVal())), 1, cntr);
					break;
				case TOGGLE:
					userSettings.add(new CheckBox(prefCtrl.getName()), 1, cntr);
					break;
				default:
					userSettings.add(new Label("Houston, we've had a problem"), 1, cntr);
			}
			cntr++;
		}
		/*for (int ouah = 0; ouah < godOuahGlobals.optionText.length; optName = godOuahGlobals.optionText[ouah++]) {
			//userOptions.availableOptions.put(optName, godOuahGlobals.optionControl);
			userSettings.add(new Label(optName), 0, (ouah + 2));
		}
		
		//setting value/adjustment column
		//NOTE: This still needs to be modified to use the new controlStruct data
		userSettings.add(new Label("Value"), 1, 0);
		optCtrl = godOuahGlobals.optionControl[0];
		for (int ouah = 1; ouah < godOuahGlobals.optionControl.length; optCtrl = godOuahGlobals.optionControl[ouah++]) {
			switch (optCtrl) {
				case SLIDER:
					userSettings.add(new Slider(), 1, (ouah + 2));
					break;
				case SPECTRUM:
					//userSettings.add(new Label("In Progress"), 1, (ouah + 2));
					userSettings.add(new ColorPicker(), 1, (ouah + 2));
					break;
				case NUMERIC:
					userSettings.add(new TextField(), 1, (ouah + 2));
					break;
				case TOGGLE:
					userSettings.add(new CheckBox(), 1, (ouah + 2));
					break;
				default:
					userSettings.add(new Label("Houston, we've had a problem"), 1, (ouah + 2));
			}
		}*/
		
		saveExit.setOnAction(new SaveNExit());
		abandonExit.setOnAction(new AbandonNExit());
		userSettings.add(saveExit, 0, 13);
		userSettings.add(abandonExit, 2, 13);
		
		Scene userSetScene = new Scene(userSettings);
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
	public static void setWorldXY(double x, double y) {
		worldX = x;
		worldY = y;
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
		}
	}
	
	//event handlers
	private class SaveNExit implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			// TODO implement moar than testing code
			guhUpDown();
		}
		
	}
	
	private class AbandonNExit implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			// TODO moar than testing code, detc
			guhUpDown();
		}
		
	}
	
}
