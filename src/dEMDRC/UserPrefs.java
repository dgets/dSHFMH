package dEMDRC;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserPrefs implements EventHandler<ActionEvent> {
	private Stage userSetStage = new Stage();
	private static double worldX = -1;
	private static double worldY = -1;

	@Override
	public void handle(ActionEvent arg0) {
		GridPane userSettings = new GridPane();
		Button saveExit = new Button("Save & Exit");
		Button abandonExit = new Button("Abandon & Exit");
		
		Options godOuahGlobals = new Options();
		Options.UserSet userOptions = godOuahGlobals.new UserSet();
		HeadsUp.blockInput();
		
		String optName = null;
		
		userSettings.setVgap(5); userSettings.setHgap(3);
		
		//setting description column
		userSettings.add(new Label("Setting"), 0, 0);
		/*for (int ouah = 2; ouah <= (userOptions.availableOptions.length - 3); ouah++) {
			userSettings.add(new Label(userOptions.availableOptions[ouah - 2]), 0, ouah);
		}*/
		
		for (int ouah = 0; ouah < godOuahGlobals.optionText.length; optName = godOuahGlobals.optionText[ouah++]) {
			//userOptions.availableOptions.put(optName, godOuahGlobals.optionControl);
			userSettings.add(new Label(optName), 0, (ouah + 2));
		}
		
		//setting value/adjustment column
		userSettings.add(new Label("Value"), 1, 0);
		
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
