package naming;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NamingService extends Remote {
	public void addMachine(Machine machine) throws RemoteException;
}
