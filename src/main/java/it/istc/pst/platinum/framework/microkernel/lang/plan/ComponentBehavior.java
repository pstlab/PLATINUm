package it.istc.pst.platinum.framework.microkernel.lang.plan;

import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;

/**
 * 
 * @author anacleto
 *
 */
public class ComponentBehavior 
{
	private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);
	private int id;
	private ComponentValue value;
	private long[] end;
	private long[] duration;
	
	/**
	 * @param v
	 * @param end
	 * @param duration
	 */
	protected ComponentBehavior(ComponentValue v, long[] end, long[] duration) {
		this.id = ID_COUNTER.getAndIncrement();
		this.value = v;
		this.end = end;
		this.duration = duration;
	}
	
	public long[] getDuration() {
		return duration;
	}
	
	public long[] getEnd() {
		return end;
	}
	
	public ComponentValue getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentBehavior other = (ComponentBehavior) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[ComponentBehavior value: " + this.value.getLabel() + ", end: " + this.end + ", duration: " +  this.duration+ "]";
	}
}
