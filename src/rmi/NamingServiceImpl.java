package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import datastructure.FileUnit;
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
			storageValids.put(machine, now);
		}
	}

	@Override
	public Machine createFile(String fullFilePath) throws RemoteException {
		NamingServer namingServer = NamingServer.getInstance();
		int storeIndex = fullFilePath.hashCode() % namingServer.storageValids.size();
		Machine machine = new ArrayList<>(namingServer.storageValids.keySet()).get(storeIndex);
		
		String[] path = fullFilePath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			
			if (i != path.length-1) {
				ArrayList<FileUnit> fileUnits = nowFileUnit.list();
				boolean isCreatedDir = false;
				for (int j = 0; j < fileUnits.size(); j++) {
					if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						nowFileUnit = fileUnits.get(j);
						isCreatedDir = true;
						break;
					}
				}
				if (!isCreatedDir) {
					return null;
				}
			} else {
				return machine;
			}
			
		}
		return null;
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
		
		String[] path = fullDirPath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			
			if (i != path.length-1) {
				ArrayList<FileUnit> fileUnits = nowFileUnit.list();
				boolean isCreatedDir = false;
				for (int j = 0; j < fileUnits.size(); j++) {
					if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						nowFileUnit = fileUnits.get(j);
						isCreatedDir = true;
						break;
					}
				}
				if (!isCreatedDir) {
					return null;
				}
			} else {
				return machine;
			}
			
		}
		return null;
	}

	@Override
	public Machine getDirLocation(String fullDirPath) throws RemoteException {
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
		return nowFileUnit.list();
	}

	private void addToDir(Machine machine, FileUnit srcDir, FileUnit targetDir) {
		ArrayList<FileUnit> srcLowerFileUnits = srcDir.list();
		for (FileUnit toAddFileUnit : srcLowerFileUnits) {
			ArrayList<FileUnit> targetLowerFileUnits = targetDir.list();
			boolean isExist = false;
			FileUnit targetAddedFileUnit = null;
			for (FileUnit existFileUnit : targetLowerFileUnits) {
				if (existFileUnit.getName().equals(toAddFileUnit.getName())) {
					isExist = true;
					ArrayList<Machine> machines = existFileUnit.getAllMachines();
					boolean isRecordThisMachine = false;
					for (Machine recordMachine : machines) {
						if (recordMachine.ip.equals(machine.ip) && recordMachine.port == machine.port) {
							isRecordThisMachine = true;
						}
					}
					if (!isRecordThisMachine) {
						targetAddedFileUnit = existFileUnit;
						existFileUnit.addStorageMachine(machine);
					}
				}
			}
			if (!isExist) {
				targetAddedFileUnit = new FileUnit(toAddFileUnit.getName(), toAddFileUnit.isDir());
				targetAddedFileUnit.addStorageMachine(machine);
				targetDir.addLowerFileUnit(targetAddedFileUnit);
			}
			if (targetAddedFileUnit.isDir()) {
				addToDir(machine, toAddFileUnit, targetAddedFileUnit);
			}
		}
	}
	
	@Override
	public boolean informOnline(Machine machine, FileUnit localRoot)
			throws RemoteException {
		FileUnit root = NamingServer.getInstance().getRoot();
		addToDir(machine, localRoot, root);
		return true;
	}

		
}
