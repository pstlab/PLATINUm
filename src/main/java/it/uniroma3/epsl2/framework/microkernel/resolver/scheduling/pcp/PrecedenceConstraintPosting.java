package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.pcp;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class PrecedenceConstraintPosting extends FlawSolution 
{
	private List<Decision> precedence;
	
	/**
	 * 
	 * @param flaw
	 * @param precedences
	 */
	protected PrecedenceConstraintPosting(ResourceProfileFlaw flaw, List<Decision> precedences) {
		super(flaw);
		this.precedence = new ArrayList<>(precedences);
	}

	/**
	 * 
	 */
	@Override
	public double getCost() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getPrecedences() {
		return new ArrayList<>(this.precedence);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[PrecedenceConstraintPosting\n- constraint= " + this.precedence + "\n]";
	}
}
