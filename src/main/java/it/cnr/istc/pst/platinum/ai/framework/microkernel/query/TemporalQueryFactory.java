package it.cnr.istc.pst.platinum.ai.framework.microkernel.query;

import java.lang.reflect.Constructor;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalQueryFactory 
{
	/**
	 * 
	 */
	public TemporalQueryFactory() {}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T extends TemporalQuery> T create(TemporalQueryType type) 
	{
		// query instance
		T query = null;
		try 
		{
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
