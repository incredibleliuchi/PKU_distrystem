package server.naming;

import java.io.Serializable;

import util.Variables;

public class Machine implements Serializable {
	private static final long serialVersionUID = 1;
	final public String ip;
	final public int port;
	
	public Machine(String name) {
		ip = Variables.getInstance().getProperty(name + "Ip");
		port = Integer.parseInt(Variables.getInstance().getProperty(name + "Port"));
	}
	
	public Machine(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public int hashCode() {
		return ip.hashCode() * 23 + new Integer(port).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Machine &&
				ip.equals(((Machine) obj).ip) && 
				port == ((Machine) obj).port; 
	}
	
	public String getAddress(String serviceName) {
		return "rmi://" + toString() + "/" + serviceName;
	}
	
	@Override
	public String toString() {
		return ip + ":" + port;
	}
}
