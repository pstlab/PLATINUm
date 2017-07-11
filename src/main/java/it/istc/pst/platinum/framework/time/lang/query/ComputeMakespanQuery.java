package it.istc.pst.platinum.framework.time.lang.query;

import java.util.HashSet;
import java.util.Set;

import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public final class ComputeMakespanQuery extends TemporalIntervalQuery 
{
	private Set<TemporalInterval> subset;
	private double makespan;
	
	/**
	 * 
	 */
	protected ComputeMakespanQuery() {
		super(TemporalQueryType.COMPUTE_MAKESPAN);
		this.makespan = Double.MAX_VALUE - 1;
		this.subset = new HashSet<>();
	}
	
	/**
	 * 
	 * @param subset
	 */
	public void setSubset(Set<TemporalInterval> subset) {
		this.subset = subset;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<TemporalInterval> getSubset() {
		return subset;
	}
	
	/**
	 * 
	 * @param makespan
	 */
	public void setMakespan(double makespan) {
		this.makespan = makespan;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMakespan() {
		return makespan;
	}
}
