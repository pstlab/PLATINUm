package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.pcp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.resource.ResourceProfileManager;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.resource.ProfileSample;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEventType;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceProfile;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentReference;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceSchedulingResolver <T extends DomainComponent & ResourceProfileManager> extends Resolver 
{ 
	@ComponentReference
	protected T component;

	/**
	 * 
	 */
	protected ResourceSchedulingResolver() {
		super(ResolverType.RESOURCE_SCHEDULING_RESOLVER);
	}

	@Override
	protected void doRetract(FlawSolution solution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// get pessimistic resource profile 
		List<ResourceEvent> events = this.component.getConsumptions();
		// get optimistic resource profile
		events.addAll(this.component.getProductions());
		
		// get optimistic resource profile
		ResourceProfile orp = this.computeOptimisticResourceProfile(events);
		// compute flaws
		List<Flaw> flaws = this.computeProfilePeaks(orp);
		
		// check results
		if (flaws.isEmpty()) {
			// get pessimistic resource profile
			ResourceProfile prp = this.computePessimisticResourceProfile(events);
			// compute flaws
			flaws = this.computeProfilePeaks(prp);
		}
		
		// get computed flaws
		return flaws;
	}

	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException 
	{
		// cast flaw
		ResourceProfileFlaw profileFlaw = (ResourceProfileFlaw) flaw;
		// compute all possible orderings of the tokens
		List<List<Decision>> orderings = this.schedule(profileFlaw.getPeak());
		// compute solutions
		for (List<Decision> ordering : orderings) 
		{
			// add a possible 
			FlawSolution solution = new PrecedenceConstraintPosting(profileFlaw, ordering);
			profileFlaw.addSolution(solution);
		}
		
		// check available solutions
		if (!profileFlaw.isSolvable()) {
			// throw exception
			throw new UnsolvableFlawFoundException("Unsolvable Peak found on state variable " + this.component.getName() + ":\n" + flaw);
		}
		
	}
	
	/**
	 * 
	 * @param events
	 * @return
	 */
	private ResourceProfile computeOptimisticResourceProfile(List<ResourceEvent> events) 
	{
		// create resource profile
		ResourceProfile orp = new OptimisticResourceProfile();
		for (ResourceEvent event : events) {
			// add event to profile
			orp.sample(event);
		}
		
		// get profile
		return orp;
	}
	
	/**
	 * 
	 * @param events
	 * @return
	 */
	private ResourceProfile computePessimisticResourceProfile(List<ResourceEvent> events) {
		// create resource profile
		ResourceProfile prp = new PessimisticResourceProfile();
		for (ResourceEvent event : events) {
			// add event to profile
			prp.sample(event);
		}
		// get profile
		return prp;
	}
	
	/**
	 * 
	 * @param initialCapacity
	 * @param profile
	 * @return
	 */
	private List<Flaw> computeProfilePeaks(ResourceProfile profile)
	{
		// initialize resource capacity level
		long currentLevel = this.component.getInitialCapacity();
		// peak flag
		boolean isPeak = false;
		// prepare a peak
		ResourceProfileFlaw peak = new ResourceProfileFlaw(this.component);
		
		// list of flaws
		List<Flaw> flaws = new ArrayList<>();
		// check profile samples
		for (ProfileSample sample : profile.getProfileSamples()) 
		{
			// get event
			ResourceEvent event = sample.getEvent();
			// add event to peak 
			peak.addEvent(event);
			
			// update resource level
			currentLevel = event.getType().equals(ResourceEventType.PRODUCTION) ? 
					(currentLevel + event.getAmount())	:		// increase resource level 
					(currentLevel - event.getAmount());			// decrease resource level
			
			// check peak flag
			if (!isPeak) 
			{
				// check if entering peak modality
				isPeak = currentLevel < this.component.getMinCapacity() || 
						currentLevel > this.component.getMaxCapacity();
						
				// if not peak check if flaws can be discarded
				if (!isPeak && currentLevel == this.component.getInitialCapacity()) {
					// reset peak
					peak = new ResourceProfileFlaw(this.component);
				}
			}
			else if (isPeak)
			{
				// check current level of resource
				if (currentLevel >= this.component.getMinCapacity() && 
						currentLevel <= this.component.getMaxCapacity())
				{
					// a peak found
					flaws.add(peak);
					// reset new peak
					peak = new ResourceProfileFlaw(this.component);
					isPeak = false;
				}
			}
		}
		
		
		// check pending peak
		if (isPeak) {
			// add peak
			flaws.add(peak);
		}
		
		// get flaws
		return flaws;
	}
	
	/**
	 * 
	 */
	private List<List<Decision>> schedule(Collection<Decision> decisions) 
	{
		// initialize permutations
		List<List<Decision>> result = new ArrayList<>();
		// compute permutations
		this.computePermutations(decisions, new ArrayList<Decision>(), result);
		// get permutations
		return result;
	}
	
	/**
	 * 
	 * @param decisions
	 * @param current
	 * @param result
	 */
	private void computePermutations(Collection<Decision> decisions, List<Decision> current, List<List<Decision>> result) 
	{
		// base step
		if (current.size() == decisions.size()) {
			result.add(current);
		}
		else {
			// recursive step
			for (Decision dec : decisions) {
				if (!current.contains(dec)) {
					// create a new current list
					List<Decision> list = new ArrayList<>(current);
					list.add(dec);
					// recursive call
					this.computePermutations(decisions, list, result);
				}
			}
		}
	}
}
