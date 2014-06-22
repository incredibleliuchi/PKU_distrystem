package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.Machine;

public interface NamingService extends Remote {
	public void updateMachine(Machine machine) throws RemoteException;
	
}
