package ui;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import server.naming.NamingServer;
import server.storage.StorageServer;


class Args4J {
	@Option(name = "-naming", usage = "Start as naming server.")
	boolean naming;
	
	@Option(name = "-storage", usage = "Start as storage server.")
	boolean storage;
	
	@Option(name = "-client", usage = "Start as client.")
	boolean client;
}

public class Main {
	public static void main(String[] args) throws Exception {
		Args4J args4j = new Args4J();
		CmdLineParser parser = new CmdLineParser(args4j);
		parser.parseArgument(args);
		if ( args.length == 0 ) {
			parser.printUsage(System.out);
			return;
		}
		
		if ( args4j.naming ) {
			NamingServer.main(null);
			
		} else if ( args4j.storage ) {
			StorageServer.main(null);
			
		} else if ( args4j.client ) {
		}
	}
}

