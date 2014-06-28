package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.rmi.Naming;

import javax.swing.Timer;

import rmi.NamingService;
import server.storage.StorageServer;
import util.Variables;

public class Test {
	public static void main(String[] args) throws Exception {
//		System.out.println();
//		testRandom();
//		byte[] bs = new byte [] {1, 2, 3};
//		byte[] c = Arrays.copyOf(bs, 0);
//		System.out.println(c.length);
		System.out.println(args[0]);
	}
	
	public static void testRandom() throws Exception {
		File file = new File("README.md");
//		System.out.println(file.length());
		RandomAccessFile rf = new RandomAccessFile(file, "r");
		rf.seek(31L);
		byte[] data = new byte[100];
		int r = rf.read(data);
		System.out.println(r);
		System.out.println(new String(data, "utf8"));
		System.out.println("finish");
		rf.close();
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
