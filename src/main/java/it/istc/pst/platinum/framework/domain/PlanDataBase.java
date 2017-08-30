package it.istc.pst.platinum.framework.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.PlanElementStatus;
import it.istc.pst.platinum.framework.domain.component.pdb.Operator;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationRule;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.DomainComponentNotFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.OperatorPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Plan;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.microkernel.lang.problem.Problem;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;

/**
 * 
 * @author anacleto
 *
 */
public interface PlanDataBase 
{
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
	
//	/**
//	 * 
//	 * @return
//	 */
//	public Agenda getAgenda();
	
	/**
	 * 
	 * @return
	 */
	public Plan getPlan();
	
	/**
	 * 
	 * @param status
	 * @return
	 */
	public Plan getPlan(PlanElementStatus status);
	
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
	 * @throws UnsolvableFlawException
	 */
	public List<Flaw> detectFlaws() 
			throws UnsolvableFlawException;
	
	/**
	 * 
	 * @param types
	 * @return
	 * @throws UnsolvableFlawException
	 */
	public List<Flaw> detectFlaws(FlawType type) 
			throws UnsolvableFlawException;
	
	/**
	 * 
	 * @param operator
	 * @throws OperatorPropagationException
	 */
	public void propagate(Operator operator) 
			throws OperatorPropagationException;
	
	/**
	 * 
	 * @param operator
	 */
	public void retract(Operator operator);
	
	/**
	 * 
	 * @param single
	 */
	public void display();
	
	/**
	 * 
	 * @return
	 */
	public Map<DomainComponent, Set<DomainComponent>> getDependencyGraph();
	
	/**
	 * 
	 * @return
	 */
	public Map<ComponentValue, Set<ComponentValue>> getDecompositionTree();
	
	/**
	 * Compute the makespan of the plan
	 * 
	 * @return
	 */
	public double computeMakespan();
}
