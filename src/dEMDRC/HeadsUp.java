package dEMDRC;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HeadsUp extends Application {
	
	@Override
	public void start(Stage arg0) throws Exception {
		
		
		Group wutGroot = new Group();
		Scene kR = new Scene(wutGroot, Options.MaxX, Options.MaxY, Color.BLACK);
		
		final Canvas ouahPad = new Canvas(250, 250);
		GraphicsContext gc = ouahPad.getGraphicsContext2D();
	}

}
