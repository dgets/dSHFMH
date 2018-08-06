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

public class HeadsUp extends Application {
	public static Timer gmt;
	
	private Group wutGroot = new Group();
	private Scene kR = new Scene(wutGroot, Options.MaxX, Options.MaxY, Color.BLACK);
	private final Canvas ouahPad = new Canvas(Options.MaxX, Options.MaxY);
	private GraphicsContext gc = ouahPad.getGraphicsContext2D();
	private Button toggleActive = new Button("Start");
	private Button goUserPrefs = new Button("User Prefs");
	private static Slider adjustSpeed = new Slider();
	private static GridPane dash = new GridPane();	//also this needs to be comfortably set below kitt
	private static Region veil = new Region();
	
	public static Options opts = new Options();
	public static Options.UserSet uSet = opts.new UserSet();
	public static AudioStim blonk;	//not sure about this being static... audio issues?
	public static UserPrefsHandler userPrefsDisplay;
	
	/**
	 * I guess this would be the equivalent of our 'main()' while we're working with javafx
	 * 
	 */
	@Override
	public void start(Stage world) throws Exception {
		if (opts.debuggingTest()) { 
			gc = DisplayArray.swoosh(gc);
			wutGroot.getChildren().add(ouahPad);
		}
		
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
		
		if (opts.debuggingGen()) {
			System.out.println("uSet.initStructs() is on deck");
		}
		uSet.initStructs();
		//uSet.loadXMLSettings();
		
		toggleActive.setOnAction(new ToggleKitt());
		userPrefsDisplay = new UserPrefsHandler();
		userPrefsDisplay.init();
		goUserPrefs.setOnAction(userPrefsDisplay /*= new UserPrefsHandler()*/);	//needs to be after XML is loaded (above block?)
		
		gmt = new Timer();
		
		blonk = new AudioStim();
		
		//immediate timer activation (for testing)
		if (opts.debuggingTest()) { 
			toggleActive.setText("Stop");
			scheduleBounce();
		}
		
		//any init for other objects
		userPrefsDisplay.setWorldXY(world.getX(), world.getY());	//working?
	}
	
	public static void togglePause() {
		if (DisplayArray.paused) {
			HeadsUp.gmt.notify();
		} else {
			try {
				HeadsUp.gmt.wait();
			} catch (InterruptedException ex) {
				System.err.println("Issue asking gmt scheduler to wait!\nMsg: " + ex.getMessage());
			} catch (Exception ex) {
				System.err.println("Unknown error asking gmt scheduler to wait!\nMsg: " + ex.getMessage());
			}
		}
		
		DisplayArray.paused = !DisplayArray.paused;
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
	public void scheduleBounce() {
		gmt.schedule(new BounceTask(), (Options.DefaultPauseInMS / DisplayArray.determineEyesInArray(Options.MaxX)));
	}

	//weird subclasses
	private class ToggleKitt implements EventHandler<ActionEvent> {
		public boolean running;
		
		//need the constructor nao
		public ToggleKitt() {
			if (opts.debuggingTest()) {
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
		
		/**
		 * Method toggles the status of the running flag, starts or cancels the timer scheduling depending on what running state
		 * we're going into, and toggles the text of the toggleActive button
		 */
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
	
	public class BounceTask extends TimerTask {
		public void run() {
			DisplayArray.swoosh(gc);
			
			if (DisplayArray.moreRemaining()) {
				gmt.schedule(new BounceTask(), (Options.DefaultPauseInMS / DisplayArray.determineEyesInArray(Options.MaxX)));
			} else {
				//we need to change the controls (start/stop) status nao
				toggleActive.setText("Start");
			}
		}
	}
	
}
