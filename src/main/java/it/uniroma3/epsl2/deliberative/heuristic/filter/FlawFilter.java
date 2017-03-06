package it.uniroma3.epsl2.deliberative.heuristic.filter;

import java.util.Collection;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FlawFilter extends ApplicationFrameworkObject 
{
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	@PlanDataBaseReference
	protected PlanDataBase pdb;
	
	private FlawFilterType type;
	
	/**
	 * 
	 * @param type
	 */
	protected FlawFilter(FlawFilterType type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawFilterType getType() {
		return this.type;
	}
	
	/**
	 * The filter applies the encapsulated criteria to an already 
	 * computed set of flaws
	 * 
	 * @param flaws
	 * @return
	 */
	public abstract Set<Flaw> filter(Collection<Flaw> flaws);
	
	/**
	 * The filter applies the encapsulated criteria to the flaws
	 * detected by directly querying the plan data-base
	 * 
	 * @return
	 * @throws UnsolvableFlawFoundException
	 */
	public abstract Set<Flaw> filter() 
			throws UnsolvableFlawFoundException;
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[FlawFilter type= " +  this.type + "]";
	}
}
