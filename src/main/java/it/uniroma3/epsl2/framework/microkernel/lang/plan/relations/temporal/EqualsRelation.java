package it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal;

import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintFactory;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.allen.EqualsIntervalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class EqualsRelation extends TemporalRelation 
{
	private TemporalConstraintFactory factory;
	private long[] startBound;
	private long[] endBound;
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected EqualsRelation(Decision reference, Decision target) {
		super(RelationType.EQUALS, reference, target);
		this.startBound = new long[] {0, 0};
		this.endBound = new long[] {0, 0};
		// get factory
		this.factory = TemporalConstraintFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@Override
	public long[][] getBounds() {
		return new long[][] {{0, 0}, {0, 0}};
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getStartTimeBound() {
		return this.startBound;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getEndTimeBound() {
		return this.endBound;
	}
	
	/**
	 * 
	 * @param bounds
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.startBound = new long[] {0, 0};
		this.endBound = new long[] {0, 0};
	}
	
	/**
	 * 
	 */
	@Override
	public TemporalConstraintType getConstraintType() {
		return TemporalConstraintType.EQUALS;
	}
	
	/**
	 * 
	 */
	@Override
	public EqualsIntervalConstraint create() {
		// create constraint
		EqualsIntervalConstraint c = this.factory.create(
				TemporalConstraintType.EQUALS);

		// set intervals
		c.setReference(this.reference.getToken().getInterval());
		c.setTarget(this.target.getToken().getInterval());
		
		// set constraint
		this.constraint = c;
		// get constraint
		return c;
	}
}
