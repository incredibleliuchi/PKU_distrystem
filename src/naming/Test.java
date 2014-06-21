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

import storage.dynamic.LoadServiceImpl;


public class Test {
	Set<Machine> storages = new HashSet<>();
	
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		LoadServiceImpl loadService = new LoadServiceImpl();
		LocateRegistry.createRegistry(7779);
		Naming.rebind("rmi://127.0.0.1:7779/LoadService", loadService);
		System.out.println("shit");
	}
}
