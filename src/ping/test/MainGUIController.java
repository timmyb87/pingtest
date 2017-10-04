package ping.test;

import java.io.IOException;
import java.lang.Thread.State;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author tbandola
 */
public class MainGUIController implements Initializable {
	private static TestingThread testThread;
	private static ArrayList<TestUnit> tests;
	private long length = 0;
	private final String[] defaultAddresses = {"127.0.0.1","www.google.com","www.yahoo.com"};
	
	@FXML private Button run;
	@FXML private Button reset;
	@FXML private Button stop;
	@FXML private Button addAddress;
	@FXML private Button addDefaults;
	
	@FXML private TextField testLength;
	@FXML private TextField address;
	
	@FXML private Label statusLabel;
	
	@FXML private HBox scrollPaneContent;
	@FXML private HBox statusBar;
	
	@FXML private BorderPane bp = new BorderPane();
	
	@FXML private AnchorPane ap = new AnchorPane();
	
	private Integer getTestLengthValue() {
		if(testLength.getText().compareTo("") != 0){
			return Integer.valueOf(testLength.getText());
		}
		else{
			return 0;
		}
	}
	
	private void setStatusIdle(){
		statusLabel.setText("Idle");
	}
	
	private void setStatusRunning(){
		statusLabel.setText("Running");
	}
	
	@FXML private void runTest(ActionEvent event) throws UnknownHostException, IOException, InterruptedException{
		if(testThread == null || testThread.getState() == State.TERMINATED){
			testThread = new TestingThread(getTestLengthValue());
		}
		testThread.start();
		setStatusRunning();
	}
	
	@FXML private void resetTest(){
		stopTest();
		for(TestUnit t : tests){
			t.reset();
		}
	}
	
	@FXML private void stopTest(){
		if(testThread != null) { testThread.interrupt(); }
		setStatusIdle();
	}
	
	@FXML private void addTestEvent() throws IOException{
		addTest(address.getText());
	}
	
	@FXML private void addDefaults() throws IOException{
		for(String a : defaultAddresses){
			addTest(a);
		}
	}
	
	@FXML private void closeAction(){
		if(testThread != null){
			testThread.interrupt();
		}
		Platform.exit();
	}
	
	private void addTest(String a) throws IOException{
		boolean add = true;
		//check if address already added
		for(TestUnit t : tests){
			if(t.getAddress().equals(a)){
				add = false;
				break;
			}
		}
		//if not, add
		if(add){
			TestUnit unit = new TestUnit(a);
			tests.add(unit);
			scrollPaneContent.getChildren().add(unit.getPane());
			address.setText("");
		}
	}
	
	public static void removeUnit(String a){
		for(TestUnit t : tests){
			if(t.getAddress().equals(a)){
				tests.remove(t);
				break;
			}
		}
	}
	
	public static ArrayList<TestUnit> getTestUnits(){ return tests; }
	
	public static TestingThread getTestThread(){ return testThread; }
	
	@Override public void initialize(URL url, ResourceBundle rb) {
		tests = new ArrayList();
		setStatusIdle();
	}
}