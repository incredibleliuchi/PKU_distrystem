package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import datastructure.FileUnit;
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

	@Override
	public Machine createFile(String fullFilePath) throws RemoteException {
		NamingServer namingServer = NamingServer.getInstance();
		int storeIndex = fullFilePath.hashCode() % namingServer.storageValids.size();
		Machine machine = new ArrayList<>(namingServer.storageValids.keySet()).get(storeIndex);
		return machine;
	}

	@Override
	public Machine getFileLocation(String fullFilePath) throws RemoteException {
		NamingServer namingServer = NamingServer.getInstance();
		String[] path = fullFilePath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			ArrayList<FileUnit> fileUnits = nowFileUnit.list();
			for (int j = 0; j < fileUnits.size(); j++) {
				if (i != path.length-1) {
					if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						nowFileUnit = fileUnits.get(j);
						break;
					}
				} else {
					if (!fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						return fileUnits.get(j).getAllMachines().get(0);
					}
				}
				if (j == fileUnits.size()) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public Machine createDir(String fullDirPath) throws RemoteException {
		NamingServer namingServer = NamingServer.getInstance();
		int storeIndex = fullDirPath.hashCode() % namingServer.storageValids.size();
		Machine machine = new ArrayList<>(namingServer.storageValids.keySet()).get(storeIndex);
		return machine;
	}

	@Override
	public Machine deleteDir(String fullDirPath) throws RemoteException {
		NamingServer namingServer = NamingServer.getInstance();
		String[] path = fullDirPath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			ArrayList<FileUnit> fileUnits = nowFileUnit.list();
			for (int j = 0; j < fileUnits.size(); j++) {
				if (i != path.length-1) {
					if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						nowFileUnit = fileUnits.get(j);
						break;
					}
				} else {
					if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						return fileUnits.get(j).getAllMachines().get(0);
					}
				}
				if (j == fileUnits.size()) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public ArrayList<FileUnit> listDir(String fullDirPath)
			throws RemoteException {
		NamingServer namingServer = NamingServer.getInstance();
		String[] path = fullDirPath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			ArrayList<FileUnit> fileUnits = nowFileUnit.list();
			for (int j = 0; j < fileUnits.size(); j++) {
				if (i != path.length-1) {
					if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						nowFileUnit = fileUnits.get(j);
						break;
					}
				} else {
					if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						return fileUnits.get(j).list();
					}
				}
				if (j == fileUnits.size()) {
					return null;
				}
			}
		}
		return null;
	}
		
}
