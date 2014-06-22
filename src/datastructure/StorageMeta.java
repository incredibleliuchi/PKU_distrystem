package datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Store meta-data of one storage server for naming server.
 */
public class StorageMeta {

	private ArrayList<FileUnit> storedFileUnits;
	
	public StorageMeta() {
		storedFileUnits = new ArrayList<>();
	}
	
	public void addFileUnits(FileUnit fileUnit) {
		storedFileUnits.add(fileUnit);
	}
	
	
}
