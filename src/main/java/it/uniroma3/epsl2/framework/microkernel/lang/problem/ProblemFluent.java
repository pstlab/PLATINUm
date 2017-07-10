package it.uniroma3.epsl2.framework.microkernel.lang.problem;

import java.util.concurrent.atomic.AtomicInteger;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ProblemFluent 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	protected int id;
	protected ProblemFluentType type;
	protected ComponentValue value;
	protected String[] labels;
	protected long[] start;
	protected long[] end;
	protected long[] duration;
	
	/**
	 * 
	 * @param type
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param duration
	 */
	protected ProblemFluent(ProblemFluentType type, 
			ComponentValue value, String[] labels, 
			long[] start, long[] end, long[] duration) {
		this.id = ID_COUNTER.getAndIncrement();
		this.type = type;
		this.value = value;
		this.labels = labels;
		this.start = start;
		this.end = end;
		this.duration = duration;
	}
	
	/**
	 * 
	 * @return
	 */
	public ProblemFluentType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public ComponentValue getValue() {
		return value;
	}
	
	/**
	 * 
	 */
	public String[] getParameterLabels() {
		return this.labels;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getStart() {
		return start;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getEnd() {
		return end;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getDuration() {
		return duration;
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProblemFact other = (ProblemFact) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
