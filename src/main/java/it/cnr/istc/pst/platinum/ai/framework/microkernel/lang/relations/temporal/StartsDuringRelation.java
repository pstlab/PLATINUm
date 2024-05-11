package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintFactory;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.StartsDuringIntervalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class StartsDuringRelation extends TemporalRelation 
{
	private long[] firstBound;
	private long[] secondBound;
	
	protected TemporalConstraintFactory factory;
	
	/**
	 * 
	 * @param id
	 * @param reference
	 * @param target
	 */
	protected StartsDuringRelation(int id, Decision reference, Decision target) {
		super(id, RelationType.STARTS_DURING, reference, target);
		this.firstBound = new long[] {0, Long.MAX_VALUE - 1};
		this.secondBound = new long[] {0, Long.MAX_VALUE - 1};
		// get factory
		this.factory = new TemporalConstraintFactory();
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public long[][] getBounds() {
		return new long[][] {
			this.firstBound,
			this.secondBound
		};
	}
	
	/**
	 * 
	 * @param bounds
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.firstBound = bounds[0];
		this.secondBound = bounds[1];
	}
	
	/**
	 * 
	 * @param bound
	 */
	public void setFirstBound(long[] bound) {
		this.firstBound = bound;
	}
	
	/**
	 * 
	 * @param bound
	 */
	public void setSecondBound(long[] bound) {
		this.secondBound = bound;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getFirstBound() {
		return this.firstBound;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getSecondBound() {
		return this.secondBound;
	}
	
	/**
	 * 
	 */
	@Override
	public TemporalConstraintType getConstraintType() {
		return TemporalConstraintType.STARTS_DURING;
	}
	
	/**
	 * 
	 */
	@Override
	public StartsDuringIntervalConstraint create() {
		// create constraint
		StartsDuringIntervalConstraint c = this.factory.create(
				TemporalConstraintType.STARTS_DURING);

		// set intervals
		c.setReference(this.reference.getToken().getInterval());
		c.setTarget(this.target.getToken().getInterval());
		// set bounds
		c.setFirstBound(this.firstBound);
		c.setSecondBound(this.secondBound);
		// set constraint
		this.constraint = c;
		// get constraint
		return c;
	}
}
