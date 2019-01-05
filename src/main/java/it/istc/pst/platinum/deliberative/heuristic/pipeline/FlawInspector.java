package it.istc.pst.platinum.deliberative.heuristic.pipeline;

import java.util.Collection;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.DeliberativeObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.PlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FlawInspector extends DeliberativeObject 
{
	@PlanDataBasePlaceholder
	protected PlanDataBase pdb;
	
	private String label;
	
	/**
	 * 
	 * @param type
	 */
	protected FlawInspector(String label) {
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return this.label;
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
	 * @throws UnsolvableFlawException
	 */
	public abstract Set<Flaw> detectFlaws() 
			throws UnsolvableFlawException;
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[FlawFilter label= " +  this.label + "]";
	}
}
