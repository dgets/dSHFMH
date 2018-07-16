package dEMDRC;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Timer;
import java.util.TimerTask;

import dEMDRC.Options.UserSet;

public class HeadsUp extends Application {
	private Timer gmt;
	
	private Group wutGroot = new Group();
	private Scene kR = new Scene(wutGroot, Options.MaxX, Options.MaxY, Color.BLACK);
	private final Canvas ouahPad = new Canvas(Options.MaxX, Options.MaxY);
	private GraphicsContext gc = ouahPad.getGraphicsContext2D();
	private Button toggleActive = new Button("Start");
	private Button goUserPrefs = new Button("User Prefs");
	private static Slider adjustSpeed = new Slider();
	private static GridPane dash = new GridPane();	//also this needs to be comfortably set below kitt
	private static Region veil = new Region();
	
	public static AudioStim blonk = new AudioStim();	//not sure about this being static... audio issues?
	private static Options nang = new Options();	//yes, this & next ARE used (in UserPrefs.handle())
	//public Options.UserSet userSettings = nang.new UserSet(); 
	public static UserPrefs userPrefsDisplay;
	
	//getters/setters
	public GraphicsContext getGC() {
		return gc;
	}
	
	/**
	 * I guess this would be the equivalent of our 'main()' while we're working with javafx
	 * 
	 */
	@Override
	public void start(Stage world) throws Exception {
		//testing
		gc = DisplayArray.swoosh(gc);
		wutGroot.getChildren().add(ouahPad);
		
		//eyes array
		world.setScene(kR);
		world.setTitle("kitt's eyes are on you . . .");
		world.show();
		
		//controls
		dash.setVgap(5); dash.setHgap(3);	//a little space between elements
		veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3)");
		veil.setVisible(false);
		//dash.getColumnConstraints().add(new ColumnConstraints(150));
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
		controls.setX(world.getHeight() + world.getX());
		controls.setY(world.getY() + (world.getHeight() * 1.2));
		controls.setScene(manual);
		controls.setTitle("sooo manipulative . . .");
		controls.show();
		
		toggleActive.setOnAction(new ToggleKitt());
		goUserPrefs.setOnAction(userPrefsDisplay = new UserPrefs());
		//userSettings.initStructs();
		
		gmt = new Timer();
		
		//immediate timer activation (for testing)
		if (Options.testing) { 
			toggleActive.setText("Stop");
			scheduleBounce();
		}
		
		//any init for other objects
		UserPrefs.setWorldXY(world.getX(), world.getY());	//not working
		
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
		//we need to gray out or otherwise indicate that the form is not accepting interaction nao
		dash.setDisable(true);
		veil.setVisible(true);
	}
	
	/**
	 * Method restore input from form widgets
	 * 
	 * @author sprite
	 *
	 */
	public static void restoreInput() {
		//restore the display to its former glory
		dash.setDisable(false);
		veil.setVisible(false);
	}
	
	/**
	 * Just a wrapper for scheduling a new bounce task so that we're not duplicating so much code
	 */
	private void scheduleBounce() {
		gmt.schedule(new BounceTask(), (Options.DefaultPauseInMS / DisplayArray.determineEyesInArray(Options.MaxX)));
	}

	//weird subclasses
	private class ToggleKitt implements EventHandler<ActionEvent> {
		public boolean running;
		
		//need the constructor nao
		public ToggleKitt() {
			if (Options.testing) {
				running = true;
			} else {
				running = false;
			}
		}

		//default button handler
		@Override
		public void handle(ActionEvent arg0) {
			toggle();
		}
		
		//getter/setter (fucking ouah I hate the static dichotomy, need to review that shit in my books)
		/*public void setRunning(boolean run) {
			running = run;
		}*/
		
		//for all of those invocations
		public void toggle() {
			running = !running;
			
			if (running) {
				scheduleBounce();
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
			} else {
				//we need to change the controls (start/stop) status nao
				toggleActive.setText("Start");
			}
		}
	}
	
}
