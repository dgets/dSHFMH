package dEMDRC;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HeadsUp extends Application {
	private Timer gmt;
	
	private Group wutGroot = new Group();
	private Scene kR = new Scene(wutGroot, Options.MaxX, Options.MaxY, Color.BLACK);
	private final Canvas ouahPad = new Canvas(Options.MaxX, Options.MaxY);
	private GraphicsContext gc = ouahPad.getGraphicsContext2D();
	private Button toggleActive = new Button("Stop");
	private Button goUserPrefs = new Button("User Prefs");
	private static Slider adjustSpeed = new Slider();
	private static GridPane dash = new GridPane();	//also this needs to be comfortably set below kitt
	
	public static AudioStim blonk = new AudioStim();	//not sure about this being static... audio issues?
	
	//getters/setters
	public GraphicsContext getGC() {
		return gc;
	}
	
	@Override
	public void start(Stage world) throws Exception {
		//testing
		gc = DisplayArray.swoosh(gc);
		wutGroot.getChildren().add(ouahPad);
		
		//eyes array
		world.setScene(kR);
		world.setTitle("kitt's eyes are on you . . .");
		world.show();
		
		//get audio buffer set up
		//blonk = new AudioStim();
		
		//controls
		dash.setVgap(5); dash.setHgap(3);	//a little space between elements
		dash.add(new Label("Display Speed"), 1, 0);	//the shorthand is definitely better
		dash.add(new Label("User Preferences"), 2, 0);
		dash.add(goUserPrefs, 2, 1);
		GridPane.setRowIndex(toggleActive, 1); GridPane.setColumnIndex(toggleActive, 0); //gross longhand
		adjustSpeed.setMax(Options.MaximumPauseInMS);
		adjustSpeed.setMin(Options.MinimumPauseInMS);
		adjustSpeed.setValue(Options.DefaultPauseInMS);
		GridPane.setRowIndex(adjustSpeed, 1); GridPane.setColumnIndex(adjustSpeed, 1);
		dash.getChildren().addAll(toggleActive, adjustSpeed);
		Stage controls = new Stage();
		Scene manual = new Scene(dash);
		controls.setScene(manual);
		controls.setTitle("sooo manipulative . . .");
		controls.show();
		
		toggleActive.setOnAction(new ToggleKitt());
		
		//immediate timer activation (for testing)
		if (Options.testing) { 
			gmt = new Timer();
			gmt.schedule(new BounceTask(), (Options.DefaultPauseInMS / DisplayArray.determineEyesInArray(Options.MaxX)));
			
			//AudioStim.playAudioStim(Options.StereoSide.LEFT);
		}
		
	}
	
	/**
	 * Method is just a wrapper to return adjustSpeed's value without exposing the whole widget
	 * 
	 * @return double Slider value
	 */
	public static double getSliderSpeed() {
		return adjustSpeed.getValue();
	}
	
	/**
	 * Method blocks input from form widgets
	 * 
	 * @author sprite
	 *
	 */
	public static void blockInput() {
		dash.setDisable(true);
		
		//we need to gray out or otherwise indicate that the form is not accepting interaction nao
		ObservableList<Node> kiddies = dash.getChildren();
		//HashMap<Node, double> kidsNOpacities = new HashMap();
		//so yeah that doesn't work I guess we'll assume 100%, etc
		
		for (Node kiddo : kiddies) {
			kiddo.setOpacity(70);
		}
	}
	
	/**
	 * Method restore input from form widgets
	 * 
	 * @author sprite
	 *
	 */
	public static void restoreInput() {
		dash.setDisable(false);
		
		//restore the display to its former glory
		ObservableList<Node> kiddies = dash.getChildren();
		
		for (Node kiddo : kiddies) {
			kiddo.setOpacity(100);
		}
	}

	//weird subclasses
	private class ToggleKitt implements EventHandler {
		private boolean running;
		
		//need the constructor nao
		public ToggleKitt() {
			if (Options.testing) {
				running = true;
			} else {
				running = false;
			}
		}

		@Override
		public void handle(Event arg0) {
			running = !running;
			
			if (running) {
				gmt.schedule(new BounceTask(), (Options.DefaultPauseInMS / DisplayArray.determineEyesInArray(Options.MaxX)));
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
				gmt.schedule(new BounceTask(), (Options.DefaultPauseInMS / DisplayArray.determineEyesInArray(Options.MaxX)));
			}
		}
	}
	
}
