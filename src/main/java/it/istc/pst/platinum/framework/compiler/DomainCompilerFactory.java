package it.istc.pst.platinum.framework.compiler;

import java.lang.reflect.Constructor;

/**
 * 
 * @author anacleto
 *
 */
public class DomainCompilerFactory {

	private static DomainCompilerFactory INSTANCE = null;
	
	private DomainCompilerFactory() {}
	
	/**
	 * 
	 * @return
	 */
	public static DomainCompilerFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DomainCompilerFactory();
		}
		return INSTANCE;
	}
	
	/**
	 * 
	 * @param type
	 * @param ddlFile
	 * @param pdlFile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends DomainCompiler> T create(DomainCompilerType type, String ddlFile, String pdlFile) {
		T compiler = null;
		try {
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getCompilerClassName());
			Constructor<T> c = clazz.getDeclaredConstructor(String.class, String.class);
			c.setAccessible(true);
			compiler = c.newInstance(ddlFile, pdlFile);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		return compiler;
	}
	
	/**
	 * 
	 * @param type
	 * @param ddlFile
	 * @param pdlFile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends DomainCompiler> T create(DomainCompilerType type, String ddlFile) {
		T compiler = null;
		try {
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getCompilerClassName());
			Constructor<T> c = clazz.getDeclaredConstructor(String.class);
			c.setAccessible(true);
			compiler = c.newInstance(ddlFile);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		return compiler;
	}
}
