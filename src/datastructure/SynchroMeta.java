package datastructure;

import java.io.Serializable;

import server.Machine;

public class SynchroMeta implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int DELETE = 0;
	public static final int CONFIRM = 1;
	
	private final String path;
	private final int opType;
	private final Machine targetMachine;

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
