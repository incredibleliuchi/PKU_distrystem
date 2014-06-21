package server.storage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.Machine;
import server.Server;
import server.naming.NamingService;
import server.naming.NamingServiceImpl;


public class StorageServer implements Server {
	private static final Logger logger = LogManager.getLogger(NamingServiceImpl.class);
	private static StorageServer INSTANCE = null;
	public static StorageServer getInstance() {
		if ( INSTANCE == null ) INSTANCE = new StorageServer();
		return INSTANCE;
	}
	private StorageServer() {
		namingServer = new Machine("namingServer");
		me = new Machine("my");
	}
	
	public final Machine namingServer;
	public final Machine me;

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
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		StorageServer server = StorageServer.getInstance();
		NamingService loadService = (NamingService) Naming.lookup(server.namingServer.getAddress(NamingService.class.getName()));
		for (int i = 0; i < 10; i ++) {
			loadService.addMachine(server.me);
		}
		server.loadService();
	}
}
