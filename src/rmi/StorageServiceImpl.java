package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.Machine;
import server.storage.StorageServer;

public class StorageServiceImpl extends UnicastRemoteObject implements StorageService {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(StorageServiceImpl.class);
	public StorageServiceImpl() throws RemoteException {
		super();
	}
	
	
	
	
	@Override
	public boolean createFile(String fullFilePath) throws RemoteException {
		return StorageServer.getInstance().createFile(fullFilePath);
	}
	@Override
	public byte[] getFile(String fullFilePath) throws RemoteException {
		return StorageServer.getInstance().getFile(fullFilePath);
	}
	@Override
	public boolean deleteFile(String fullFilePath) throws RemoteException {
		return StorageServer.getInstance().deleteFile(fullFilePath);
	}
	@Override
	public long getSizeOfFile(String fullFilePath) throws RemoteException {
		return StorageServer.getInstance().getSizeOfFile(fullFilePath);
	}
	@Override
	public boolean appendWriteFile(String fullFilePath, byte[] data)
			throws RemoteException {
		return StorageServer.getInstance().appendWriteFile(fullFilePath, data);
	}
	@Override
	public boolean createDir(String fullDirPath) throws RemoteException {
		return StorageServer.getInstance().createDir(fullDirPath);
	}
	@Override
	public boolean deleteDir(String fullDirPath) throws RemoteException {
		return StorageServer.getInstance().deleteDir(fullDirPath);
	}
	
}
