package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

import javax.swing.Timer;

import rmi.NamingService;
import server.storage.StorageServer;
import util.Variables;

public class Test {
	public static void main(String[] args) {
		System.out.println();
	}
	
	public static void testTimer() {
		final int interval = Integer.parseInt(Variables.getInstance().getProperty("sendingTime")); 
		final Timer timer = new Timer(interval, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					StorageServer server = StorageServer.getInstance();
					NamingService loadService = (NamingService) Naming.lookup(server.namingServer.getAddress(NamingService.class.getName()));
					loadService.updateMachine(server.me);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		timer.start();
		while (true) {
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
