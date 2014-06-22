package server.naming;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.Machine;
import server.Server;
import server.storage.StorageMeta;
import util.Variables;

public class NamingServer implements Server {
	private static final Logger logger = LogManager.getLogger(NamingServer.class);
	private static NamingServer INSTANCE = null;
	public static NamingServer getInstance() {
		if ( INSTANCE == null ) INSTANCE = new NamingServer();
		return INSTANCE;
	}
	private NamingServer() {
		me = new Machine("namingServer");
	}
	
	public final Map<Machine, Long> storageValids = new HashMap<>();
	public final Map<Machine, StorageMeta> storageMetas = new HashMap<>();
	public final Machine me;
	
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
			// TODO: immigrate data.
			storageValids.remove(machine);
			storageMetas.remove(machine);
		}
	}
	
	
	public static void main(String[] args) {
		NamingServer.getInstance().loadService();
		
		// Check whether each storage server is available
		final int interval = Integer.parseInt(Variables.getInstance().getProperty("contactInterval")); 
		final Timer timer = new Timer(interval, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					NamingServer.getInstance().checkStorages();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		timer.start();
	}
}
