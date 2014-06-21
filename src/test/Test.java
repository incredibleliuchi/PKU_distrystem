package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		Timer timer = new Timer(1000, new ActionListener() {
			
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
