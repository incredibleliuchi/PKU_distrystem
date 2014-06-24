package server.storage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import datastructure.FileUnit;
import rmi.NamingService;
import rmi.NamingServiceImpl;
import rmi.StorageService;
import rmi.StorageServiceImpl;
import server.Machine;
import server.Server;
import util.Variables;


public class StorageServer implements Server {
	
	private static final String FILE_STORE_PATH = "storage";
	private static final String STORAGE_SERVER_NUMBER = "1";
	
	private static final Logger logger = LogManager.getLogger(NamingServiceImpl.class);
	private static StorageServer INSTANCE = null;
	public static StorageServer getInstance() {
		if ( INSTANCE == null ) INSTANCE = new StorageServer();
		return INSTANCE;
	}
	private StorageServer() {
		namingServer = new Machine("namingServer");
		me = new Machine("my");
		
		fileStoreRootDir = FILE_STORE_PATH + "/" + STORAGE_SERVER_NUMBER;
		File storeDir = new File(FILE_STORE_PATH);
		if (!storeDir.exists()) {
			storeDir.mkdir();
		}
		File thisServerDir = new File(fileStoreRootDir);
		if (!thisServerDir.exists()) {
			thisServerDir.mkdir();
		}
		
		Machine namingServer = new Machine("namingServer");
		try {
			namingService = (NamingService) Naming.lookup(namingServer.getAddress(NamingService.class.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileUnit localRoot = generateDirTree(thisServerDir);
		try {
			namingService.informOnline(me, localRoot);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public final Machine namingServer;
	public final Machine me;
	//TODO inform naming server data change
	private NamingService namingService;
	
	private String fileStoreRootDir;

	@Override
	public void loadService() {
		try {
			final StorageService service = new StorageServiceImpl();
			LocateRegistry.createRegistry(me.port);
			Naming.rebind(me.getAddress(StorageService.class.getName()), service);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	private FileUnit generateDirTree(File dir) {
		FileUnit result = new FileUnit(dir.getName(), true);
		File[] lowerFiles = dir.listFiles();
		for (File file : lowerFiles) {
			if (file.isDirectory()) {
				FileUnit fileUnit = generateDirTree(file);
				result.addLowerFileUnit(fileUnit);
			} else {
				FileUnit fileUnit = new FileUnit(file.getName(), false);
				result.addLowerFileUnit(fileUnit);
			}
		}
		return result;
	}
	
	private File locateFile(String fullFilePath) {
		File file = null;
		String[] path = fullFilePath.split("/");
		String nowDir = new String(fileStoreRootDir);
		for (int i = 0; i < path.length; i++) {
			nowDir = nowDir + "/" + path[i];
			file = new File(nowDir);
			if (i == path.length-1) {
				if (!file.exists()) {
					return null;
				}
			} else {
				if (!file.exists() || !file.isDirectory()) {
					return null;
				}
			}
		}
		return file;
	}
	
	public boolean createFile(String fullFilePath) {
		File file = null;
		String[] path = fullFilePath.split("/");
		String nowDir = new String(fileStoreRootDir);
		for (int i = 0; i < path.length; i++) {
			nowDir = nowDir + "/" + path[i];
			file = new File(nowDir);
			if (i == path.length-1) {
				if (!file.exists()) {
					try {
						return file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (!file.exists()) {
					file.mkdir();
				} else if (!file.isDirectory()) {
					return false;
				}
			}
		}
		return false;
	}
	
	public byte[] getFile(String fullFilePath) {
		File file = locateFile(fullFilePath);
		if (file == null) {
			return null;
		}
		byte[] buffer = null;
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			
			int length = (int)file.length();
			buffer = new byte[length];
			inputStream.read(buffer);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer;
	}
	
	public boolean deleteFile(String fullFilePath) {
		File file = locateFile(fullFilePath);
		if (file == null) {
			return false;
		}
		
		return file.delete();
	}
	
	public long getSizeOfFile(String fullFilePath) {
		File file = locateFile(fullFilePath);
		if (file == null) {
			return -1;
		}
		
		return file.length();
	}
	
	public boolean appendWriteFile(String fullFilePath, byte[] data) {
		File file = locateFile(fullFilePath);
		if (file == null) {
			return false;
		}
		
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(file, "rw");
			long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(data);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}
	
	public boolean createDir(String fullDirPath) {
		File file = null;
		String[] path = fullDirPath.split("/");
		String nowDir = new String(fileStoreRootDir);
		for (int i = 0; i < path.length; i++) {
			nowDir = nowDir + "/" + path[i];
			file = new File(nowDir);
			if (i == path.length-1) {
				if (!file.exists()) {
					return file.mkdir();
				}
			} else {
				if (!file.exists()) {
					file.mkdir();
				} else if (!file.isDirectory()) {
					return false;
				}
			}
		}
		return false;
	}
	
	private void recursionDeleteDir(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				recursionDeleteDir(file);
			} else {
				file.delete();
			}
		}
		dir.delete();
	}
	
	public boolean deleteDir(String fullDirPath) {
		File file = null;
		String[] path = fullDirPath.split("/");
		String nowDir = new String(fileStoreRootDir);
		for (int i = 0; i < path.length; i++) {
			nowDir = nowDir + "/" + path[i];
			file = new File(nowDir);
			if (i == path.length-1) {
				if (!file.exists() || !file.isDirectory()) {
					return false;
				} else {
					recursionDeleteDir(file);
					return true;
				}
			} else {
				if (!file.exists() || !file.isDirectory()) {
					return false;
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		StorageServer.getInstance().loadService();
		
		final int interval = Integer.parseInt(Variables.getInstance().getProperty("contactInterval")); 
		final Timer timer = new Timer(interval, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					StorageServer server = StorageServer.getInstance();
					NamingService loadService = (NamingService) Naming.lookup(server.namingServer.getAddress(NamingService.class.getName()));
					loadService.updateMachine(server.me);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		timer.start();

		
	}
}
