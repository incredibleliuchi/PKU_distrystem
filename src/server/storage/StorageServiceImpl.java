package server.storage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import server.naming.NamingServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StorageServiceImpl extends UnicastRemoteObject implements StorageService {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(StorageServiceImpl.class);
	public StorageServiceImpl() throws RemoteException {
		super();
	}
	
}
