package ping.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestingThread extends Thread {
	private boolean run = true;
	private BufferedWriter bw;
	private PrintWriter pw;
	private int testLength;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private Timer timer;
	
	
	TestingThread() throws IOException{
		timer = new Timer();
		bw = new BufferedWriter(new FileWriter("log.txt", false));
		pw = new PrintWriter(bw);
	}
	
	TestingThread(int time) throws IOException{
		this();
		testLength = time;
	}
	
	@Override
	public void interrupt() {
		run = false;
		timer.cancel();
		//outputToLog();
	}
	
	private void outputToLog(){
		pw.println("Summary:");
		for(TestUnit u : MainGUIController.getTestUnits()){
			if(u.getTries() > 0){
				pw.println("Test to "+u.getAddress());
				pw.println("Tries: "+u.getTries());
				pw.println("Successes: "+u.getSuccesses());
				pw.println("Percent of Successes: "+((double)u.getSuccesses()/u.getTries()*100+"%"));
				pw.println();
			}
		}
		pw.close();
	}
	
	private void test() throws UnknownHostException, IOException, InterruptedException{
		for(TestUnit u : MainGUIController.getTestUnits()){
			if(!run){
				break;
			}
			if(!u.test()){
				pw.println("Ping attempt to "
						+u.getAddress() 
						+ " failed " +(dateFormat.format(new Date())));
				pw.println();
			}
			Thread.sleep(1000);
		}
	}
	
	@Override
	public void run() {
		if(testLength != 0){
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					run = false;
					timer.cancel();
				}
			}, 1000*testLength);
		}
		
		while(run){
			try{
				test();
			}
			catch(UnknownHostException e){}
			catch(IOException e){}
			catch(InterruptedException e){}
		}
		outputToLog();
	}
}