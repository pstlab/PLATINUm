package it.uniroma3.epsl2.framework.time.tn;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkFactory;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalNetworkFactory extends ApplicationFrameworkFactory {

	/**
	 * 
	 */
	public TemporalNetworkFactory() {
		super();
	}
	
	/**
	 * 
	 * @param registryKey
	 * @param type
	 * @param origin
	 * @param horizon
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T extends TemporalNetwork> T createSingleton(TemporalNetworkType type, long origin, long horizon) 
	{
		// temporal network
		T tn = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getNetworkClassName());
			// get constructor with required parameters
			Constructor<T> c = clazz.getDeclaredConstructor(Long.TYPE, Long.TYPE);
			// set accessible
			c.setAccessible(true);
			// create temporal network instance
			tn = c.newInstance(origin, horizon);
			
			// inject logger
			this.injectFrameworkLoggerReference(tn);
			// complete initialization
			this.completeApplicationObjectInitialization(tn);
			// registry the network as singleton object
			this.register(SINGLETON_TEMPORAL_NETWORK_REFERENCE, tn);
		}
		catch (SecurityException | NoSuchMethodException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get network
		return tn;
	}
	
	/**
	 * 
	 * @param registryKey
	 * @param type
	 * @param origin
	 * @param horizon
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T extends TemporalNetwork> T create(TemporalNetworkType type, long origin, long horizon) 
	{
		// temporal network
		T tn = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getNetworkClassName());
			// get constructor with required parameters
			Constructor<T> c = clazz.getDeclaredConstructor(Long.TYPE, Long.TYPE);
			// set accessible
			c.setAccessible(true);
			// create temporal network instance
			tn = c.newInstance(origin, horizon);
			
			// complete initialization
			this.completeApplicationObjectInitialization(tn);
			// registry the network as singleton object
			this.register(tn);
		}
		catch (SecurityException | NoSuchMethodException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get network
		return tn;
	}
}
