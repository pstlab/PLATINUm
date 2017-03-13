package it.uniroma3.epsl2.framework.time.lang.query;

import java.util.HashSet;
import java.util.Set;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.TemporalInterval;

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
		super(TemporalQueryType.INTERVAL_SCHEDULE);
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
