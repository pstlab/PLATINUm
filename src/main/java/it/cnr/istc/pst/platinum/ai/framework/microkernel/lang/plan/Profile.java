package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.Resource;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEvent;

/**
 * 
 * @author alessandro
 *
 */
public class Profile 
{
	private Resource resource;
	
	/**
	 * 
	 * @param res
	 */
	protected Profile(Resource res) {
		// set resource
		this.resource = res;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.resource.getName();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getMinCapacity() {
		return this.resource.getMinCapacity();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getMaxCapacity() {
		return this.resource.getMaxCapacity();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getInitCapacity() {
		return this.resource.getInitialLevel();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ResourceEvent<?>> getResourceEvents() {
		// get resource events
		return new ArrayList<>(this.resource.getEvents());
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
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
		Profile other = (Profile) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.resource.toString();
	}
	
}
