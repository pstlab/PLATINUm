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
			this.doCompleteApplicationObjectInitialization(tn);
			// registry the network into the container
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
