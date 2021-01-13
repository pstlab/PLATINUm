package it.cnr.istc.pst.platinum.ai.deliberative.heuristic;

import java.lang.reflect.Constructor;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.deliberative.heuristic.pipeline.FlawInspector;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.PlanDataBase;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.NoFlawFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FlawSelectionHeuristic extends FrameworkObject 
{
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;
	
	private String label;
	
	/**
	 * 
	 * @param label
	 */
	protected FlawSelectionHeuristic(String label) {
		super();
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Return a set of equivalent flaws to solve for plan refinement. Each solution of a 
	 * flaw determines a branch in the resulting search space of the planner.
	 * 
	 * @return
	 * @throws UnsolvableFlawException
	 * @throws NoFlawFoundException
	 */
	public abstract Set<Flaw> choose() 
			throws UnsolvableFlawException, NoFlawFoundException; 
	
	/**
	 * Check existing flaws without computing possible solutions
	 * 
	 * @return
	 */
	public abstract Set<Flaw> check();
	
	/**
	 * Filter a set of already computed flaws according to a certain criterion
	 * 
	 * @param flaws
	 * @return
	 * @throws NoFlawFoundException
	 */
	public abstract Set<Flaw> filter(Set<Flaw> flaws) 
			throws NoFlawFoundException;
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"label\": \"" + this.label + "\" }";
	}
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	protected <T extends FlawInspector> T doCreateFlawFilter(String className) 
	{
		// create filter instance
		T filter = this.createFilter(className);
		try
		{
			// inject plan database reference
			FrameworkReflectionUtils.doInjectReferenceThroughAnnotation(filter, PlanDataBasePlaceholder.class, this.pdb);
		}
		catch (Exception ex) { 
			throw new RuntimeException("Error while injecting plan database reference into flaw filter:\n- filter class: " + className +"\n- message: " + ex.getMessage() + "\n");
		}
		
		try
		{
			// finalize filter construction
			FrameworkReflectionUtils.doInvokeMethodTaggedWithAnnotation(filter, PostConstruct.class);
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while calling post construct method on filter from class: " + className + "\n");
		}
		
		// get created filter class
		return filter;
	}
	
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	private <T extends FlawInspector> T createFilter(String className) {
		// flaw filter
		T filter = null;
		try
		{
			// get class
			Class<?> clazz = Class.forName(className);
			// get constructor
			@SuppressWarnings("unchecked")
			Constructor<T> c = (Constructor<T>) clazz.getDeclaredConstructor();
			// set accessibility
			c.setAccessible(true);
			// create instance
			filter = c.newInstance();
		}
		catch (Exception ex) {
			throw new RuntimeException("Error while creating flaw filter from class: " + className + "\n");
		}
		
		// get created filter
		return filter;
	}
}



