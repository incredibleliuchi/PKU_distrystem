package server.storage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.List;

import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rmi.NamingService;
import rmi.StorageService;
import rmi.StorageServiceImpl;
import server.Machine;
import server.Server;
import util.Variables;
import datastructure.FileUnit;
import datastructure.SynchroMeta;


public class StorageServer implements Server {
	private static final Logger logger = LogManager.getLogger(StorageServer.class);
	
	private static final String FILE_STORE_PATH = Variables.getInstance().getProperty("myRoot");// "storage";
//	private static final String STORAGE_SERVER_NUMBER = "3";
	
	private static StorageServer INSTANCE = null;
	public static StorageServer getInstance() {
		if ( INSTANCE == null ) INSTANCE = new StorageServer();
		return INSTANCE;
	}
	private StorageServer() {
		namingServer = new Machine("namingServer");
		me = new Machine("my");
//		me = new Machine("storageServer"+STORAGE_SERVER_NUMBER);
		
		fileStoreRootDir = FILE_STORE_PATH; //+ "/" + STORAGE_SERVER_NUMBER;
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
		List<SynchroMeta> changeMetas = null;
		try {
			changeMetas = namingService.informOnline(me, localRoot);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (changeMetas != null) {
			for (SynchroMeta synchroMeta : changeMetas) {
				String fullFilePath = fileStoreRootDir + synchroMeta.getPath();
				if (synchroMeta.getOpType() == SynchroMeta.DELETE) {
					File file = new File(fullFilePath);
					if (file.isDirectory()) {
						recursionDeleteDir(file);
					} else {
						file.delete();
					}
				}
				if (synchroMeta.getOpType() == SynchroMeta.CONFIRM) {
					connectToOtherStorageServer(synchroMeta.getTargetMachine());
					logger.info("full file path: " + fullFilePath);
					File file = new File(fullFilePath);
					file.delete();
					byte[] data = null;
					try {
						data = otherStorageService.getFile(synchroMeta.getPath());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					RandomAccessFile randomFile = null;
					try {
						randomFile = new RandomAccessFile(file, "rw");
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
				}
			}
		}
	}
	
	public final Machine namingServer;
	public final Machine me;
	private NamingService namingService;
	private StorageService otherStorageService;
	
	private final String fileStoreRootDir;

	private boolean connectToOtherStorageServer(Machine machine) {
		try {
			otherStorageService = (StorageService) Naming.lookup(machine.getAddress(StorageService.class.getName()));
			return true;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}
	
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
	
	public boolean createFile(String fullFilePath, boolean isOrigin) {
		File file = null;
		String[] path = fullFilePath.split("/");
		String nowDir = new String(fileStoreRootDir);
		for (int i = 0; i < path.length; i++) {
			nowDir = nowDir + "/" + path[i];
			file = new File(nowDir);
			if (i == path.length-1) {
				if (!file.exists()) {
					try {
						boolean isCreateSuccess = file.createNewFile();
						if (isCreateSuccess) {
							List<Machine> otherMachines = namingService.notifyCreateFile(fullFilePath, isOrigin, me);
							if (otherMachines != null) {
								for (Machine otherMachine : otherMachines) {
									if (otherMachine.equals(me)) {
										continue;
									}
									connectToOtherStorageServer(otherMachine);
									otherStorageService.createFile(fullFilePath, false);
								}
							}
						}
						return isCreateSuccess;
					} catch (IOException e) {
						logger.error(e);
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
			logger.error(e);
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		return buffer;
	}
	
	public byte[] randomReadFile(String fullFilePath, long pos, int size) {
		File file = locateFile(fullFilePath);
		if (file == null) {
			return null;
		}
		byte[] res = null;
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(file, "r");
			randomFile.seek(pos);
			res = new byte [size];
			int count = randomFile.read(res);
			if (count < 0) count = 0;
			res = Arrays.copyOf(res, count);
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		return res;
	}
	
	public boolean deleteFile(String fullFilePath, boolean isOrigin) {
		File file = locateFile(fullFilePath);
		if (file == null) {
			return false;
		}
		
		boolean isDeleteSuccess = file.delete();
		if (isDeleteSuccess) {
			try {
				List<Machine> otherMachines = namingService.notifyDeleteFile(fullFilePath, isOrigin, me);
				if (otherMachines != null) {
					for (Machine otherMachine : otherMachines) {
						if (otherMachine.equals(me)) {
							continue;
						}
						connectToOtherStorageServer(otherMachine);
						otherStorageService.deleteFile(fullFilePath, false);
					}
				}
			} catch (RemoteException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return isDeleteSuccess;
	}
	
	public long getSizeOfFile(String fullFilePath) {
		File file = locateFile(fullFilePath);
		if (file == null) {
			return -1;
		}
		return file.length();
	}
	
	public boolean appendWriteFile(String fullFilePath, byte[] data, boolean isOrigin) {
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
			logger.error(e);
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		
		try {
			List<Machine> otherMachines = namingService.notifyWriteFile(fullFilePath, isOrigin);
			if (otherMachines != null) {
				for (Machine otherMachine : otherMachines) {
					if (otherMachine.equals(me)) {
						continue;
					}
					connectToOtherStorageServer(otherMachine);
					otherStorageService.appendWriteFile(fullFilePath, data, false);
				}
			}
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean randomWriteFile(String fullFilePath, long pos, byte[] data, boolean isOrigin) {
		File file = locateFile(fullFilePath);
		if (file == null) {
			return false;
		}
		
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(file, "rw");
			long fileLength = randomFile.length();
			if (pos > fileLength) {
				logger.error("Illegal: pos > fileLength");
				randomFile.close();
				return false;
			}
            randomFile.seek(pos);
            randomFile.write(data);
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		
		try {
			List<Machine> otherMachines = namingService.notifyWriteFile(fullFilePath, isOrigin);
			if (otherMachines != null) {
				for (Machine otherMachine : otherMachines) {
					if (otherMachine.equals(me)) {
						continue;
					}
					connectToOtherStorageServer(otherMachine);
					otherStorageService.randomWriteFile(fullFilePath, pos, data, false);
				}
			}
		} catch (RemoteException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean createDir(String fullDirPath, boolean isOrigin) {
		File file = null;
		String[] path = fullDirPath.split("/");
		String nowDir = new String(fileStoreRootDir);
		for (int i = 0; i < path.length; i++) {
			nowDir = nowDir + "/" + path[i];
			file = new File(nowDir);
			if (i == path.length-1) {
				if (!file.exists()) {
					boolean isMkdirSuccess = file.mkdir();
					if (isMkdirSuccess) {
						try {
							List<Machine> otherMachines = namingService.notifyCreateDir(fullDirPath, isOrigin, me);
							if (otherMachines != null) {
								for (Machine otherMachine : otherMachines) {
									if (otherMachine.equals(me)) {
										continue;
									}
									connectToOtherStorageServer(otherMachine);
									otherStorageService.createDir(fullDirPath, false);
								}
							}
						} catch (RemoteException e) {
							logger.error(e);
							e.printStackTrace();
						}
					}
					return isMkdirSuccess;
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
	
	public boolean deleteDir(String fullDirPath, boolean isOrigin) {
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
					try {
						List<Machine> otherMachines = namingService.notifyDeleteDir(fullDirPath, isOrigin, me);
						if (otherMachines != null) {
							for (Machine otherMachine : otherMachines) {
								if (otherMachine.equals(me)) {
									continue;
								}
								connectToOtherStorageServer(otherMachine);
								otherStorageService.deleteDir(fullDirPath, false);
							}
						}
					} catch (RemoteException e) {
						logger.error(e);
						e.printStackTrace();
					}
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
	
	public static void main(String[] args) throws Exception {

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
					logger.error(e);
					e.printStackTrace();
				}
			}
		});
		timer.start();
		
	}
}
