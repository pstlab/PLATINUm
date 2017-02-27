package it.uniroma3.epsl2.framework.lang.plan.relations.temporal;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintFactory;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.allen.DuringIntervalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class DuringRelation extends TemporalRelation 
{
	private long[] startTimeBound;
	private long[] endTimeBound;
	
	private TemporalConstraintFactory factory;
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected DuringRelation(Decision reference, Decision target) {
		super(RelationType.DURING, reference, target);
		this.startTimeBound = new long[] {0, Long.MAX_VALUE - 1};
		this.endTimeBound = new long[] {0, Long.MAX_VALUE - 1};
		// get factory
		this.factory = TemporalConstraintFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@Override
	public long[][] getBounds() {
		return new long[][] {
			this.startTimeBound,
			this.endTimeBound
		};
	}
	
	/**
	 * 
	 * @param bound
	 */
	public void setStartTimeBound(long[] bound) {
		this.startTimeBound = bound;
	}
	
	/**
	 * 
	 * @param bound
	 */
	public void setEndTimeBound(long[] bound) {
		this.endTimeBound = bound;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getStartTimeBound() {
		return this.startTimeBound;
	}
	
	/**
	 * 
	 * @param bounds
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.startTimeBound = bounds[0];
		this.endTimeBound = bounds[1];
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getEndTimeBound() {
		return this.endTimeBound;
	}
	
	/**
	 * 
	 */
	@Override
	public TemporalConstraintType getConstraintType() {
		return TemporalConstraintType.DURING;
	}
	
	/**
	 * 
	 */
	@Override
	public DuringIntervalConstraint create() {
		// create constraint
		DuringIntervalConstraint c = this.factory.create(
				TemporalConstraintType.DURING);

		// set intervals
		c.setReference(this.reference.getToken().getInterval());
		c.setTarget(this.target.getToken().getInterval());
		
		// set bounds
		c.setFirstBound(this.startTimeBound);
		c.setSecondBound(this.endTimeBound);
		// set constraint
		this.constraint = c;
		// get constraint
		return c;
	}
}
