package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import server.Machine;
import datastructure.FileUnit;
import datastructure.SynchroMeta;

/**
 * Service running on naming server. 
 */
public interface NamingService extends Remote {
	
	/**
	 * Update a machine's heart beat. <b>Called by a timer on storage server.</b>
	 * @param machine the storage to send heart beat itself.
	 */
	public void updateMachine(Machine machine) throws RemoteException;
	
	/**
	 * Create a file. <b>Called by client.</b>
	 * @param fullFilePath the to be created file's full path.
	 * @return the machine which stores this file.
	 */
	public Machine createFile(String fullFilePath) throws RemoteException;
	
	/**
	 * Get a machine which stores a specific file. <b>Called by client.</b>
	 * @param fullFilePath the specific file's full path.
	 * @return the machine which stores this file. 
	 */
	public Machine getFileLocation(String fullFilePath) throws RemoteException;
	
	/**
	 * Create a directory. <b>Called by client.</b>
	 * @param fullDirPath the to be created directory's full path.
	 * @return the machine which stores this directory. 
	 */
	public Machine createDir(String fullDirPath) throws RemoteException;
	
	/**
	 * Get a machine which stores a specific directory. <b>Called by client.</b>
	 * @param fullDirPath the specific directory's full path.
	 * @return the machine which stores this file. 
	 */
	public Machine getDirLocation(String fullDirPath) throws RemoteException;
	
	/**
	 * Get the list of {@link FileUnit} in a specific directory. <b>Called by client.</b>
	 * @param fullDirPath the specific directory's full path
	 * @return A list of {@link FileUnit} in the specific directory.
	 */
	public List<FileUnit> listDir(String fullDirPath) throws RemoteException;
	
	/**
	 * Inform naming server that this storage server is online now. <b>Called by storage server.</b>
	 * @param machine this storage server.
	 * @param localRoot all files in this storage server.
	 * @return A list to tell which files are confirmed and which are old and need to be deleted. 
	 */
	public List<SynchroMeta> informOnline(Machine machine, FileUnit localRoot) throws RemoteException;
	
	/**
	 * Notify naming server that this storage server has created a file. <b>Called by storage server.</b>
	 * @param fullFilePath the created file's full path.
	 * @param isOrigin whether this is the original storage server to create this file.
	 * @param operateMachine this storage server.
	 * @return A list of machines which are going to own this file by this storage server.
	 */
	public List<Machine> notifyCreateFile(String fullFilePath, boolean isOrigin, Machine operateMachine) throws RemoteException;
	
	/**
	 * Notify naming server that this storage server delete a file. <b>Called by storage server.</b>
	 * @param fullFilePath the deleted file's full path.
	 * @param isOrigin whether this is the original storage server to delete this file.
	 * @param operateMachine this storage server.
	 * @return A list of machines which are going to delete this file by this storage server.
	 * (i.e. those machines which has stored this file)
	 */
	public List<Machine> notifyDeleteFile(String fullFilePath, boolean isOrigin, Machine operateMachine) throws RemoteException;
	
	/**
	 * Notify naming server that this storage server write a file. <b>Called by storage server.</b>
	 * @param fullFilePath the written file's full path.
	 * @param isOrigin whether this is the original storage server to write this file.
	 * @return A list of machines which are going to write this file by this storage server. 
	 * (i.e. those machines which has stored this file)
	 */
	public List<Machine> notifyWriteFile(String fullFilePath, boolean isOrigin) throws RemoteException;
	
	/**
	 * Notify naming server that this storage server create a directory. <b>Called by storage server.</b>
	 * @param fullDirPath the created directory's full path.
	 * @param isOrigin whether this is the original storage server to create this directory.
	 * @param operateMachine this storage server.
	 * @return A list of machines which are going to crate this directory by this storage server.
	 */
	public List<Machine> notifyCreateDir(String fullDirPath, boolean isOrigin, Machine operateMachine) throws RemoteException;
	
	/**
	 * Notify naming server that this storage server delete a directory. <b>Called by storage server.</b>
	 * @param fullFilePath the deleted directory's full path.
	 * @param isOrigin whether this is the original storage server to delete this directory.
	 * @param operateMachine this storage server.
	 * @return A list of machines which are going to delete this directory by this storage server.
	 * (i.e. those machines which has stored this directory)
	 */
	public List<Machine> notifyDeleteDir(String fullDirPath, boolean isOrigin, Machine operateMachine) throws RemoteException;
}
