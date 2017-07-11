package it.istc.pst.platinum.framework.microkernel.query;

import java.lang.reflect.Constructor;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalQueryFactory 
{
	private static TemporalQueryFactory INSTANCE = null;
	
	private TemporalQueryFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static TemporalQueryFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TemporalQueryFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T extends TemporalQuery> T create(TemporalQueryType type) {
		// query instance
		T query = null;
		try {
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getQueryClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			// create instance
			query = c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get created instance
		return query;
	}
}
