package it.istc.pst.platinum.framework.parameter.lang.constraints;

import java.lang.reflect.Constructor;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterConstraintFactory 
{
	private static ParameterConstraintFactory INSTANCE = null;
	
	/**
	 * 
	 */
	private ParameterConstraintFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static ParameterConstraintFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ParameterConstraintFactory();
		}
		return INSTANCE;
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
