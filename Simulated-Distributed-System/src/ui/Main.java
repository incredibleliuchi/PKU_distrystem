package ui;

import java.io.IOException;
import java.sql.SQLException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;


class Args4J {
//	@Option(name = "-index", usage = "Index Data.")
//	boolean doIndex;
//	
//	@Option(name = "-search", usage = "Do Default Search. ex. -search \"物语 花\"")
//	String searchQuery = null;
//	
//	@Option(name = "-serve", usage = "Run The Searching Server.")
//	boolean doServe;
}

public class Main {
	public static void main(String[] args) throws CmdLineException, IOException, InterruptedException, SQLException {
		Args4J args4j = new Args4J();
		CmdLineParser parser = new CmdLineParser(args4j);
		parser.parseArgument(args);
		if ( args.length == 0 ) {
			parser.printUsage(System.out);
			return;
		}
		
//		if ( args4j.doIndex ) {
//			Indexer.main(new String [0]);
//			
//		} else if ( args4j.searchQuery != null ) {
//			IndexReader reader = DirectoryReader.open(FSDirectory.open(
//					new File(Variables.getInstance().getProperties().getProperty("indexPath"))));
//			IndexSearcher searcher = new IndexSearcher(reader);
//			JSONObject result = Searcher.getInstance().search(searcher, 
//					QueryParser.getInstance().parseAsField(args4j.searchQuery, "name"), 0, 100);
//			System.out.println(result);
//			reader.close();
//			
//		} else if ( args4j.doServe ) {
//			new Thread(new TCPThreadServer()).run();
//			while ( true ) Thread.sleep(Long.MAX_VALUE);
//		}
	}
}

