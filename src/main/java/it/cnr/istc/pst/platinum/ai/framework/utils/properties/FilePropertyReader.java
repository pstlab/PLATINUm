package it.cnr.istc.pst.platinum.ai.framework.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author anacleto
 *
 */
public class FilePropertyReader 
{
	public static final String DEFAULT_DELIBERATIVE_PROPERTY = "etc/deliberative.properties";
	public static final String DEFAULT_EXECUTIVE_PROPERTY = "etc/executive.properties";
	public static final String DEFAULT_AGENT_PROPERTY = "etc/agent.properties";
	
	private String name;
	private Map<String, String> key2value;
	
	/**
	 * 
	 * @param path
	 */
	public FilePropertyReader(String path)
	{
		// set property data
		this.key2value = new HashMap<String, String>();
		try {
			// setup file property
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(path)));
			// read property file
			for (String key : properties.stringPropertyNames()) {
				// get property value
				String value = properties.getProperty(key);
				// add entry 
				this.key2value.put(key, value);
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
		return this.key2value.get(key);
	}
}
