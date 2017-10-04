package ping.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class TestUnit {
	private String address;
	private int tries, successes;
	
	@FXML private Pane pane;
	@FXML private Button closeButton;
	@FXML private ProgressBar bar = new ProgressBar();
	
	public TestUnit() throws IOException{
		tries = 0;
		successes = 0;
		bar = new ProgressBar();
		bar.setProgress(0);
	}
	
	public TestUnit(String a) throws IOException{
		this();
		address = a;
		pane = PingTest.getMainLoader().load(getClass().getResource("testUnitDisplay.fxml"));
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				Label l = (Label)pane.lookup("#addressLabel");
				l.setText(a);
			}
		});
	}
	
	public boolean test() throws UnknownHostException, IOException{
		tries++;
		boolean b = InetAddress.getByName(address).isReachable(4000);
		if(b){
			successes++;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				setProgressBar((double)successes/tries);
				setTriesLabel(tries);
				setSuccessesLabel(successes);
			}
		});
		return b;
	}
	
	@FXML private void closeUnit(){
		((HBox)pane.getParent()).getChildren().remove(pane);
		Label l = (Label)pane.lookup("#addressLabel");
		MainGUIController.removeUnit(l.getText());
	}
	
	public void reset(){
		tries = 0;
		successes = 0;
		setProgressBar(0);
		setTriesLabel(0);
		setSuccessesLabel(0);
	}
	
	private void setProgressBar(double d){
		((ProgressBar)pane.lookup("#progressBar")).setProgress(d);
	}
	
	private void setTriesLabel(int t){
		((Label)pane.lookup("#triesLabel")).setText(""+t);
	}
	
	private void setSuccessesLabel(int s){
		((Label)pane.lookup("#successesLabel")).setText(""+s);
	}
	
	public Pane getPane(){ return pane; }
	
	public String getAddress(){ return address; }
	
	public int getTries(){ return tries; }
	
	public int getSuccesses(){ return successes; }
}