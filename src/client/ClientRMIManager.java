package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rmi.NamingService;
import rmi.StorageService;
import server.Machine;
import datastructure.FileUnit;


public class ClientRMIManager {
	private static final Logger logger = LogManager.getLogger(ClientRMIManager.class);

	private NamingService namingService;
	private StorageService storageService;
	
	public ClientRMIManager() {
		Machine namingServer = new Machine("namingServer");
		try {
			namingService = (NamingService) Naming.lookup(namingServer.getAddress(NamingService.class.getName()));
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	public Machine getCreateFileLocation(String fullFilePath) {
		logger.entry(fullFilePath);
		Machine machine = null;
		try {
			machine = namingService.createFile(fullFilePath);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return machine;
	}
	
	public Machine getFileLocation(String fullFilePath) {
		logger.entry(fullFilePath);
		Machine machine = null;
		try {
			machine = namingService.getFileLocation(fullFilePath);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return machine;
	}

	public Machine getCreateDirLocation(String fullDirPath) {
		logger.entry(fullDirPath);
		Machine machine = null;
		try {
			machine = namingService.createDir(fullDirPath);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return machine;
	}
	
	public Machine getDirLocation(String fullDirPath) {
		logger.entry(fullDirPath);
		Machine machine = null;
		try {
			machine = namingService.getDirLocation(fullDirPath);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return machine;
	}
	
	public List<FileUnit> listDir(String fullDirPath) {
		logger.entry(fullDirPath);
		List<FileUnit> fileUnits = null;
		try {
			fileUnits = namingService.listDir(fullDirPath);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return fileUnits;
	}

	public boolean connectToStorageServer(Machine machine) {
		logger.entry(machine);
		try {
			storageService = (StorageService) Naming.lookup(machine.getAddress(StorageService.class.getName()));
			return true;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean storageServerCreateFile(String fullFilePath) {
		logger.entry(fullFilePath);
		try {
			return storageService.createFile(fullFilePath, true);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
	
	public byte[] storageServerRandomReadFile(String fullFilePath, long pos, int size) {
		logger.entry(fullFilePath, pos, size);
		try {
			return storageService.randomReadFile(fullFilePath, pos, size);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] storageServerGetFile(String fullFilePath) {
		logger.entry(fullFilePath);
		try {
			return storageService.getFile(fullFilePath);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean storageServerDeleteFile(String fullFilePath) {
		logger.entry(fullFilePath);
		try {
			return storageService.deleteFile(fullFilePath, true);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
	
	public long storageServerGetSizeOfFile(String fullFilePath) {
		logger.entry(fullFilePath);
		try {
			return storageService.getSizeOfFile(fullFilePath);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return -1;
	}
	
	public boolean storageServerAppendWriteFile(String fullFilePath, byte[] data) {
		logger.entry(fullFilePath, "len:" + data.length);
		try {
			return storageService.appendWriteFile(fullFilePath, data, true);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean storageServerRandomWriteFile(String fullFilePath, long pos, byte[] data) {
		logger.entry(fullFilePath, pos, "len:" + data.length);
		try {
			return storageService.randomWriteFile(fullFilePath, pos, data, true);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean storageServerCreateDir(String fullDirPath) {
		logger.entry(fullDirPath);
		try {
			return storageService.createDir(fullDirPath, true);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean storageServerDeleteDir(String fullDirPath) {
		logger.entry(fullDirPath);
		try {
			return storageService.deleteDir(fullDirPath, true);
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
}
