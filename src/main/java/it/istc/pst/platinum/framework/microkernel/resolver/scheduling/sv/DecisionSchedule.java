package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.sv;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public final class DecisionSchedule extends FlawSolution 
{
	private List<Decision> schedule;		// computed solution of the peak
	
	/**
	 * 
	 * @param peak
	 * @param schedule
	 */
	protected DecisionSchedule(Peak peak, List<Decision> schedule) {
		super(peak);
		this.schedule= new ArrayList<>(schedule);
	}
	
	/**
	 * 
	 */
	@Override
	public double getCost() {
		// get property file 
		FilePropertyReader property = FilePropertyReader.getDeliberativePropertyFile();
		// read property
		String cost = property.getProperty("scheduling-cost");
		// parse and get double value
		return Double.parseDouble(cost);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getSchedule() {
		return new ArrayList<>(this.schedule);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[DecisionSchedule schedule= " + this.schedule + "]";
	}
	
}
