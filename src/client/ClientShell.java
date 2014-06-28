package client;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import server.Machine;
import datastructure.FileUnit;


class Args4J {
	@Option(name = "-write", usage = "write filepath -p pos [-sdata|-fdata] input")
	String writePath;

	@Option(name = "-read", usage = "read filepath -p pos -l length")
	String readPath;
	
	@Option(name = "-l")
	int length = 0;
	
	@Option(name = "-p")
	long pos = 0L;

	@Option(name = "-size", usage = "size filepath")
	String sizePath;
	
	@Option(name = "-sdata")
	String sdata;
	
	@Option(name = "-fdata")
	String fdata;
	
	@Option(name = "-rm", usage = "rm filepath")
	String rmPath;
	
	@Option(name = "-rmdir", usage = "rmdir dirpath")
	String rmdirPath;

	@Option(name = "-touch", usage = "touch filepath")
	String touchPath;
	
	@Option(name = "-mkdir", usage = "mkdir dirpath")
	String mkdirPath;

	@Option(name = "-log", usage = "log")
	boolean log;
	
	@Option(name = "-quit", usage = "quit")
	boolean quit;

	@Option(name = "-help", usage = "help")
	boolean help;
	
	@Option(name = "-list", usage = "list dirpath")
	String listPath;
	
	@Option(name = "-exist", usage = "exist filepath")
	String existPath;
}

public class ClientShell {
	
	public static void main(String[] args) throws Exception {

		final Client client = new Client();
		final Args4J args4j = new Args4J();
		final CmdLineParser parser = new CmdLineParser(args4j);
		parser.parseArgument(args);
		if ( args.length == 0 ) {
			parser.printUsage(System.out);
			return;
		}
		
		if ( args4j.writePath != null ) {
			byte[] data = null;
			if ( args4j.sdata != null ) {
				data = args4j.sdata.getBytes("utf8");
				
			} else if ( args4j.fdata != null ) {
				RandomAccessFile file = new RandomAccessFile(args4j.fdata, "r");
				long length = file.length();
				data = new byte [(int) length];
				file.read(data);
				file.close();
			}
			if ( data == null ) throw new IllegalArgumentException("Need sdata or fdata");
			client.randomWriteFile(args4j.writePath, args4j.pos, data);
			
		} else if ( args4j.readPath != null ) {
			byte [] data;
			long length;
			if ( args4j.length > 0 ) {
				length = args4j.length;
			} else {
				length = client.getSizeOfFile(args4j.readPath);
			}
			data = client.randomReadFile(args4j.readPath, args4j.pos, (int) length);
			if ( data == null ) throw new RuntimeException("command execution error");
			System.out.println(new String(data, "utf8"));
			
		} else if ( args4j.sizePath != null ) {
			final long size = client.getSizeOfFile(args4j.sizePath);
			if ( size >= 0 ) System.out.println(size);
			else throw new RuntimeException("command execution error");
			
		} else if ( args4j.touchPath != null ) {
			final boolean success = client.createFile(args4j.touchPath);
			if ( !success ) throw new RuntimeException("command execution error");
			
		} else if ( args4j.mkdirPath != null ) {
			final boolean success = client.createDir(args4j.mkdirPath);
			if ( !success ) throw new RuntimeException("command execution error");
		
		} else if ( args4j.rmPath != null ) {
			final boolean success = client.deleteFile(args4j.rmPath);
			if ( !success ) throw new RuntimeException("command execution error");
			
		} else if ( args4j.rmdirPath != null ) {
			final boolean success = client.deleteDir(args4j.rmdirPath);
			if ( !success ) throw new RuntimeException("command execution error");
			
		} else if ( args4j.listPath != null ) {
			final List<FileUnit> units = client.listDir(args4j.listPath);
			for (FileUnit fileUnit : units) {
				System.out.println(args4j.listPath + "/" + fileUnit);
				List<Machine> machines = fileUnit.getAllMachines();
				for (Machine machine : machines) {
					System.out.println("\t"+machine.port);
				}
			}
			
		} else if ( args4j.existPath != null ) {
			final boolean success = client.isExistFile(args4j.existPath);
			System.out.println(success ? "True" : "False");
			
		} else if ( args4j.log ) {
			final List<FileUnit> units = client.listDir("");
			for (FileUnit unit : units) {
				unit.listRecursively(System.out);
			}
			
		} else if ( args4j.quit ) {
			System.exit(0);
			
		} else if ( args4j.help ) {
			parser.printUsage(System.out);
			
		} else {
			System.out.println("Command not found. Run help to get more information.");
		}
	}
}
