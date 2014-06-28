package server.naming;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rmi.NamingService;
import rmi.NamingServiceImpl;
import rmi.StorageService;
import server.Machine;
import server.Server;
import util.Variables;
import datastructure.FileUnit;

public class NamingServer implements Server {
	private static final Logger logger = LogManager.getLogger(NamingServer.class);
	private static NamingServer INSTANCE = null;
	public static NamingServer getInstance() {
		if ( INSTANCE == null ) INSTANCE = new NamingServer();
		return INSTANCE;
	}
	private NamingServer() {
		me = new Machine("namingServer");
		
		root = new FileUnit("root", true);
	}
	
	public final Map<Machine, Long> storageValids = new HashMap<>();
	public final Machine me;
	
	private StorageService storageService;
	
	private boolean connectToStorageServer(Machine machine) {
		try {
			storageService = (StorageService) Naming.lookup(machine.getAddress(StorageService.class.getName()));
			return true;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * record the virtual user-view document tree structure
	 * the root directory
	 */
	private final FileUnit root;
	
	public FileUnit getRoot() {
		return root;
	}
	
	@Override
	public void loadService() {
		try {
			final NamingServiceImpl service = new NamingServiceImpl();
			LocateRegistry.createRegistry(me.port);
			Naming.rebind(me.getAddress(NamingService.class.getName()), service);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Check whether each storage server is valid now. <br/>
	 * If not valid, remove the storage server.
	 */
	public void checkStorages() {
		final long now = new Date().getTime();
		final long threshold = Long.parseLong(Variables.getInstance().getProperty("contactThreshold"));
		Set<Machine> toRemove = new HashSet<>();
		for (Machine machine : storageValids.keySet()) {
			final long date = storageValids.get(machine);
			if ( now - date > threshold ) {
				toRemove.add(machine);
			}
		}
		for (Machine machine : toRemove) {
			logger.info(String.format("removing %s (last contact: %s)", machine, new Date(storageValids.get(machine))));
			refreshDirRecordWithMachineInvalid(machine, root);
			storageValids.remove(machine);
		}
	}
	
	/**
	 * First, immigrate toCopy set.
	 * Then, check if there are at least 3 copies for each file, 
	 * and put those files to toCopy set.
	 */
	@Deprecated
	public void checkCopies() {
		// TODO
	}

	private void refreshDirRecordWithMachineInvalid(Machine invalidMachine, FileUnit nowDir) {
		List<FileUnit> fileUnits = nowDir.list();
		for (int j = 0; j < fileUnits.size(); j++) {
			FileUnit fileUnit = fileUnits.get(j);
			if (fileUnit.isDir()) {
				refreshDirRecordWithMachineInvalid(invalidMachine, fileUnit);
			}
			fileUnit.deleteStorageMachine(invalidMachine);
			if (fileUnit.getAllMachines().size() == 0) {
				nowDir.deleteLowerFileUnit(fileUnit);
				j--;
			}
		}

	}
	
	/**
	 * Set a timer to check whether each storage server is available
	 * @return timer
	 */
	public Timer startValidTimer() {
		final int interval = Integer.parseInt(Variables.getInstance().getProperty("contactInterval")); 
		final Timer timer = new Timer(interval, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					NamingServer.getInstance().checkStorages();
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		});
		timer.start();
		return timer;
	}
	

	/**
	 * Set a timer to check if there are at least 3 copies of each file
	 */
	@Deprecated
	public Timer startCopyTimer() {
		final int interval = Integer.parseInt(Variables.getInstance().getProperty("copyThreshold")); 
		final Timer timer = new Timer(interval, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					NamingServer.getInstance().checkCopies();
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		});
		timer.start();
		return timer;
	}
	
	public void checkDupFileNumAndIncrease(FileUnit nowDir, String nowPath) {
		List<FileUnit> fileUnits = nowDir.list();
		for (int j = 0; j < fileUnits.size(); j++) {
			FileUnit fileUnit = fileUnits.get(j);
			int dupTarget = 3;
			if (storageValids.size() < 3) {
				dupTarget = storageValids.size();
			}
			
			if (fileUnit.getAllMachines().size() < dupTarget) {
				Machine storedMachine = fileUnit.getAllMachines().get(0);
				
				String fullDirPath = nowPath + fileUnit.getName();
				int storeIndex = fullDirPath.hashCode() % storageValids.size();
				storeIndex = (storeIndex + storageValids.size()) % storageValids.size();
				int backNum = dupTarget - fileUnit.getAllMachines().size();
				while (backNum > 0) {
					Machine machine = new ArrayList<>(storageValids.keySet()).get(storeIndex);
					storeIndex = (storeIndex + 1) % storageValids.size();
					if (!fileUnit.isStoredAtMachine(machine)) {
						
						connectToStorageServer(machine);
						if (fileUnit.isDir()) {
							try {
								storageService.createDir(fullDirPath, false);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						} else {
							try {
								storageService.createFile(fullDirPath, false);
								connectToStorageServer(storedMachine);
								byte[] data = storageService.getFile(fullDirPath);
								
								connectToStorageServer(machine);
								storageService.appendWriteFile(fullDirPath, data, false);
								
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
						
						backNum--;
					}
				}
			}
			if (fileUnit.isDir()) {
				checkDupFileNumAndIncrease(fileUnit, nowPath + fileUnit.getName() + "/");
			}
		}

	}
	
	
	public static void main(String[] args) {
		NamingServer.getInstance().loadService();
		NamingServer.getInstance().startValidTimer();
//		NamingServer.getInstance().startCopyTimer();
	}
}
