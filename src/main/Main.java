package main;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import rpc.NamingServerService;
import rpc.NamingServerServiceImpl;

public class Main {

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		// TODO Auto-generated method stub
		NamingServerServiceImpl loadService = new NamingServerServiceImpl();
		LocateRegistry.createRegistry(7779);
		Naming.rebind("rmi://127.0.0.1:7779/LoadService", loadService);
		System.out.println("shit");

		
		NamingServerService loadService1 = (NamingServerService) Naming.lookup("rmi://127.0.0.1:7779/LoadService");
		loadService1.call();
		
	}

}
