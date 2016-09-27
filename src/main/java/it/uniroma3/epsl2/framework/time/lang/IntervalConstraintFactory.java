package it.uniroma3.epsl2.framework.time.lang;

import java.lang.reflect.Constructor;

/**
 * 
 * @author anacleto
 *
 */
public class IntervalConstraintFactory {

	private static IntervalConstraintFactory INSTANCE = null;
	
	/*
	 * Create Allen's language factory
	 */
	private IntervalConstraintFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static IntervalConstraintFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new IntervalConstraintFactory();
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
	public <T extends TemporalConstraint> T create(TemporalConstraintType type) {	//, TemporalInterval reference, TemporalInterval target) 
		// constraint to create
		T constraint = null;
		try {
			// get class from name
			Class<T> clazz = (Class<T>) Class.forName(type.getConstraintClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();	//TemporalInterval.class, TemporalInterval.class);
			c.setAccessible(true);
			// create instance
			constraint = c.newInstance();	//	reference, target);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get created instance
		return constraint;
	}
}
