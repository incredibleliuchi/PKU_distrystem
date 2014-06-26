package client;

import java.io.UnsupportedEncodingException;
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
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		Client client = new Client();

		System.out.println(client.createDir("aaa"));
		System.out.println(client.createFile("aaa/ddd.txt"));
		System.out.println(client.createDir("aaa/bbb"));
		System.out.println(client.createFile("aaa/bbb/ddd.txt"));
		System.out.println(client.createFile("aaa/bbb/ccc/ddd.txt"));
		System.out.println(client.deleteFile("aaa/ddd.tt"));
		System.out.println(client.createFile("liuchi.txt"));
		byte[] data = "This is a shit.".getBytes("utf8");
		System.out.println(client.appendWriteFile("liuchi.txt", data));
		System.out.println(client.appendWriteFile("liuchi.txt", data));
		
		System.out.println("=======================");
		
		System.out.println(client.getSizeOfFile("liuchi.txt"));
		
		ArrayList<FileUnit> dir = client.listDir("");
		for (FileUnit fileUnit : dir) {
			System.out.println(fileUnit.getName() + " " + fileUnit.isDir());
		}
	}
}
