package it.istc.pst.platinum.framework.domain.component.pdb;

import java.util.List;

import it.istc.pst.platinum.deliberative.solver.Operator;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.PlanElementStatus;
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
	 * Check consistency of the plan database
	 * 
	 * The procedure verifies also pseudo-controllability by checking 
	 * "squeezed" uncontrollable tokens
	 * 
	 * @throws ConsistencyCheckException
	 */
	public void verify() 
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
	 * @return
	 * @throws UnsolvableFlawException
	 */
	public List<Flaw> detectFlaws() 
			throws UnsolvableFlawException;
	
	/**
	 * 
	 * @param type
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
	 * Compute the makespan of the plan
	 * 
	 * @return
	 */
	public double computeMakespan();
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public SynchronizationRule createSynchronizationRule(ComponentValue value, String[] labels) 
			throws DomainComponentNotFoundException;
	
	/*
	 * 
	 */
	public void addSynchronizationRule(SynchronizationRule rule) 
			throws SynchronizationCycleException;
	
	/**
	 * 
	 * @return
	 */
	public List<SynchronizationRule> getSynchronizationRules();
	
	/**
	 * 
	 */
	public List<SynchronizationRule> getSynchronizationRules(ComponentValue value);
	
	/**
	 * 
	 */
	public List<SynchronizationRule> getSynchronizationRules(DomainComponent component);
}
