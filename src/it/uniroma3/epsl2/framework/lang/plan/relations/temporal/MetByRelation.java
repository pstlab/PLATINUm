package it.uniroma3.epsl2.framework.lang.plan.relations.temporal;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.time.lang.IntervalConstraintFactory;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.allen.MetByIntervalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class MetByRelation extends TemporalRelation 
{
	private IntervalConstraintFactory factory;
	private long[] bound;
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected MetByRelation(Decision reference, Decision target) {
		super(RelationType.MET_BY, reference, target);
		this.bound = new long[] {0, 0};
		// get factory
		this.factory = IntervalConstraintFactory.getInstance();
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public long[][] getBounds() {
		return new long[][] {{0, 0}};
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
	public long getDistasnceUpperBound() {
		return this.bound[1];
	}
	
	/**
	 * 
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.bound = new long[] {0, 0};
	}
	
	/**
	 * 
	 */
	@Override
	public TemporalConstraintType getConstraintType() {
		return TemporalConstraintType.MET_BY;
	}
	
	/**
	 * 
	 */
	@Override
	public MetByIntervalConstraint create() {
		// create constraint
		MetByIntervalConstraint c = this.factory.create(
				TemporalConstraintType.MET_BY);

		// set intervals
		c.setReference(this.reference.getToken().getInterval());
		c.setTarget(this.target.getToken().getInterval());
		
		// set constraint
		this.constraint = c;
		// get constraint
		return c;
	}
}
