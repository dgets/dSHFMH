package dEMDRC;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserPrefs implements EventHandler {
	private Stage userSetStage = new Stage();

	@Override
	public void handle(Event arg0) {
		// TODO find out how much fun it's going to be implementing opening up a new control panel from here instead
		//		of from HeadsUp
		GridPane userSettings = new GridPane();
		Button saveExit = new Button("Save & Exit");
		Button abandonExit = new Button("Abandon & Exit");
		
		HeadsUp.blockInput();
		
		userSettings.setVgap(5); userSettings.setHgap(3);
		userSettings.add(new Label("Setting"), 0, 0);
		userSettings.add(new Label("Value"), 1, 0);
		saveExit.setOnAction(new SaveNExit());
		abandonExit.setOnAction(new AbandonNExit());
		userSettings.add(saveExit, 0, 2);
		userSettings.add(abandonExit, 2, 2);
		
		Scene userSetScene = new Scene(userSettings);
		userSetStage.setScene(userSetScene);
		userSetStage.setTitle("ya just couldn't leave good enough alone...");
		userSetStage.show();
		
	}

	private void guhUpDown() {
		HeadsUp.restoreInput();
		userSetStage.close();
	}
	
	private class SaveNExit implements EventHandler {

		@Override
		public void handle(Event arg0) {
			// TODO implement moar than testing code
			guhUpDown();
		}
		
	}
	
	private class AbandonNExit implements EventHandler {

		@Override
		public void handle(Event arg0) {
			// TODO moar than testing code, detc
			guhUpDown();
		}
		
	}
	
}
