package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.pcp;

import it.uniroma3.epsl2.framework.lang.plan.resource.ProfileSample;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceProfile;

/**
 * 
 * @author anacleto
 *
 */
public class OptimisticResourceProfile extends ResourceProfile {

	/**
	 * 
	 */
	protected OptimisticResourceProfile() {
		super();
	}
	
	/**
	 * 
	 */
	@Override
	public void sample(ResourceEvent event) 
	{
		// create sample
		ProfileSample sample; 
		// check event type
		switch (event.getType())
		{
			case CONSUMPTION : {
				// create sample
				sample = this.createSample(event, event.getEvent().getUpperBound());
				// add sample
				this.samples.add(sample);
			}
			break;
			
			case PRODUCTION : {
				// create sample
				sample = this.createSample(event, event.getEvent().getLowerBound());
				// add sample
				this.samples.add(sample);
			}
			break;
		}
	}
}
