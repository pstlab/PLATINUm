package it.istc.pst.platinum.framework.time.lang;

import java.lang.reflect.Constructor;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalConstraintFactory 
{
	private static TemporalConstraintFactory INSTANCE = null;
	
	/*
	 * Create Allen's language factory
	 */
	private TemporalConstraintFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static TemporalConstraintFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TemporalConstraintFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param clazz
	 * @param reference
	 * @param target
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemporalConstraint> T create(TemporalConstraintType type) { 
		// constraint to create
		T constraint = null;
		try {
			// get class from name
			Class<T> clazz = (Class<T>) Class.forName(type.getConstraintClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			// create instance
			constraint = c.newInstance();
		}
		catch(Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get created instance
		return constraint;
	}
}