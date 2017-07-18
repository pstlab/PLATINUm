package it.istc.pst.platinum.framework.microkernel.lang.plan.relations.temporal;

import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintFactory;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.BeforeIntervalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class BeforeRelation extends TemporalRelation 
{
	private long[] bound;
	
	private TemporalConstraintFactory factory;
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected BeforeRelation(Decision reference, Decision target) {
		super(RelationType.BEFORE, reference, target);
		this.bound = new long[] {0, Long.MAX_VALUE - 1};
		// get factory
		this.factory = TemporalConstraintFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@Override
	public long[][] getBounds() {
		return new long[][] {this.bound};
	}
	
	/**
	 * 
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.bound = bounds[0];
	}
	
	/**
	 * 
	 * @param bound
	 */
	public void setBound(long[] bound) {
		this.bound = bound;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getLowerBound() {
		return this.bound[0];
	}
	
	/**
	 * 
	 */
	public long getUpperBound() {
		return this.bound[1];
	}

	/**
	 * 
	 * @return
	 */
	public TemporalConstraintType getConstraintType() {
		return TemporalConstraintType.BEFORE;
	}
	
	/**
	 * 
	 */
	@Override
	public BeforeIntervalConstraint create() {
		// create constraint
		BeforeIntervalConstraint c = this.factory.create(
				TemporalConstraintType.BEFORE);

		// set intervals
		c.setReference(this.reference.getToken().getInterval());
		c.setTarget(this.target.getToken().getInterval());
		// set bounds
		c.setLowerBound(this.bound[0]);
		c.setUpperBound(this.bound[1]);
		// set constraint
		this.constraint = c;
		// get constraint
		return c;
	}
}
