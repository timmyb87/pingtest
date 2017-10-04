package ping.test;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author tbandola
 */
public class PingTest extends Application {
	private static FXMLLoader mainLoader = new FXMLLoader();
	
	@Override
	public void start(Stage stage) throws IOException{
		stage.setTitle("Ping Test");
		Scene scene = new Scene(mainLoader.load(getClass().getResource("MainGUI.fxml")));
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		TestingThread thread = MainGUIController.getTestThread();
		if(thread != null){ thread.interrupt(); }
		super.stop();
	}
	
	public static FXMLLoader getMainLoader(){ return mainLoader; }

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
