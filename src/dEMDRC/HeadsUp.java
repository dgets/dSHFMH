package dEMDRC;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

public class HeadsUp extends Application {
	private Timer gmt;
	private Group wutGroot = new Group();
	private Scene kR = new Scene(wutGroot, Options.MaxX, Options.MaxY, Color.BLACK);
	
	private final Canvas ouahPad = new Canvas(Options.MaxX, Options.MaxY);
	private GraphicsContext gc = ouahPad.getGraphicsContext2D();
	
	@Override
	public void start(Stage world) throws Exception {
		//testing
		//gc = DisplayArray.initDisplay(gc);
		gc = DisplayArray.swoosh(gc);
		wutGroot.getChildren().add(ouahPad);
		
		world.setScene(kR);
		world.setTitle("kitt's eyes are on you . . .");
		world.show();
		
		gmt = new Timer();
		gmt.schedule(new BounceTask(), Options.PauseInMS);
	}

	/*private GraphicsContext bounce(GraphicsContext gc) {
		DisplayArray.swoosh(gc);
	}*/
	
	private class BounceTask extends TimerTask {
		//private int remaining = 30;	//just testing shit, later based on Options.Duration*
		
		public void run() {
			DisplayArray.swoosh(gc);
			
			if (DisplayArray.moreRemaining()) {
				//this.remaining--;
				gmt.schedule(new BounceTask(), Options.PauseInMS);
			}
		}
	}
	
}
