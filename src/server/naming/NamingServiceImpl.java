package server.naming;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.Machine;

public class NamingServiceImpl extends UnicastRemoteObject implements NamingService {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(NamingServiceImpl.class);
	public NamingServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public void addMachine(Machine machine) throws RemoteException {
		logger.entry(machine);
		NamingServer.getInstance().storages.add(machine);
	}	
}
