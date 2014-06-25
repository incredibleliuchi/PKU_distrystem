package datastructure;

import java.io.Serializable;
import java.util.ArrayList;

import server.Machine;

public class FileUnit  implements Serializable {
	
	private static final long serialVersionUID = 8247624941002229076L;
	private String name;
	private boolean isDir;
	private ArrayList<FileUnit> lowerFileUnits;
	private ArrayList<Machine> storageMachines;
	
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
	public ArrayList<FileUnit> list() {
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
	public ArrayList<Machine> getAllMachines() {
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

}