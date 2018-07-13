package dEMDRC;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;

public class UserPrefs implements EventHandler {

	@Override
	public void handle(Event arg0) {
		// TODO find out how much fun it's going to be implementing opening up a new control panel from here instead
		//		of from HeadsUp
		GridPane userSettings = new GridPane();
		
		//NOTE: before we start taking input from these widgets we should probably make other controls inactive
		//		(see HeadsUp.blockInput())
		HeadsUp.blockInput();
	}

}
