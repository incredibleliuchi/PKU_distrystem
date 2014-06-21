package naming;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Variables;


public class Test {
	Set<Machine> storages = new HashSet<>();

	public final static Machine me;
	
	static {
		final String myIp = Variables.getInstance().getProperty("namingServerIp");
		final int myPort = Integer.parseInt(Variables.getInstance().getProperty("namingServerPort"));
		me = new Machine(myIp, myPort);
	}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		NamingServiceImpl loadService = new NamingServiceImpl();
		LocateRegistry.createRegistry(me.port);
		Naming.rebind(me.getAddress(NamingService.class.getName()), loadService);
		System.out.println("shit");
	}
}
