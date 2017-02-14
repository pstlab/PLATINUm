package it.uniroma3.epsl2.framework.lang.plan.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ResourceProfile 
{
	protected List<ProfileSample> samples;
	
	/**
	 * 
	 */
	protected ResourceProfile() {
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
	public abstract void sample(ResourceEvent event);
	
	/**
	 * 
	 * @param event
	 * @param time
	 * @return
	 */
	protected ProfileSample createSample(ResourceEvent event, long time) {
		ProfileSample sample = new ProfileSample(event, time);
		return sample;
	}
}
