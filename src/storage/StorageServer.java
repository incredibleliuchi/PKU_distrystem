package storage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import naming.Machine;
import naming.NamingService;
import naming.NamingServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.Server;


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
		loadService();
	}
	
	public final Machine namingServer;
	public final Machine me;

	@Override
	public void loadService() {
		try {
			StorageService service = new StorageServiceImpl();
			LocateRegistry.createRegistry(me.port);
			Naming.rebind(me.getAddress(StorageService.class.getName()), service);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		StorageServer server = StorageServer.getInstance();
		server.loadService();
		NamingService loadService = (NamingService) Naming.lookup(server.namingServer.getAddress(NamingService.class.getName()));
		loadService.addMachine(server.me);
	}
}
