package it.cnr.istc.pst.platinum.ai.framework.parameter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.ParameterSolverPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.ParameterQuery;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.ParameterQueryFactory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.ParameterQueryType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.QueryManager;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.AddConstraintParameterNotification;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.AddParameterNotification;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.DelConstraintParameterNotification;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.DelParameterNotification;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.ParameterNotification;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.ParameterNotificationFactory;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.ParameterNotificationObserver;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.ParameterNotificationType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolver;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolverType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ex.ParameterConsistencyException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ex.ParameterConstraintPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ex.ParameterCreationException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ex.ParameterNotFoundException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.Parameter;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomain;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomainType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.BinaryParameterConstraint;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraint;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.query.CheckValuesParameterQuery;

/**
 * 
 * @author anacleto
 *
 */
@ParameterFacadeConfiguration(
		// default parameter constraint solver
		solver = ParameterSolverType.CHOCHO_SOLVER
)
public class ParameterFacade extends FrameworkObject implements QueryManager<ParameterQuery> 
{
	@ParameterSolverPlaceholder
	protected ParameterSolver solver;
	
	protected Map<String, ParameterDomain> domains;						// available parameter domains
	protected Set<Parameter<?>> parameters;								// set of created parameters
	protected Map<Parameter<?>, List<ParameterConstraint>> out;			// outgoing constraints
	protected Map<Parameter<?>, List<ParameterConstraint>> in;			// incoming constraints
	
	protected ParameterNotificationFactory factory;
	protected ParameterQueryFactory queryFactory;						// query factory
	protected final List<ParameterNotificationObserver> observers;
	
	/**
	 * 
	 */
	protected ParameterFacade() {
		super();
		// set parameters' domains
		this.domains = new HashMap<>();
		// set of parameters
		this.parameters = new HashSet<>();
		this.out = new HashMap<>();
		this.in = new HashMap<>();
		// get query factory
		this.queryFactory = ParameterQueryFactory.getInstance();
		
		// set observers
		this.observers = new LinkedList<>();
		// get factory
		this.factory = ParameterNotificationFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		synchronized (this.observers) {
			// subscribe observer
			this.observers.add(this.solver);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ParameterDomain> getParameterDomains() {
		return new ArrayList<>(this.domains.values());
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Parameter<?>> getParameters() {
		return new ArrayList<>(this.parameters);
	}
	
	
	
	/**
	 * Create a possible domain for the specified parameter type.
	 * 
	 * The name of the domain is a unique identifier of the created domain.
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public <T extends ParameterDomain> T createParameterDomain(String name, ParameterDomainType type) 
	{	
		// domain
		T domain = this.doCreateParameterDomain(name, type);
		// add domain 
		this.domains.put(domain.getName(), domain);
		// get created domain
		return domain;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public <T extends ParameterQuery> T createQuery(ParameterQueryType type) {
		// create query
		return this.queryFactory.create(type);
	}
	
	/**
	 * 
	 * @param label
	 * @param type
	 * @param domain
	 * @return
	 * @throws ParameterCreationException
	 */
	public <T extends Parameter<?>> T createParameter(String label, ParameterDomain domain) 
			throws ParameterCreationException
	{
		// create parameter
		T param = this.doCreateParameter(label, domain);
		// get create parameter
		return param;
	}
	
	/**
	 * 
	 * @param param
	 */
	public void addParameter(Parameter<?> param) 
	{
		// add parameter to the data structure
		this.parameters.add(param);
		// initialize constraints
		this.in.put(param, new ArrayList<>());
		this.out.put(param, new ArrayList<>());
		// complete operation
		this.doAddParameter(param);
	}
	
	/**
	 * 
	 * @param param
	 * @throws ParameterNotFoundException
	 */
	public void deleteParameter(Parameter<?> param) 
			throws ParameterNotFoundException 
	{
		// check parameter
		if (!this.parameters.contains(param)) {
			throw new ParameterNotFoundException("Parameter [" + param.getId() + " " + param.getLabel() + "] not found");
		}
		
		// remove all constraints concerning the parameter
		this.in.remove(param);
		this.out.remove(param);
		// remove parameter
		this.parameters.remove(param);
		// complete operation
		this.doDeleteParameter(param);
	}
	
	/**
	 * 
	 * @param constraint
	 * @throws ParameterConstraintPropagationException
	 */
	public void propagate(ParameterConstraint constraint) 
			throws ParameterConstraintPropagationException
	{
		// check constraint type
		switch (constraint.getType())
		{
			// bind constraint
			case BIND : 
			case EXCLUDE :
			{
				// check reference parameter
				if (!this.parameters.contains(constraint.getReference())) {
					throw new ParameterConstraintPropagationException("Reference parameter not found\n- reference= " + constraint.getReference() + "\n");
				}
				
				// propagate constraint
				this.doPropagateConstraint(constraint);
				// add constraint to data structures
				this.out.get(constraint.getReference()).add(constraint);
			}
			break;
			
			// binary constraint
			case EQUAL : 
			case NOT_EQUAL : 
			{
				// get binary constraint
				BinaryParameterConstraint binary = (BinaryParameterConstraint) constraint;
				// check reference and target parameters
				if (!this.parameters.contains(binary.getReference()) || 
						!this.parameters.contains(binary.getTarget())) 
				{
					throw new ParameterConstraintPropagationException("Constraint parameters not found\n- reference= " + constraint.getReference() + "\n- target= " + binary.getTarget() + "\n");
				}
				
				// propagate constraint
				this.doPropagateConstraint(constraint);
				// add constraint to data structures
				this.in.get(binary.getTarget()).add(binary);
				this.out.get(binary.getReference()).add(binary);
			}
			break;
			
			default : {
				throw new RuntimeException("Unknown parameter constraint type - " + constraint.getType());
			}
		}
	}
	
	/**
	 * 
	 * @param constraint
	 */
	public void retract(ParameterConstraint constraint) 
	{
		// check if constraint exists
		if (!this.out.containsKey(constraint.getReference()) && 
				!this.out.get(constraint.getReference()).contains(constraint)) 
		{
			// constraint not found
			throw new RuntimeException("ParameterConstraint not found - " + constraint);
		}
		
		// retract constraint
		this.doRetractConstraint(constraint);
		
		// check constraint type
		switch (constraint.getType())
		{
			// bind constraint
			case BIND : 
			case EXCLUDE : 
			{
				// remove constraint
				this.out.get(constraint.getReference()).remove(constraint);
			}
			break; 
			
			// binary constraint
			case EQUAL : 
			case NOT_EQUAL : 
			{
				// get binary constraint
				BinaryParameterConstraint binary = (BinaryParameterConstraint) constraint;
				// remove constraint
				this.in.get(binary.getTarget()).remove(binary);
				this.out.get(binary.getReference()).remove(binary);
			}
			break;
			
			default : {
				throw new RuntimeException("Unknown parameter constraint type - " + constraint.getType());
			}
		}

	}
	
	/**
	 * 
	 * @throws ConsistencyCheckException
	 */
	public void verify() 
			throws ConsistencyCheckException 
	{
		// check temporal network consistency
		if (!this.solver.isConsistent()) {
			throw new ParameterConsistencyException("Inconsistent parameter constraints found");
		}
	}

	/**
	 * 
	 */
	@Override
	public void process(ParameterQuery query) 
	{
		// check query type
		switch (query.getType()) 
		{
			// check parameter values
			case CHECK_PARAMETER_VALUES : 
			{
				// get query
				CheckValuesParameterQuery pQuery = (CheckValuesParameterQuery) query;
				// get parameter
				Parameter<?> param = pQuery.getParameter();
				// get values
				this.solver.computeValues(param);
			}
			break;
			
			// compute values of all variables
			case COMPUTE_SOLUTION :
			{
				// compute CSP solution
				this.solver.computeSolution();
			}
			break;
		}
	}
	
	/**
	 * 
	 */
	protected void doAddParameter(Parameter<?> param) 
	{
		// create notification
		AddParameterNotification notif = this.factory.create(ParameterNotificationType.ADD_PARAM);
		// set added parameter
		notif.setParameter(param);
		// publish notification
		this.publish(notif);
	}

	/**
	 * 
	 */
	protected void doDeleteParameter(Parameter<?> param) 
	{
		// create notification
		DelParameterNotification notif = this.factory.create(ParameterNotificationType.DEL_PARAM);
		// set added parameter
		notif.setParameter(param);
		// publish notification
		this.publish(notif);
	}
	
	/**
	 * 
	 */
	protected void doPropagateConstraint(ParameterConstraint constraint) 
			throws ParameterConstraintPropagationException 
	{	
		// create notification
		AddConstraintParameterNotification notif = this.factory.create(ParameterNotificationType.ADD_CONSTRAINT);
		notif.setConstraint(constraint);
		// publish notification
		this.publish(notif);
	}
	
	/**
	 * 
	 */
	protected void doRetractConstraint(ParameterConstraint constraint) 
	{
		// create notification
		DelConstraintParameterNotification notif = this.factory.create(ParameterNotificationType.DEL_CONSTRAINT);
		notif.setConstraint(constraint);
		// publish notification
		this.publish(notif);
	}
	
	/**
	 * 
	 * @param notif
	 */
	private void publish(ParameterNotification notif) {
		synchronized (this.observers) {
			for (ParameterNotificationObserver observer : this.observers) {
				observer.update(notif);
			}
		}
	}
	
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T extends ParameterDomain> T doCreateParameterDomain(String name, ParameterDomainType type)
	{
		// parameter to create
		T dom = null;
		try {
			// get parameter class
			Class<T> clazz = (Class<T>) Class.forName(type.getParameterDomainClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor(String.class);
			c.setAccessible(true);
			dom = c.newInstance(name);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get create parameter
		return dom;
	}
	
	/**
	 * 
	 * @param label
	 * @param domain
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T extends Parameter<?>> T doCreateParameter(String label, ParameterDomain domain) {
		// parameter to create
		T param = null;
		try {
			// get parameter class
			Class<T> clazz = (Class<T>) Class.forName(domain.getParameterType()
					.getParameterClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor(
					String.class, 
					Class.forName(domain.getType().getParameterDomainClassName()));
			
			// set accessible
			c.setAccessible(true);
			param = c.newInstance(label, domain);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get create parameter
		return param;
	}
}
