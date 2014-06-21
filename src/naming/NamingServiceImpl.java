package naming;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NamingServiceImpl extends UnicastRemoteObject implements NamingService {
	private static final long serialVersionUID = -4276686317588928546L;
	private static final Logger logger = LogManager.getLogger(NamingServiceImpl.class);
	public NamingServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public void addMachine(Machine machine) throws RemoteException {
		logger.entry(machine);
	}	
}
