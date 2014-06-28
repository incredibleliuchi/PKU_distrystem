package datastructure;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.Machine;

/**
 * A file unit which can be a file or directory. 
 */
public class FileUnit implements Serializable {
	private static final Logger logger = LogManager.getLogger(FileUnit.class);
	private static final long serialVersionUID = 1L;
	private final String name;
	private final boolean isDir;
	private List<FileUnit> lowerFileUnits;
	private final List<Machine> storageMachines;
	
	public FileUnit(String name, boolean isDir) {
		this.name = name;
		this.isDir = isDir;
		if (isDir) {
			lowerFileUnits = new ArrayList<>();
		}
		storageMachines = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isDir() {
		return isDir;
	}
	
	public List<FileUnit> list() {
		return lowerFileUnits;
	}
	
	public boolean addLowerFileUnit(FileUnit fileUnit) {
		if (isDir) {
			for (int i = 0; i < lowerFileUnits.size(); i++) {
				if (lowerFileUnits.get(i).name == fileUnit.name && lowerFileUnits.get(i).isDir == fileUnit.isDir) {
					return false;
				}
			}
			lowerFileUnits.add(fileUnit);
			return true;
		}
		return false;
	}
	
	public void addStorageMachine(Machine machine) {
		for (int i=0; i<storageMachines.size(); ++i) {
			if (storageMachines.get(i).port == machine.port && storageMachines.get(i).ip.equals(machine.ip)) {
				return;
			}
		}
		storageMachines.add(machine);
	}
	
	public boolean isStoredAtMachine(Machine machine) {
		for (int i=0; i<storageMachines.size(); ++i) {
			if (storageMachines.get(i).port == machine.port && storageMachines.get(i).ip.equals(machine.ip)) {
				return true;
			}
		}
		return false;
	}
	
	public int getStorageMachineNum() {
		return storageMachines.size();
	}
	
	public void deleteStorageMachine(Machine machine) {
		for (int i=0; i<storageMachines.size(); ++i) {
			if (storageMachines.get(i).port == machine.port && storageMachines.get(i).ip.equals(machine.ip)) {
				storageMachines.remove(i);
				break;
			}
		}
	}
	
	public List<Machine> getAllMachines() {
		return storageMachines;
	}
	
	public void deleteLowerFileUnit(FileUnit target) {
		for (int i = 0; i < lowerFileUnits.size(); i++) {
			if (lowerFileUnits.get(i).name == target.name && lowerFileUnits.get(i).isDir == target.isDir) {
				lowerFileUnits.remove(i);
				break;
			}
		}
	}
	
	public void listRecursively(OutputStream out, String prefix) {
		if (lowerFileUnits == null) return;
		if (prefix.length() > 0 && prefix.charAt(prefix.length() - 1) != '/') prefix += "/";
		try {
			for (FileUnit unit : lowerFileUnits) {
				out.write((prefix + unit + " -- ").getBytes("utf8"));
				for (Machine machine : unit.storageMachines) {
					out.write((machine + "; ").getBytes("utf8"));
				}
				out.write("\n".getBytes("utf8"));
				if ( unit.isDir ) unit.listRecursively(out, prefix + unit.name);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return name + "[" + (isDir ? "d" : "f") + "]"; 
	}
}
