package client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rmi.NamingService;
import rmi.NamingServiceImpl;
import server.Machine;


public class ClientRMIManager {
	private static final Logger logger = LogManager.getLogger(ClientRMIManager.class);

	private NamingService namingService;
	
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

	public Machine getCreateDirLocation(String fullFilePath) {
		Machine machine = null;
		try {
			machine = namingService.createFile(fullFilePath);
		} catch (RemoteException e) {
			e.printStackTrace();
			logger.error("namingServer failure");
		}
		return machine;
	}
	
}
