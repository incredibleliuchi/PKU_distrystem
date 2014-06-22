package client;

import server.Machine;


public class Client {
	
	private ClientRMIManager rmiManager;
	
	public Client() {
		rmiManager = new ClientRMIManager();
	}
	
	public boolean createFile(String fullFilePath) {
		
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		}
		
		//TODO
		
		return false;
	}
	
	public byte[] getFile(String fullFilePath) {
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return null;
		}
		
		//TODO
		return null;
	}
	
	public boolean deleteFile(String fullFilePath) {
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		}
		
		//TODO
		
		return false;
	}
	
	public boolean isExistFile(String fullFilePath) {
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public long getSizeOfFile(String fullFilePath) {
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return -1;
		}
		//TODO
		return -1;
	}

	public boolean appendWriteFile(String fullFilePath, byte[] data) {
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		}
		//TODO
		return false;
	}
	
	public boolean createDir(String fullFilePath) {
		//TODO
		return false;
	}
	
	public boolean deleteDir(String fullFilePath) {
		//TODO
		return false;
	}
	
	public boolean listDir(String fullFilePath) {
		//TODO
		return false;
	}
	
}
