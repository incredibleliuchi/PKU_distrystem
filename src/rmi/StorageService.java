package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StorageService extends Remote {
	
	public boolean createFile(String fullFilePath) throws RemoteException;
	public byte[] getFile(String fullFilePath) throws RemoteException;
	public boolean deleteFile(String fullFilePath) throws RemoteException;
	public long getSizeOfFile(String fullFilePath) throws RemoteException;
	public boolean appendWriteFile(String fullFilePath, byte[] data) throws RemoteException;
	
	public boolean createDir(String fullDirPath) throws RemoteException;
	public boolean deleteDir(String fullDirPath) throws RemoteException;

}
