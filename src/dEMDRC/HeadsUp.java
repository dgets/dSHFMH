package dEMDRC;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HeadsUp extends Application {
	
	@Override
	public void start(Stage world) throws Exception {
		
		
		Group wutGroot = new Group();
		Scene kR = new Scene(wutGroot, Options.MaxX, Options.MaxY, Color.BLACK);
		
		final Canvas ouahPad = new Canvas(250, 250);
		GraphicsContext gc = ouahPad.getGraphicsContext2D();
		
		gc = DisplayArray.initDisplay(gc);
		wutGroot.getChildren().add(ouahPad);
		
		world.setScene(kR);
		world.setTitle("kitt's eyes are on you . . .");
		world.show();
	}

}
