package it.cnr.istc.pst.platinum.ai.framework.domain.component.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ResourceProfile <S extends ResourceProfileSample> 
{
	private static AtomicLong ID_COUNTER = new AtomicLong(0);
	protected long id;
	protected List<S> samples;
	
	/**
	 * 
	 * @param resource
	 */
	public ResourceProfile() {
		// set id 
		this.id = ID_COUNTER.getAndIncrement();
		// initialize samples
		this.samples = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<S> getSamples() {
		// list of samples
		List<S> list = new ArrayList<>(this.samples);
		// sort the list of samples
		Collections.sort(list);
		// get sorted list of samples
		return list;
	}
	
	/**
	 * Add a sample to the profile
	 * 
	 * @param sample
	 */
	public void addSampel(S sample) {
		this.samples.add(sample);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.samples.isEmpty();
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		return true;
	}
}
