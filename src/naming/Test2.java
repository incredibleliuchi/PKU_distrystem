package naming;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import storage.dynamic.LoadService;



public class Test2 {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		LoadService loadService = (LoadService) Naming.lookup("rmi://127.0.0.1:7779/LoadService");
		loadService.call();
	}
}
