package dEMDRC;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.Event;
import javafx.event.EventHandler;
import java.util.Timer;
import java.util.TimerTask;

public class HeadsUp extends Application {
	private Timer gmt;
	
	private Group wutGroot = new Group();
	private Scene kR = new Scene(wutGroot, Options.MaxX, Options.MaxY, Color.BLACK);
	private final Canvas ouahPad = new Canvas(Options.MaxX, Options.MaxY);
	private GraphicsContext gc = ouahPad.getGraphicsContext2D();
	private Button toggleActive = new Button("Start");
	
	@Override
	public void start(Stage world) throws Exception {
		//testing
		//gc = DisplayArray.initDisplay(gc);
		gc = DisplayArray.swoosh(gc);
		wutGroot.getChildren().add(ouahPad);
		
		//eyes array
		world.setScene(kR);
		world.setTitle("kitt's eyes are on you . . .");
		world.show();
		
		//controls
		HBox dash = new HBox();
		dash.getChildren().add(toggleActive);
		Stage controls = new Stage();
		Scene manual = new Scene(dash);
		controls.setScene(manual);
		controls.show();
		
		toggleActive.setOnAction(new ToggleKitt());
		
		//immediate timer activation (for testing)
		if (Options.testing) { 
			gmt = new Timer();
			gmt.schedule(new BounceTask(), Options.PauseInMS);
			
			AudioStim.playAudioStim(Options.StereoSide.LEFT);
		}
		
	}

	private class ToggleKitt implements EventHandler {
		private boolean running = false;

		@Override
		public void handle(Event arg0) {
			running = !running;
			
			if (running) {
				gmt.schedule(new BounceTask(), Options.PauseInMS);
				toggleActive.setText("Stop");
			} else {
				gmt.cancel();
				toggleActive.setText("Start");
			}
		}
		
	}
	
	private class BounceTask extends TimerTask {
		public void run() {
			DisplayArray.swoosh(gc);
			
			if (DisplayArray.moreRemaining()) {
				//this.remaining--;
				gmt.schedule(new BounceTask(), Options.PauseInMS);
			}
		}
	}
	
}
