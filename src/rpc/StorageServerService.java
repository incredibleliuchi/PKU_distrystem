package rpc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StorageServerService extends Remote {
	public void call() throws RemoteException;
}
