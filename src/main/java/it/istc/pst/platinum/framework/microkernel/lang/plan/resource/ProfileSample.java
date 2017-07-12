package it.istc.pst.platinum.framework.microkernel.lang.plan.resource;

/**
 * 
 * @author anacleto
 *
 */
public class ProfileSample implements Comparable<ProfileSample> 
{
	private ResourceEvent event;
	private long time;
	
	/**
	 * 
	 * @param event
	 * @param time
	 */
	protected ProfileSample(ResourceEvent event, long time) {
		this.event = event;
		this.time = time;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResourceEvent getEvent() {
		return event;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ProfileSample o) {
		return this.time <= o.time ? -1 : 1;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
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
		ProfileSample other = (ProfileSample) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ResourceProfileSample time= " + this.time + " type= " + this.event.getType() + "(" + this.event.getAmount() + ")]";
	}
}
