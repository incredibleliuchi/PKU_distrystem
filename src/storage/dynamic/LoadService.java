package storage.dynamic;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoadService extends Remote {
	public void call() throws RemoteException;
}
