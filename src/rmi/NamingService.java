package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import datastructure.FileUnit;
import datastructure.SynchroMeta;
import server.Machine;

public interface NamingService extends Remote {
	public void updateMachine(Machine machine) throws RemoteException;
	
	/**
	 * client to naming server to get storage server meta-info
	 */
	public Machine createFile(String fullFilePath) throws RemoteException;
	public Machine getFileLocation(String fullFilePath) throws RemoteException;
	public Machine createDir(String fullDirPath) throws RemoteException;
	public Machine getDirLocation(String fullDirPath) throws RemoteException;
	public ArrayList<FileUnit> listDir(String fullDirPath) throws RemoteException;
	
	/**
	 * storage to naming server
	 */
	public ArrayList<SynchroMeta> informOnline(Machine machine, FileUnit localRoot) throws RemoteException;
	public ArrayList<Machine> notifyCreateFile(String fullFilePath, boolean isOrigin, Machine operateMachine) throws RemoteException;
	public ArrayList<Machine> notifyDeleteFile(String fullFilePath, boolean isOrigin, Machine operateMachine) throws RemoteException;
	public ArrayList<Machine> notifyAppendWriteFile(String fullFilePath, boolean isOrigin, Machine operateMachine) throws RemoteException;
	public ArrayList<Machine> notifyCreateDir(String fullDirPath, boolean isOrigin, Machine operateMachine) throws RemoteException;
	public ArrayList<Machine> notifyDeleteDir(String fullDirPath, boolean isOrigin, Machine operateMachine) throws RemoteException;


}
