package it.cnr.istc.pst.platinum.ai.executive.pdb;

import java.lang.reflect.Constructor;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacadeBuilder;
import it.cnr.istc.pst.platinum.ai.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutivePlanDataBaseBuilder 
{
	/**
	 * 
	 * @param origin
	 * @param horizon
	 * @return
	 */
	public synchronized static ExecutivePlanDataBase createAndSet(long origin, long horizon) {
		// create default plan database
		return ExecutivePlanDataBaseBuilder.createAndSet(ExecutivePlanDataBase.class, origin, horizon);
	}
	
	/**
	 * 
	 * @param pdbClass
	 * @param origin
	 * @param horizon
	 * @return
	 */
	public synchronized static <T extends ExecutivePlanDataBase> T createAndSet(Class<T> pdbClass, long origin, long horizon)
	{
		// initialize plan database instance
		T pdb = ExecutivePlanDataBaseBuilder.doCreatePlanDataBase(pdbClass);
		try
		{
			// create temporal facade 
			TemporalFacade facade = TemporalFacadeBuilder.createAndSet(pdb, origin, horizon);
			// inject facade reference
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(pdb, TemporalFacadePlaceholder.class, facade);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating temporal facade into executive plan database:\n- message: " + ex.getMessage() + "\n");
		}
		
		try
		{
			// finalize construction
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(pdb, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on executive plan database from class " + pdbClass.getName()  +"\n:- message:" + ex.getMessage() + "\n");
		}
		
		// get plan database instance
		return pdb;
	}
	
	/**
	 * 
	 * @return
	 */
	private static synchronized <T extends ExecutivePlanDataBase> T doCreatePlanDataBase(Class<T> pdbClass) {
		// instance
		T pdb = null;
		try
		{
			// get constructor
			Constructor<T> c = (Constructor<T>) pdbClass.getDeclaredConstructor();
			// set accessible
			c.setAccessible(true);
			// create instance
			pdb = c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating executive plan database instance:\n- message: " + ex.getMessage() + "\n");
		}
		
		// get instance
		return pdb;
	}
}
