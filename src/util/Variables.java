package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Record Variables
 * @author dhuang
 */
public class Variables {
	private static final Logger logger = LogManager.getLogger(Variables.class);
	private static Variables INSTANCE = null;
	public static Variables getInstance() {
		if ( INSTANCE == null ) INSTANCE = new Variables();
		return INSTANCE;
	}
	private Variables() {
		try {
			properties.load(new FileInputStream("META-INF/conf.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		logger.info(Variables.getInstance().getProperty("port"));
	}
	
	private final Properties properties = new Properties();
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
