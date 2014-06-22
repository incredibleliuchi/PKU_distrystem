package client;

import java.rmi.Naming;
import server.Machine;
import server.naming.NamingService;


public class ClientRMIManager {

	private NamingService namingService;
	
	public ClientRMIManager() {
		Machine namingServer = new Machine("namingServer");
		try {
			namingService = (NamingService) Naming.lookup(namingServer.getAddress(NamingService.class.getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
