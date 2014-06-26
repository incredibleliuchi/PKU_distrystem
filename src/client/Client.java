package client;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.AALOAD;

import datastructure.FileUnit;
import server.Machine;


public class Client {
	
	private ClientRMIManager rmiManager;
	
	public Client() {
		rmiManager = new ClientRMIManager();
	}
	
	public boolean createFile(String fullFilePath) {

		if (fullFilePath.equals("")) {
			return false;
		}
		
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

		if (fullFilePath.equals("")) {
			return null;
		}
		
		Machine targetMachine = rmiManager.getFileLocation(fullFilePath);
		if (targetMachine == null) {
			return null;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return null;
		}
		
		return rmiManager.storageServerGetFile(fullFilePath);
	}
	
	public boolean deleteFile(String fullFilePath) {

		if (fullFilePath.equals("")) {
			return false;
		}
		
		Machine targetMachine = rmiManager.getFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return false;
		}
		
		return rmiManager.storageServerDeleteFile(fullFilePath);
	}
	
	public boolean isExistFile(String fullFilePath) {

		if (fullFilePath.equals("")) {
			return false;
		}
		
		Machine targetMachine = rmiManager.getFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public long getSizeOfFile(String fullFilePath) {

		if (fullFilePath.equals("")) {
			return -1;
		}
		
		Machine targetMachine = rmiManager.getFileLocation(fullFilePath);
		if (targetMachine == null) {
			return -1;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return -1;
		}
		
		return rmiManager.storageServerGetSizeOfFile(fullFilePath);
	}

	public boolean appendWriteFile(String fullFilePath, byte[] data) {

		if (fullFilePath.equals("")) {
			return false;
		}
		
		Machine targetMachine = rmiManager.getFileLocation(fullFilePath);
		if (targetMachine == null) {
			return false;
		}
		
		if (!rmiManager.connectToStorageServer(targetMachine)) {
			return false;
		}
		
		return rmiManager.storageServerAppendWriteFile(fullFilePath, data);
	}
	
	public boolean createDir(String fullDirPath) {

		if (fullDirPath.equals("")) {
			return false;
		}
		
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

		if (fullDirPath.equals("")) {
			return false;
		}
		
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
		
		//byte[] data = {65,66,67,68,69,70};
		//System.out.println(client.appendWriteFile("liuchi.dd", data));
		//System.out.println(client.appendWriteFile("liuchi.ddd", data));
		
		System.out.println("=======================");
		
		//System.out.println(client.getSizeOfFile("liuchi.ddd"));
		
		//System.out.println(client.isExistFile("aaa"));
		//System.out.println(client.isExistFile("aaa/ddd.tt"));
		//System.out.println(client.isExistFile("aaa/dcc.ee"));
		//System.out.println(client.isExistFile("qwer"));
		
		ArrayList<FileUnit> dir = client.listDir("");
		for (FileUnit fileUnit : dir) {
			System.out.println(fileUnit.getName() + " " + fileUnit.isDir());
		}
		//data = client.getFile("liuchi.ddd");
		//for (int i = 0; i < data.length; i++) {
		//	System.out.println(data[i]);
		//}
	}
}
