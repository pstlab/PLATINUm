package it.uniroma3.epsl2.framework.parameter.lang;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicInteger;

import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterFactory 
{
	private static ParameterFactory INSTANCE = null;
	private AtomicInteger anonymParamCounter;
	
	/**
	 * 
	 */
	private ParameterFactory() {
		 this.anonymParamCounter = new AtomicInteger(0);
	}

	/**
	 * 
	 * @return
	 */
	public static ParameterFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ParameterFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ParameterDomain> T createParameterDomain(String name, ParameterDomainType type) {
		// parameter to create
		T dom = null;
		try {
			// get parameter class
			Class<T> clazz = (Class<T>) Class.forName(type.getParameterDomainClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor(String.class);
			c.setAccessible(true);
			dom = c.newInstance(name);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get create parameter
		return dom;
	}
	
	/**
	 * 
	 * @param label
	 * @param domain
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Parameter> T createParameter(String label, ParameterType type, ParameterDomain domain) {
		// parameter to create
		T param = null;
		try {
			// get parameter class
			Class<T> clazz = (Class<T>) Class.forName(type.getParameterClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor(
					String.class, 
					Class.forName(domain.getType().getParameterDomainClassName()));
			// set accessible
			c.setAccessible(true);
			param = c.newInstance(label, domain);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get create parameter
		return param;
	}
	
	/**
	 * 
	 * @param domain
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Parameter> T createAnonymousParameter(ParameterType type, ParameterDomain domain) {
		// parameter to create
		T param = null;
		try {
			// get parameter class
			Class<T> clazz = (Class<T>) Class.forName(type.getParameterClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor(
					String.class, 
					Class.forName(domain.getType().getParameterDomainClassName()));
			// set accessible
			c.setAccessible(true);
			int id = this.anonymParamCounter.getAndIncrement();
			param = c.newInstance("anonym-" + id, domain);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get create parameter
		return param;
	} 
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ParameterConstraint> T createParameterConstraint(ParameterConstraintType type) {
		// the parameter constraint
		T cons = null;
		try {
			// get parameter constraint class
			Class<T> clazz = (Class<T>) Class.forName(type.getConstraintClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			cons = c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get parameter constraint
		return cons;
	}
}
