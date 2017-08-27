package it.istc.pst.platinum.framework.microkernel.lang.relations.temporal;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintFactory;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.EqualsIntervalConstraint;

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
