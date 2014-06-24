package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import datastructure.FileUnit;
import rmi.NamingService;
import rmi.NamingServiceImpl;
import rmi.StorageService;
import server.Machine;


public class ClientRMIManager {
	private static final Logger logger = LogManager.getLogger(ClientRMIManager.class);

	private NamingService namingService;
	private StorageService storageService;
	
	public ClientRMIManager() {
		Machine namingServer = new Machine("namingServer");
		try {
			namingService = (NamingService) Naming.lookup(namingServer.getAddress(NamingService.class.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Machine getCreateFileLocation(String fullFilePath) {
		Machine machine = null;
		try {
			machine = namingService.createFile(fullFilePath);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.error("namingServer failure");
		}
		return machine;
	}
	
	public Machine getFileLocation(String fullFilePath) {
		Machine machine = null;
		try {
			machine = namingService.getFileLocation(fullFilePath);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.error("namingServer failure");
		}
		return machine;
	}

	public Machine getCreateDirLocation(String fullDirPath) {
		Machine machine = null;
		try {
			machine = namingService.createDir(fullDirPath);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.error("namingServer failure");
		}
		return machine;
	}
	
	public Machine getDirLocation(String fullDirPath) {
		Machine machine = null;
		try {
			machine = namingService.getDirLocation(fullDirPath);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.error("namingServer failure");
		}
		return machine;
	}
	
	public ArrayList<FileUnit> listDir(String fullDirPath) {
		ArrayList<FileUnit> fileUnits = null;
		try {
			fileUnits = namingService.listDir(fullDirPath);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.error("namingServer failure");
		}
		return fileUnits;
	}

	public boolean connectToStorageServer(Machine machine) {
		try {
			storageService = (StorageService) Naming.lookup(machine.getAddress(StorageService.class.getName()));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean storageServerCreateFile(String fullFilePath) {
		try {
			return storageService.createFile(fullFilePath);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public byte[] storageServerGetFile(String fullFilePath) {
		try {
			return storageService.getFile(fullFilePath);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean storageServerDeleteFile(String fullFilePath) {
		try {
			return storageService.deleteFile(fullFilePath);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public long storageServerGetSizeOfFile(String fullFilePath) {
		try {
			return storageService.getSizeOfFile(fullFilePath);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public boolean storageServerAppendWriteFile(String fullFilePath, byte[] data) {
		try {
			return storageService.appendWriteFile(fullFilePath, data);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean storageServerCreateDir(String fullDirPath) {
		try {
			return storageService.createDir(fullDirPath);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean storageServerDeleteDir(String fullDirPath) {
		try {
			return storageService.deleteDir(fullDirPath);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
}
