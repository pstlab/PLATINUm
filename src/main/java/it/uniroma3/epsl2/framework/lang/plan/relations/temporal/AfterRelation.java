package it.uniroma3.epsl2.framework.lang.plan.relations.temporal;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintFactory;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.allen.AfterIntervalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class AfterRelation extends TemporalRelation 
{
	private long[] bound;
	
	protected TemporalConstraintFactory factory;
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected AfterRelation(Decision reference, Decision target) {
		super(RelationType.AFTER, reference, target);
		this.bound = new long[] {0, Long.MAX_VALUE - 1};
		// get factory
		this.factory = TemporalConstraintFactory.getInstance();
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public long[][] getBounds() {
		return new long[][] {this.bound};
	}
	
	/**
	 * 
	 * @param bounds
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
	public long getDistanceLowerBound() {
		return this.bound[0];
	}
	
	/**
	 * 
	 */
	public long getDistanceUpperBound() {
		return this.bound[1];
	}
	
	/**
	 * 
	 */
	@Override
	public TemporalConstraintType getConstraintType() {
		return TemporalConstraintType.AFTER;
	}
	
	/**
	 * 
	 */
	@Override
	public AfterIntervalConstraint create() {
		// create constraint
		AfterIntervalConstraint c = this.factory.create(
				TemporalConstraintType.AFTER);

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
