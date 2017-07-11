package it.istc.pst.platinum.testing.framework.parameter.csp.v4;

import java.lang.reflect.Constructor;

import it.istc.pst.platinum.framework.parameter.ex.ParameterCreationException;
import it.istc.pst.platinum.framework.parameter.lang.Parameter;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * Factory for testing purposes
 * 
 * @author anacleto
 *
 */
class ParameterTestFactory {

	protected ParameterTestFactory() {}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public <T extends ParameterDomain> T createParameterDomain(String name, ParameterDomainType type) 
	{	
		// domain
		T domain = this.doCreateParameterDomain(name, type);
		// get created domain
		return domain;
	}
	
	/**
	 * 
	 * @param label
	 * @param type
	 * @param domain
	 * @return
	 * @throws ParameterCreationException
	 */
	public <T extends Parameter<?>> T createParameter(String label, ParameterDomain domain) 
			throws ParameterCreationException
	{
		// create parameter
		T param = this.doCreateParameter(label, domain);
		// get create parameter
		return param;
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T extends ParameterDomain> T doCreateParameterDomain(String name, ParameterDomainType type)
	{
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
	private <T extends Parameter<?>> T doCreateParameter(String label, ParameterDomain domain) 
	{
		// parameter to create
		T param = null;
		try {
			// get parameter class
			Class<T> clazz = (Class<T>) Class.forName(domain.getParameterType()
					.getParameterClassName());
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
