package it.istc.pst.platinum.framework.utils.properties;

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
	public FilePropertyReader(String name)
	{
		try 
		{
			// setup file property
			this.properties = new Properties();
			// load file properties
			try (FileInputStream in = new FileInputStream(name)) {
				// load the (resource) property file
				this.properties.load(in);
			}
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
		return new FilePropertyReader("etc/platinum/deliberative.properties");
	}
	
	/**
	 * 
	 * @return
	 */
	public static final FilePropertyReader getExecutivePropertyFile() {
		return new FilePropertyReader("etc/platinum/executive.properties");
	}
}
