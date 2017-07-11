package it.istc.pst.platinum.framework.microkernel.lang.plan.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceProfile 
{
	protected List<ProfileSample> samples;
	
	/**
	 * 
	 */
	public ResourceProfile() {
		this.samples = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ProfileSample> getProfileSamples() {
		// sort samples
		List<ProfileSample> list = new ArrayList<>(this.samples);
		Collections.sort(list);
		return list;
	}
	
	/**
	 * 
	 * @param event
	 * @param time
	 */
	public void addSample(ResourceEvent event, long time) {
		// create sample
		ProfileSample sample = new ProfileSample(event, time);
		// add sample
		this.samples.add(sample);
	}
}
