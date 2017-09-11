package it.istc.pst.platinum.framework.utils.properties;

import java.io.IOException;
import java.net.URL;
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
		try {
			// setup file property
			this.properties = new Properties();
			// load file properties
			URL url = ClassLoader.getSystemResource(name);
			if (url != null) {
				// load the (resource) property file
				this.properties.load(url.openStream());
			}
			else {
				throw new RuntimeException("Property file \"" + name +  "\" not found...");
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
		return new FilePropertyReader("deliberative.properties");
	}
	
	/**
	 * 
	 * @return
	 */
	public static final FilePropertyReader getExecutivePropertyFile() {
		return new FilePropertyReader("executive.properties");
	}
}
