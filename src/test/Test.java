package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import util.Variables;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		final int interval = Integer.parseInt(Variables.getInstance().getProperty("sendingTime")); 
		Timer timer = new Timer(interval, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("shit");
			}
		});
		timer.start();
		while (true) {
			Thread.sleep(Long.MAX_VALUE);
		}
	}
}
