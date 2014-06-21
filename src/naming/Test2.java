package naming;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import util.Variables;



public class Test2 {
	public final static Machine namingServer;
	public final static Machine me;
	
	static {
		final String namingServerIp = Variables.getInstance().getProperty("namingServerIp");
		final int namingServerPort = Integer.parseInt(Variables.getInstance().getProperty("namingServerPort"));
		namingServer = new Machine(namingServerIp, namingServerPort);
		final String myIp = Variables.getInstance().getProperty("myIp");
		final int myPort = Integer.parseInt(Variables.getInstance().getProperty("myPort"));
		me = new Machine(myIp, myPort);
	}
	
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		NamingService loadService = (NamingService) Naming.lookup(namingServer.getAddress(NamingService.class.getName()));
		loadService.addMachine(me);
	}
}
