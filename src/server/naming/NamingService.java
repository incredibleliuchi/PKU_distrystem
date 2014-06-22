package server.naming;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.Machine;

public interface NamingService extends Remote {
	public void addMachine(Machine machine) throws RemoteException;
	
	
}
