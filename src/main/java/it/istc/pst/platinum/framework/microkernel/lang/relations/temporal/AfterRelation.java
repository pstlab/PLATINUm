package it.istc.pst.platinum.framework.microkernel.lang.relations.temporal;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintFactory;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.AfterIntervalConstraint;

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
	protected AfterRelation(int id, Decision reference, Decision target) {
		super(id, RelationType.AFTER, reference, target);
		this.bound = new long[] {0, Long.MAX_VALUE - 1};
		// get factory
		this.factory = new TemporalConstraintFactory();
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
