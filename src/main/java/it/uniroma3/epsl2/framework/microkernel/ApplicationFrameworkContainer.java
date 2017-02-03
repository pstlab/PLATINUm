package it.uniroma3.epsl2.framework.microkernel;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author anacleto
 *
 */
public final class ApplicationFrameworkContainer 
{
	private static ApplicationFrameworkContainer CONTAINER = null;
	private static int counter = 0;
	private Map<String, ApplicationFrameworkObject> registry;
	
	/**
	 * 
	 */
	private ApplicationFrameworkContainer() {
		this.registry = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	protected static ApplicationFrameworkContainer getInstance() {
		if (CONTAINER == null) {
			CONTAINER = new ApplicationFrameworkContainer();
		}
		return CONTAINER;
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public <T extends ApplicationFrameworkObject> void save(T obj) {
		// generate key
		String key = generateKey();
		// set key
		obj.setRegistryKey(key);
		// add registry entry
		this.registry.put(key, obj);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public <T extends ApplicationFrameworkObject> void save(String key, T obj) {
		// set key
		obj.setRegistryKey(key);
		// add registry entry
		this.registry.put(key, obj);
	}
	
	/**
	 * 
	 * @param obj
	 */
	public <T extends ApplicationFrameworkObject> void cancel(T obj) {
		// get key
		String key = obj.getRegistryKey();
		if (this.registry.containsKey(key)) {
			this.registry.remove(key);
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public ApplicationFrameworkObject lookup(String key) 
	{
		// check registry
		if (!this.registry.containsKey(key)) {
			throw new RuntimeException("Application object with key \"" + key +"\" not found in registry");
		}
		// get application object
		return this.registry.get(key);
	}
	
	/**
	 * 
	 * @return
	 */
	private static synchronized String generateKey() {
		return "instance://application/framework/object" + (counter++);
	}
}
