package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StorageService extends Remote {
	
	public boolean createFile(String fullFilePath, boolean isOrigin) throws RemoteException;
	public byte[] getFile(String fullFilePath) throws RemoteException;
	public boolean deleteFile(String fullFilePath, boolean isOrigin) throws RemoteException;
	public long getSizeOfFile(String fullFilePath) throws RemoteException;
	public boolean appendWriteFile(String fullFilePath, byte[] data, boolean isOrigin) throws RemoteException;
	public boolean createDir(String fullDirPath, boolean isOrigin) throws RemoteException;
	public boolean deleteDir(String fullDirPath, boolean isOrigin) throws RemoteException;

}
