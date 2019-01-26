package it.istc.pst.platinum.framework.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author anacleto
 *
 */
public class FilePropertyReader 
{
	private String name;
	private Properties properties;
	
	/**
	 * 
	 * @param name
	 */
	private FilePropertyReader(String path)
	{
		try {
			// setup file property
			this.properties = new Properties();
			this.properties.load(new FileInputStream(new File(path)));
		}
		catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPropertyFileName() {
		return name;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}
	
	/**
	 * 
	 * @return
	 */
	public static final FilePropertyReader getDeliberativePropertyFile() {
		return new FilePropertyReader("etc/deliberative.properties");
	}
	
	/**
	 * 
	 * @return
	 */
	public static final FilePropertyReader getExecutivePropertyFile() {
		return new FilePropertyReader("etc/executive.properties");
	}
}
