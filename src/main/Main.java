package main;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import server.naming.NamingService;
import server.naming.NamingServiceImpl;

public class Main {

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		NamingService loadService = new NamingServiceImpl();
		LocateRegistry.createRegistry(7779);
		Naming.rebind("rmi://127.0.0.1:7779/LoadService", loadService);
		System.out.println("shit");

		
		NamingService loadService1 = (NamingService) Naming.lookup("rmi://127.0.0.1:7779/LoadService");
		loadService1.addMachine(null);
		
	}

}
