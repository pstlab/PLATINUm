package it.istc.pst.platinum.framework.domain.component;

import it.istc.pst.platinum.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public final class Token implements Comparable<Token> 
{
	private int id;
	protected DomainComponent component;	// component the token belongs to
	protected TemporalInterval interval;	// temporal interval
	protected Predicate predicate;			// predicate
	
	/**
	 * 
	 * @param component
	 * @param interval
	 * @param predicate
	 */
	protected Token(int id, DomainComponent component, TemporalInterval interval, Predicate predicate) {
		this.id = id;
		this.component = component;
		this.interval = interval;
		this.predicate = predicate;
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
	public TemporalInterval getInterval() {
		return interval;
	}
	
	/**
	 * 
	 * @return
	 */
	public Predicate getPredicate() {
		return predicate;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isControllable() {
		return this.interval.isControllable();
	}
	
	/**
	 * 
	 * @return
	 */
	public DomainComponent getComponent() {
		return component;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Token o) {
		// compare temporal intervals
		return this.interval.compareTo(o.interval);
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
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
		Token other = (Token) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Token " + this.id + ":" + this.predicate.getGroundSignature() + " "
				+ "start= [" + this.interval.getStartTime().getLowerBound() + "," + this.interval.getStartTime().getUpperBound() + "] "
				+ "end= [" + this.interval.getEndTime().getLowerBound() + "," + this.interval.getEndTime().getUpperBound() + "] "
				+ "duration= [" + this.interval.getDurationLowerBound() + "," + this.interval.getDurationUpperBound() + "]]";
	}
}
