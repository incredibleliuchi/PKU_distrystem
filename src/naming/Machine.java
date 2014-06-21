package naming;

import java.io.Serializable;

public class Machine implements Serializable {
	private static final long serialVersionUID = 1L;
	final public String ip;
	final public int port;
	
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
}
