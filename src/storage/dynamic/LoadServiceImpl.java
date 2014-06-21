package storage.dynamic;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadServiceImpl extends UnicastRemoteObject implements LoadService {

	public LoadServiceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(LoadServiceImpl.class);
	
	@Override
	public void call() throws RemoteException {
		logger.entry();
	}
}
