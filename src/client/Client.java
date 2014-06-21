package client;


public class Client {
	
	private ClientRMIManager rmiManager;
	
	public Client() {
		rmiManager = new ClientRMIManager();
	}
	
	public boolean createFile(String fileName, String dirPath) {
		return false;
	}
	
	public byte[] getFile(String fileName, String dirPath) {
		return null;
	}
	
	public boolean deleteFile(String fileName, String dirPath) {
		return false;
	}
	
	public boolean isExistFile(String fileName, String dirPath) {
		return false;
	}
	
	public long getSizeOfFile(String fileName, String dirPath) {
		return 0;
	}

	public boolean appendWriteFile(String fileName, String dirPath, byte[] data) {
		return false;
	}
	
	public boolean createDir(String dirName, String dirFatherPath) {
		return false;
	}
	
	public boolean deleteDir(String dirName, String dirFatherPath) {
		return false;
	}
	
	public boolean listDir(String dirName, String dirFatherPath) {
		return false;
	}
	
}
