package it.cnr.istc.pst.platinum.ai.framework.domain.component;

import java.util.List;

import it.cnr.istc.pst.platinum.ai.deliberative.solver.Operator;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.SynchronizationRule;
import it.cnr.istc.pst.platinum.ai.framework.domain.knowledge.DomainKnowledge;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.DomainComponentNotFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.OperatorPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ProblemInitializationException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.Plan;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.SolutionPlan;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.Problem;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomain;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomainType;

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
	public DomainKnowledge getDomainKnowledge();
	
	
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
//	public Plan getPlan(PlanElementStatus status);
	
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
	 * Query the plan database to retrieve flaws concerning the internal 
	 * components. Note that no solutions to the detected flaws are 
	 * computed when calling this method.
	 * 
	 * @return
	 */
	public List<Flaw> checkFlaws();
	
	/**
	 * Query the plan database to retrieve flaws concerning the internal 
	 * components. Only flaws of the specified types are considered 
	 * 
	 * Note that no solutions to the detected flaws are 
	 * computed when calling this method.
	 * 
	 * @return
	 */
	public List<Flaw> checkFlaws(FlawType[] types);
	
	/**
	 * 
	 * Query the the plan database to retrieve flaws concerning internal 
	 * components. Flaws of all known types are considered in the computation 
	 * of the response.
	 * 
	 * For each detected flaw a set of possible solutions is computed.
	 * 
	 * @return
	 * @throws UnsolvableFlawException
	 */
	public List<Flaw> detectFlaws() 
			throws UnsolvableFlawException;
	
	/**
	 * 
	 * Query the the plan database to retrieve flaws concerning internal 
	 * components. Only flaws of the specified type are considered in 
	 * computation of query response.
	 * 
	 * For each detected flaw a set of possible solutions is computed.
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
	public double[] getMakespan();
	
	/**
	 * 
	 * @return
	 */
	public double[] getBehaviorDuration();
	
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
