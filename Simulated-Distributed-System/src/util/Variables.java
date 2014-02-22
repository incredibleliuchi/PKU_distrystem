package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Variables {
	private Variables() {
		try {
			properties.load(new FileInputStream("META-INF/conf.properties"));
//			properties.load(Variables.class.getClassLoader().
//					getResource("conf.properties").openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static Variables INSTANCE = null;
	public static Variables getInstance() {
		if ( INSTANCE == null ) INSTANCE = new Variables();
		return INSTANCE;
	}
	
	public static void main(String[] args) {
		Variables.getInstance();
		System.out.println(Variables.getInstance().getProperties().getProperty("port"));
	}
	
	private Properties properties = new Properties();
	
	public Properties getProperties() {
		return properties;
	}
}
