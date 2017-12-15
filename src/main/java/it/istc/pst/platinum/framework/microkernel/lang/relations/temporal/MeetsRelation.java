package it.istc.pst.platinum.framework.microkernel.lang.relations.temporal;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintFactory;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.MeetsIntervalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class MeetsRelation extends TemporalRelation 
{
	private long[] bound;
	
	private TemporalConstraintFactory factory;
	
	/**
	 * 
	 * @param id
	 * @param reference
	 * @param target
	 */
	protected MeetsRelation(int id, Decision reference, Decision target) {
		super(id, RelationType.MEETS, reference, target);
		this.bound = new long[] {0, 0};
		// get factory
		this.factory = new TemporalConstraintFactory();
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
	public long getDistanceUpperBound() {
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
		return TemporalConstraintType.MEETS;
	}
	
	/**
	 * 
	 */
	@Override
	public MeetsIntervalConstraint create() {
		// create constraint
		MeetsIntervalConstraint c = this.factory.create(
				TemporalConstraintType.MEETS);

		// set intervals
		c.setReference(this.reference.getToken().getInterval());
		c.setTarget(this.target.getToken().getInterval());
		
		// set constraint
		this.constraint = c;
		// get constraint
		return c;
	}
}
