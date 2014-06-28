package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Service running on storage server. 
 */
public interface StorageService extends Remote {
	
	/**
	 * Create a file on this storage server.
	 * @param fullFilePath the to be created file's full path.
	 * @param isOrigin whether this is the original storage to create this file.
	 * @return true if succeed.
	 */
	public boolean createFile(String fullFilePath, boolean isOrigin) throws RemoteException;
	
	/**
	 * Get a file's byte array.
	 * @param fullFilePath the file's full path.
	 * @return the file's byte array. Return <b>null</b> if file doesn't exist.
	 */
	public byte[] getFile(String fullFilePath) throws RemoteException;
	
	/**
	 * Read data from a file's random position on this storage server.
	 * @param fullFilePath the to be read file's full path.
	 * @param pos the file's position which begins to read data.
	 * @param size the data to be read.
	 * @return the data byte array. Return <b>null</b> if file doesn't exist.
	 */
	public byte[] randomReadFile(String fullFilePath, long pos, int size) throws RemoteException;
	
	/**
	 * Delete a file on this storage server.
	 * @param fullFilePath the file's full path.
	 * @param isOrigin whether this is the original storage to delete this file.
	 * @return true if succeed.
	 */
	public boolean deleteFile(String fullFilePath, boolean isOrigin) throws RemoteException;
	
	/**
	 * Get a file's size.
	 * @param fullFilePath the file's full path.
	 * @return the file's size.
	 */
	public long getSizeOfFile(String fullFilePath) throws RemoteException;
	
	/**
	 * Append data to a file on this storage server.
	 * @param fullFilePath the to be appended file's full path.
	 * @param data data to be appended with.
	 * @param isOrigin whether this is the original storage to delete this file.
	 * @return true if succeed.
	 */
	public boolean appendWriteFile(String fullFilePath, byte[] data, boolean isOrigin) throws RemoteException;
	
	/**
	 * Write data to a file's random position on this storage server.
	 * @param fullFilePath the to be written file's full path.
	 * @param pos the file's position which begins to write data.
	 * @param data data to be written in.
	 * @param isOrigin whether this is the original storage to delete this file.
	 * @return true if succeed.
	 */
	public boolean randomWriteFile(String fullFilePath, long pos, byte[] data, boolean isOrigin) throws RemoteException;
	
	/**
	 * Create a directory on this storage server.
	 * @param fullDirPath the to be created directory's full path.
	 * @param isOrigin whether this is the original storage to delete this directory.
	 * @return true if succeed.
	 */
	public boolean createDir(String fullDirPath, boolean isOrigin) throws RemoteException;
	
	/**
	 * Delete a directory on this storage server.
	 * @param fullDirPath the to be deleted directory's full path.
	 * @param isOrigin whether this is the original storage to delete this directory.
	 * @return true if succeed.
	 */
	public boolean deleteDir(String fullDirPath, boolean isOrigin) throws RemoteException;
}
