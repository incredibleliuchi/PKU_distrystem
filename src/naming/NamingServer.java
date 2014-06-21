package naming;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.Server;

public class NamingServer implements Server {
	private static final Logger logger = LogManager.getLogger(NamingServiceImpl.class);
	private static NamingServer INSTANCE = null;
	public static NamingServer getInstance() {
		if ( INSTANCE == null ) INSTANCE = new NamingServer();
		return INSTANCE;
	}
	private NamingServer() {
		me = new Machine("namingServer");
	}
	
	public final Set<Machine> storages = new HashSet<>();
	public static Machine me;
	
	@Override
	public void loadService() {
		try {
			NamingServiceImpl service = new NamingServiceImpl();
			LocateRegistry.createRegistry(me.port);
			Naming.rebind(me.getAddress(NamingService.class.getName()), service);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		NamingServer.getInstance().loadService();
	}
}
