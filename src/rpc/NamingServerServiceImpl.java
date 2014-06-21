package rpc;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NamingServerServiceImpl extends UnicastRemoteObject implements NamingServerService {

	public NamingServerServiceImpl() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;
	
	@Override
	public void call() throws RemoteException {
		System.out.println("callllllll");
	}
}
