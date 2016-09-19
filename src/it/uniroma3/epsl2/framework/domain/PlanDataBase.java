package it.uniroma3.epsl2.framework.domain;

import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationRule;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.lang.ex.DomainComponentNotFoundException;
import it.uniroma3.epsl2.framework.lang.ex.NoFlawFoundException;
import it.uniroma3.epsl2.framework.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.lang.ex.ProblemInitializationException;
import it.uniroma3.epsl2.framework.lang.ex.SynchronizationCycleException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Agenda;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.lang.problem.Problem;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;

/**
 * 
 * @author anacleto
 *
 */
public interface PlanDataBase 
{
	/**
	 * 
	 * @param observer
	 */
	public void subscribe(PlanDataBaseObserver observer);
	
	/**
	 * 
	 * @return
	 */
	public long getOrigin();
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon();
	
	/**
	 * 
	 * @param problem
	 * @throws ProblemInitializationException
	 */
	public void setup(Problem problem) 
			throws ProblemInitializationException;
	
	/**
	 * 
	 */
	public void clear();
	
	/**
	 * 
	 * @return
	 */
	public SolutionPlan getSolutionPlan();
	
	/**
	 * 
	 * @return
	 */
	public Agenda getAgenda();
	
	/**
	 * 
	 * @return
	 */
	public Plan getPlan();
	
	/**
	 * 
	 * @return
	 */
	public List<DomainComponent> getComponents();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public DomainComponent getComponentByName(String name);
	
	/**
	 *  Check consistency of the plan data-base
	 *  
	 * @throws ConsistencyCheckException
	 */
	public void check() 
			throws ConsistencyCheckException;
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public <T extends DomainComponent> T createDomainComponent(String name, DomainComponentType type);
	
	/**
	 * 
	 * @param component
	 */
	public void addDomainComponent(DomainComponent component);
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public <T extends ParameterDomain> T createParameterDomain(String name, ParameterDomainType type);

	/**
	 * 
	 * @return
	 */
	public List<ParameterDomain> getParameterDoamins();

	/**
	 * 
	 * @param name
	 * @return
	 */
	public ParameterDomain getParameterDomainByName(String name);
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @return
	 * @throws DomainComponentNotFoundException
	 */
	public SynchronizationRule createSynchronizationRule(ComponentValue value, String[] labels) 
			throws DomainComponentNotFoundException;
	
	/**
	 * 
	 * @param rule
	 * @throws SynchronizationCycleException
	 */
	public void addSynchronizationRule(SynchronizationRule rule) 
			throws SynchronizationCycleException;
	
	/**
	 * 
	 * @return
	 */
	public List<SynchronizationRule> getSynchronizationRules();
	
	/**
	 * The method returns the list of synchronization rules with the specified 
	 * value has the head of the rule
	 * 
	 * @param value
	 * @return
	 */
	public List<SynchronizationRule> getSynchronizationRules(ComponentValue value);

	/**
	 * 
	 * @param component
	 * @return
	 */
	public List<SynchronizationRule> getSynchronizationRules(DomainComponent component);
	
	/**
	 * 
	 * @return
	 * @throws UnsolvableFlawFoundException
	 * @throws NoFlawFoundException
	 */
	public List<Flaw> detectFlaws() 
			throws UnsolvableFlawFoundException, NoFlawFoundException;
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	public void propagete(FlawSolution solution) 
			throws PlanRefinementException;
	
	/**
	 * 
	 * @param solution
	 * @throws FlawSolutionApplicationException
	 */
	public void retract(FlawSolution solution) 
			throws FlawSolutionApplicationException;
	
	/**
	 * 
	 * @param single
	 */
	public void display();
}
