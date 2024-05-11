package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintFactory;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.ContainsIntervalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class ContainsRelation extends TemporalRelation 
{
	private TemporalConstraintFactory factory;
	
	private long[] startTimeBound;
	private long[] endTimeBound;
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected ContainsRelation(int id, Decision reference, Decision target) {
		super(id, RelationType.CONTAINS, reference, target);
		this.startTimeBound = new long[] {0, Long.MAX_VALUE - 1};
		this.endTimeBound = new long[] {0, Long.MAX_VALUE - 1};
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
	 * @return
	 */
	public long[] getEndTimeBound() {
		return this.endTimeBound;
	}
	
	/**
	 * 
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.startTimeBound = bounds[0];
		this.endTimeBound = bounds[1];
	}
	
	/**
	 * 
	 */
	@Override
	public TemporalConstraintType getConstraintType() {
		return TemporalConstraintType.CONTAINS;
	}
	
	/**
	 * 
	 */
	@Override
	public ContainsIntervalConstraint create() {
		// create constraint
		ContainsIntervalConstraint c = this.factory.create(
				TemporalConstraintType.CONTAINS);

		// set intervals
		c.setReference(this.reference.getToken().getInterval());
		c.setTarget(this.target.getToken().getInterval());
		// set bounds
		c.setStartTimeBound(this.startTimeBound);
		c.setEndTimeBound(this.endTimeBound);
		// set constraint
		this.constraint = c;
		// get constraint
		return c;
	}
}
