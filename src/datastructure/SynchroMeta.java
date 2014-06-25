package datastructure;

import java.io.Serializable;

import server.Machine;

public class SynchroMeta  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7713863239555967251L;
	public static final int DELETE = 0;
	public static final int CONFIRM = 1;
	
	private String path;
	private int opType;
	private Machine targetMachine;

	public SynchroMeta(String path, int opType, Machine targetMachine) {
		this.path = path;
		this.opType = opType;
		this.targetMachine = targetMachine;
	}

	public String getPath() {
		return path;
	}

	public int getOpType() {
		return opType;
	}

	public Machine getTargetMachine() {
		return targetMachine;
	}
	
}
