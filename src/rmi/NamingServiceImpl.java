package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import datastructure.StorageMeta;
import server.Machine;
import server.naming.NamingServer;

public class NamingServiceImpl extends UnicastRemoteObject implements NamingService {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(NamingServiceImpl.class);
	public NamingServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public void updateMachine(Machine machine) throws RemoteException {
		logger.entry(machine);
		final Map<Machine, Long> storageValids = NamingServer.getInstance().storageValids;
		final long now = new Date().getTime();
		if ( storageValids.containsKey(machine) ) {
			final long date = storageValids.get(machine);
			if ( date < now ) storageValids.put(machine, now);
		} else {
			// TODO: add a new storage server, immigrate data. 
			storageValids.put(machine, now);
			final Map<Machine, StorageMeta> storageMetas = NamingServer.getInstance().storageMetas; 
			storageMetas.put(machine, new StorageMeta());
		}
	}	
}
