package it.istc.pst.platinum.framework.protocol.query;

import java.lang.reflect.Constructor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class ProtocolQueryFactory 
{
	private static ProtocolQueryFactory INSTANCE = null;
	
	/**
	 * 
	 */
	private ProtocolQueryFactory() {
		// private constructor
	}
	
	/**
	 * 
	 * @return
	 */
	public static ProtocolQueryFactory getSingletonInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ProtocolQueryFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param queryClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ProtocolQuery> T createQuery(ProtocolQueryType qtype) {
		T query;
		try {
			Class<? extends ProtocolQuery> queryClass = qtype.getQueryClass();
			Constructor<T> c = (Constructor<T>) queryClass.getDeclaredConstructor();
			c.setAccessible(true);
			query = c.newInstance();
		} 
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		return query;
	}
}
