package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import datastructure.FileUnit;
import datastructure.SynchroMeta;
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
		logger.entry(fullFilePath);
		NamingServer namingServer = NamingServer.getInstance();
		int storeIndex = fullFilePath.hashCode() % namingServer.storageValids.size();
		storeIndex = (storeIndex + namingServer.storageValids.size()) % namingServer.storageValids.size();
		Machine machine = new ArrayList<>(namingServer.storageValids.keySet()).get(storeIndex);
		
		String[] path = fullFilePath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			
			if (i != path.length-1) {
				List<FileUnit> fileUnits = nowFileUnit.list();
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
				List<FileUnit> fileUnits = nowFileUnit.list();
				boolean isCreatedFile = false;
				for (int j = 0; j < fileUnits.size(); j++) {
					if (!fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						logger.info("isCreatedFile:" + fileUnits.get(j).getName());
						isCreatedFile = true;
						break;
					}
				}
				if (isCreatedFile) {
					return null;
				}
				return machine;
			}
			
		}
		return null;
	}

	@Override
	public Machine getFileLocation(String fullFilePath) throws RemoteException {
		logger.entry(fullFilePath);
		NamingServer namingServer = NamingServer.getInstance();
		String[] path = fullFilePath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			List<FileUnit> fileUnits = nowFileUnit.list();
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
				if (j == fileUnits.size()-1) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public Machine createDir(String fullDirPath) throws RemoteException {
		logger.entry(fullDirPath);
		NamingServer namingServer = NamingServer.getInstance();
		int storeIndex = fullDirPath.hashCode() % namingServer.storageValids.size();
		storeIndex = (storeIndex + namingServer.storageValids.size()) % namingServer.storageValids.size();
		Machine machine = new ArrayList<>(namingServer.storageValids.keySet()).get(storeIndex);
		
		String[] path = fullDirPath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			
			if (i != path.length-1) {
				List<FileUnit> fileUnits = nowFileUnit.list();
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
				List<FileUnit> fileUnits = nowFileUnit.list();
				boolean isCreatedDir = false;
				for (int j = 0; j < fileUnits.size(); j++) {
					if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
						isCreatedDir = true;
						break;
					}
				}
				if (isCreatedDir) {
					return null;
				}
				return machine;
			}
			
		}
		return null;
	}

	@Override
	public Machine getDirLocation(String fullDirPath) throws RemoteException {
		logger.entry(fullDirPath);
		NamingServer namingServer = NamingServer.getInstance();
		String[] path = fullDirPath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		for (int i = 0; i < path.length; i++) {
			List<FileUnit> fileUnits = nowFileUnit.list();
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
				if (j == fileUnits.size()-1) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public List<FileUnit> listDir(String fullDirPath)
			throws RemoteException {
		logger.entry(fullDirPath);
		NamingServer namingServer = NamingServer.getInstance();
		String[] path = fullDirPath.split("/");
		FileUnit nowFileUnit = namingServer.getRoot();
		if (fullDirPath.equals("")) {
			return nowFileUnit.list();
		}
		for (int i = 0; i < path.length; i++) {
			List<FileUnit> fileUnits = nowFileUnit.list();
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
				if (j == fileUnits.size()-1) {
					return null;
				}
			}
		}
		return nowFileUnit.list();
	}

	private List<SynchroMeta> addToDir(Machine machine, FileUnit srcDir, FileUnit targetDir, String lastDirName) {
		
		List<SynchroMeta> synchroMetas = new ArrayList<>();
		
		List<FileUnit> srcLowerFileUnits = srcDir.list();
		for (FileUnit toAddFileUnit : srcLowerFileUnits) {
			List<FileUnit> targetLowerFileUnits = targetDir.list();
			boolean isExist = false;
			boolean isTypeEqual = true;
			FileUnit targetAddedFileUnit = null;
			for (FileUnit existFileUnit : targetLowerFileUnits) {
				if (existFileUnit.getName().equals(toAddFileUnit.getName())) {
					isExist = true;
					if (existFileUnit.isDir() != toAddFileUnit.isDir()) {
						isTypeEqual = false;
					} else {
						List<Machine> machines = existFileUnit.getAllMachines();
						boolean isRecordThisMachine = false;
						for (Machine recordMachine : machines) {
							if (recordMachine.ip.equals(machine.ip) && recordMachine.port == machine.port) {
								isRecordThisMachine = true;
								break;
							}
						}
						if (!isRecordThisMachine) {
							if (!existFileUnit.isDir()) {
								Machine targetMachine = existFileUnit.getAllMachines().get(0);
								synchroMetas.add(new SynchroMeta(lastDirName+"/"+toAddFileUnit.getName(), SynchroMeta.CONFIRM, new Machine(targetMachine.ip, targetMachine.port)));
							}
							targetAddedFileUnit = existFileUnit;
							existFileUnit.addStorageMachine(machine);
						}
					}
				}
			}
			if (isExist) {
				if (!isTypeEqual) {
					synchroMetas.add(new SynchroMeta(lastDirName+"/"+toAddFileUnit.getName(), SynchroMeta.DELETE, null));
					continue;
				}
			} else {
				targetAddedFileUnit = new FileUnit(toAddFileUnit.getName(), toAddFileUnit.isDir());
				targetAddedFileUnit.addStorageMachine(machine);
				targetDir.addLowerFileUnit(targetAddedFileUnit);
			}
			if (targetAddedFileUnit.isDir()) {
				List<SynchroMeta> lowerSynchroMetas = addToDir(machine, toAddFileUnit, targetAddedFileUnit, lastDirName+"/"+targetAddedFileUnit.getName());
				synchroMetas.addAll(lowerSynchroMetas);
			}
		}
		
		return synchroMetas;
	}
	
	@Override
	public List<SynchroMeta> informOnline(Machine machine, FileUnit localRoot)
			throws RemoteException {
		logger.entry(machine, localRoot);
		FileUnit root = NamingServer.getInstance().getRoot();
		List<SynchroMeta> result = addToDir(machine, localRoot, root, "");
		return result;
	}

	private FileUnit findChangedFileUnitFather(String fullFilePath) {
		FileUnit nowFileUnit = NamingServer.getInstance().getRoot();
		String[] path = fullFilePath.split("/");
		for (int i = 0; i < path.length-1; i++) {
			List<FileUnit> fileUnits = nowFileUnit.list();
			for (int j = 0; j < fileUnits.size(); j++) {
				if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
					nowFileUnit = fileUnits.get(j);
					break;
				}
			}
		}
		return nowFileUnit;
	}
	
	@Override
	public List<Machine> notifyCreateFile(String fullFilePath,
			boolean isOrigin, Machine operateMachine) throws RemoteException {
		logger.entry(fullFilePath, isOrigin, operateMachine);
		FileUnit fatheFileUnit = NamingServer.getInstance().getRoot();
		String[] path = fullFilePath.split("/");
		for (int i = 0; i < path.length-1; i++) {
			List<FileUnit> fileUnits = fatheFileUnit.list();
			for (int j = 0; j < fileUnits.size(); j++) {
				if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(path[i])) {
					fatheFileUnit = fileUnits.get(j);
					fatheFileUnit.addStorageMachine(operateMachine);
					break;
				}
			}
		}

		String name = path[path.length-1];

		List<FileUnit> fileUnits = fatheFileUnit.list();
		FileUnit changedFileUnit = null;
		for (int j = 0; j < fileUnits.size(); j++) {
			if (!fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(name)) {
				changedFileUnit = fileUnits.get(j);
				break;
			}
		}
		if (changedFileUnit == null) {
			changedFileUnit = new FileUnit(name, false);
			fatheFileUnit.addLowerFileUnit(changedFileUnit);
		}
		changedFileUnit.addStorageMachine(operateMachine);
		
		if (isOrigin) {
			NamingServer namingServer = NamingServer.getInstance();
			int storeIndex = fullFilePath.hashCode() % namingServer.storageValids.size();
			storeIndex = (storeIndex + namingServer.storageValids.size()) % namingServer.storageValids.size();
			int backNum = 3;
			if (namingServer.storageValids.size() < backNum) {
				backNum = namingServer.storageValids.size() - 1;
			}
			List<Machine> result = new ArrayList<>();
			while (backNum > 0) {
				Machine machine = new ArrayList<>(namingServer.storageValids.keySet()).get(storeIndex);
				if (!machine.ip.equals(operateMachine) || machine.port != operateMachine.port) {
					result.add(new Machine(machine.ip, machine.port));
					backNum--;
				}
				storeIndex = (storeIndex + 1) % namingServer.storageValids.size();
			}
			return result;
		}
		return null;
	}

	@Override
	public List<Machine> notifyDeleteFile(String fullFilePath,
			boolean isOrigin, Machine operateMachine) throws RemoteException {
		logger.entry(fullFilePath, isOrigin, operateMachine);
		
		FileUnit fatheFileUnit = findChangedFileUnitFather(fullFilePath);
		String[] path = fullFilePath.split("/");
		String name = path[path.length-1];

		List<FileUnit> fileUnits = fatheFileUnit.list();
		FileUnit changedFileUnit = null;
		for (int j = 0; j < fileUnits.size(); j++) {
			if (!fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(name)) {
				changedFileUnit = fileUnits.get(j);
				break;
			}
		}
		if (changedFileUnit == null) {
			return null;
		}
		changedFileUnit.deleteStorageMachine(operateMachine);
		if (changedFileUnit.getAllMachines().size() == 0) {
			fatheFileUnit.deleteLowerFileUnit(changedFileUnit);
		}
		
		if (isOrigin) {
			List<Machine> result = changedFileUnit.getAllMachines();
			return result;
		}
		return null;
	}

	@Override
	public List<Machine> notifyWriteFile(String fullFilePath, boolean isOrigin) throws RemoteException {
		logger.entry(fullFilePath, isOrigin);
		if (isOrigin) {
			FileUnit fatheFileUnit = findChangedFileUnitFather(fullFilePath);
			String[] path = fullFilePath.split("/");
			String name = path[path.length-1];

			List<FileUnit> fileUnits = fatheFileUnit.list();
			FileUnit changedFileUnit = null;
			for (int j = 0; j < fileUnits.size(); j++) {
				if (!fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(name)) {
					changedFileUnit = fileUnits.get(j);
					break;
				}
			}
			if (changedFileUnit == null) {
				return null;
			}

			return changedFileUnit.getAllMachines();
		}
		return null;
	}

	@Override
	public List<Machine> notifyCreateDir(String fullDirPath,
			boolean isOrigin, Machine operateMachine) throws RemoteException {
		logger.entry(fullDirPath, isOrigin, operateMachine);
		FileUnit fatheFileUnit = findChangedFileUnitFather(fullDirPath);
		String[] path = fullDirPath.split("/");
		String name = path[path.length-1];

		List<FileUnit> fileUnits = fatheFileUnit.list();
		FileUnit changedFileUnit = null;
		for (int j = 0; j < fileUnits.size(); j++) {
			if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(name)) {
				changedFileUnit = fileUnits.get(j);
				break;
			}
		}
		if (changedFileUnit == null) {
			changedFileUnit = new FileUnit(name, true);
			fatheFileUnit.addLowerFileUnit(changedFileUnit);
		}
		changedFileUnit.addStorageMachine(operateMachine);
		
		if (isOrigin) {
			NamingServer namingServer = NamingServer.getInstance();
			int storeIndex = fullDirPath.hashCode() % namingServer.storageValids.size();
			storeIndex = (storeIndex + namingServer.storageValids.size()) % namingServer.storageValids.size();
			int backNum = 2;
			if (namingServer.storageValids.size() < backNum) {
				backNum = namingServer.storageValids.size() - 1;
			}
			List<Machine> result = new ArrayList<>();
			while (backNum > 0) {
				Machine machine = new ArrayList<>(namingServer.storageValids.keySet()).get(storeIndex);
				if (!machine.ip.equals(operateMachine) || machine.port != operateMachine.port) {
					result.add(new Machine(machine.ip, machine.port));
					backNum--;
				}
				storeIndex = (storeIndex + 1) % namingServer.storageValids.size();
			}
			return result;
		}
		return null;
	}

	@Override
	public List<Machine> notifyDeleteDir(String fullDirPath,
			boolean isOrigin, Machine operateMachine) throws RemoteException {
		logger.entry(fullDirPath, isOrigin, operateMachine);
		FileUnit fatheFileUnit = findChangedFileUnitFather(fullDirPath);
		String[] path = fullDirPath.split("/");
		String name = path[path.length-1];

		List<FileUnit> fileUnits = fatheFileUnit.list();
		FileUnit changedFileUnit = null;
		for (int j = 0; j < fileUnits.size(); j++) {
			if (fileUnits.get(j).isDir() && fileUnits.get(j).getName().equals(name)) {
				changedFileUnit = fileUnits.get(j);
				break;
			}
		}
		if (changedFileUnit == null) {
			return null;
		}
		changedFileUnit.deleteStorageMachine(operateMachine);
		if (changedFileUnit.getAllMachines().size() == 0) {
			fatheFileUnit.deleteLowerFileUnit(changedFileUnit);
		}
		
		if (isOrigin) {
			List<Machine> result = changedFileUnit.getAllMachines();
			return result;
		}
		return null;
	}

		
}
