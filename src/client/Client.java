package client;

import java.util.ArrayList;

import datastructure.FileUnit;
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
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return false;
		}
		
		return rmiManager.storageServerCreateFile(fullFilePath);
	}
	
	public byte[] getFile(String fullFilePath) {
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return null;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return null;
		}
		
		return rmiManager.storageServerGetFile(fullFilePath);
	}
	
	public boolean deleteFile(String fullFilePath) {
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return false;
		}
		
		return rmiManager.storageServerDeleteFile(fullFilePath);
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
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return -1;
		}
		
		return rmiManager.storageServerGetSizeOfFile(fullFilePath);
	}

	public boolean appendWriteFile(String fullFilePath, byte[] data) {
		Machine targetMachine = rmiManager.getCreateFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return false;
		}
		
		return rmiManager.storageServerAppendWriteFile(fullFilePath, data);
	}
	
	public boolean createDir(String fullDirPath) {
		Machine targetMachine = rmiManager.getCreateDirLocation(fullDirPath);
		if (targetMachine == null) {
			return false;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return false;
		}
		
		return rmiManager.storageServerCreateDir(fullDirPath);
	}
	
	public boolean deleteDir(String fullDirPath) {
		Machine targetMachine = rmiManager.getDirLocation(fullDirPath);
		if (targetMachine == null) {
			return false;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return false;
		}
		
		return rmiManager.storageServerDeleteDir(fullDirPath);
	}
	
	public ArrayList<FileUnit> listDir(String fullDirPath) {
		ArrayList<FileUnit> result = rmiManager.listDir(fullDirPath);
		return result;
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		
		
		ArrayList<FileUnit> dir = client.listDir("aaa");
		for (FileUnit fileUnit : dir) {
			System.out.println(fileUnit.getName() + " " + fileUnit.isDir());
		}
	}
}
