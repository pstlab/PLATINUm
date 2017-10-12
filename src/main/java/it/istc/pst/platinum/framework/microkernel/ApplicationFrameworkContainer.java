package it.istc.pst.platinum.framework.microkernel;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author anacleto
 *
 */
public final class ApplicationFrameworkContainer 
{
	public static final String FRAMEWORK_SINGLETON_DOMAIN_KNOWLEDGE = "/platinum/framework/domain/knowledge";
	public static final String FRAMEWORK_SINGLETON_PLANDATABASE = "/platinum/framework/pdb";
	public static final String FRAMEWORK_SINGLETON_PLANDATABASE_LOGGER = "/platinum/framework/pdb/logger";
	public static final String FRAMEWORK_SINGLETON_TEMPORAL_FACADE = "/platinum/framework/temporal/facade";
	public static final String FRAMEWORK_SINGLETON_PARAMETER_FACADE = "/platinum/framework/parameter/facade";
	public static final String FRAMEWORK_SINGLETON_DELIBERATIVE = "/platinum/deliberative";
	public static final String FRAMEWORK_SINGLETON_DELIBERATIVE_LOGGER = "/platinum/deliberative/logger";
	public static final String FRAMEWORK_SINGLETON_EXECUTIVE = "/platinum/executive";
	public static final String FRAMEWORK_SINGLETON_EXECUTIVE_LOGGER = "/platinum/executive/logger";
	
	
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
	 */
	public static void clearRegistry() {
		// check if instance exists
		if (CONTAINER != null) {
			// clear registry
			CONTAINER.registry.clear();
			// reset counter
			ApplicationFrameworkContainer.counter = 0;
		}
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
	public ApplicationFrameworkObject lookup(String key) {
		// get application object - null if no object is registered with the key
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
