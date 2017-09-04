package it.istc.pst.platinum.framework.domain.knowledge;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkFactory;

/**
 * 
 * @author anacleto
 *
 */
public class DomainKnowledgeFactory extends ApplicationFrameworkFactory 
{
	/**
	 * 
	 */
	public DomainKnowledgeFactory() {
		super();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends DomainKnowledge> T create(String key, DomainKnowledgeType type) 
	{
		T knowledge = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getDomainKnowledgeClassName());
			// check whether constructor configuration annotation is present
			Constructor<T> c = clazz.getDeclaredConstructor();
			// constructor
			c.setAccessible(true);
			// create instance
			knowledge = c.newInstance();

			// inject logger
			this.injectFrameworkLogger(knowledge);
			// inject plan database
			this.injectPlanDataBase(knowledge);
			
			// complete initialization
			this.doCompleteApplicationObjectInitialization(knowledge);
			// add entry to registry
			this.register(key, knowledge);
		} 
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get component
		return knowledge;
	}
 }
