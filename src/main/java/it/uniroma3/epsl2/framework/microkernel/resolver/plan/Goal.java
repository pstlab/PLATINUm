package it.uniroma3.epsl2.framework.microkernel.resolver.plan;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class Goal extends Flaw {

	private Decision decision;
	
	/**
	 * 
	 * @param component
	 * @param decision
	 */
	protected Goal(DomainComponent component, Decision decision) {
		super(component, FlawType.PLAN_REFINEMENT);
		this.decision = decision;
	}
	
	/**
	 * 
	 */
	public void setMandatoryExpansion() {
		this.decision.setMandatoryExpansion();
	}
	
	/**
	 * 
	 */
	public void setMandatoryUnification() {
		this.decision.setMandatoryUnification();
	}
	
	/**
	 * 
	 */
	@Override
	public DomainComponent getComponent() {
		return this.decision.getComponent();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMandatoryExpansion() {
		return this.decision.isMandatoryExpansion();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMandatoryUnification() {
		return this.decision.isMandatoryUnification();
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getDecision() {
		return decision;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Goal decision= " + this.decision + "]";
	}
}
