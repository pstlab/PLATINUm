package it.uniroma3.epsl2.framework.parameter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ParameterReasonerReference;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQuery;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQueryType;
import it.uniroma3.epsl2.framework.microkernel.query.QueryManager;
import it.uniroma3.epsl2.framework.parameter.csp.solver.ParameterSolver;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterConstraintPropagationException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterCreationException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterNotFoundException;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.BinaryParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ParameterDataBaseFacade extends ApplicationFrameworkObject implements QueryManager<ParameterQuery> 
{
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	@ParameterReasonerReference
	protected ParameterSolver solver;
	
	protected Map<String, ParameterDomain> domains;				// available parameter domains
	protected Set<Parameter<?>> parameters;						// set of created parameters
	protected Map<Parameter<?>, List<ParameterConstraint>> out;	// outgoing constraints
	protected Map<Parameter<?>, List<ParameterConstraint>> in;		// incoming constraints
	
	protected ParameterQueryFactory queryFactory;				// query factory

	/**
	 * 
	 */
	protected ParameterDataBaseFacade() {
		super();
		// set parameters' domains
		this.domains = new HashMap<>();
		// set of parameters
		this.parameters = new HashSet<>();
		this.out = new HashMap<>();
		this.in = new HashMap<>();
		// get query factory
		this.queryFactory = ParameterQueryFactory.getInstance();
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
	public <T extends Parameter<?>> T createParameter(String label, ParameterType type, ParameterDomain domain) 
			throws ParameterCreationException
	{
		// check domain and parameter type
		if (!type.getDomainType().equals(domain.getType())) {
			throw new ParameterCreationException("Wrong domain type " + domain.getType() + " for parameter of type " + type);
		}
	
		// create parameter
		T param = this.doCreateParameter(label, type, domain);
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
				if (!this.parameters.contains(binary.getReference()) || !this.parameters.contains(binary.getTarget())) {
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

		/*
		 * TODO: VERIFICARE SE PARAMETRI "ISOLATI" VANNO RIMOSSI
		 */
	}
	
	/**
	 * 
	 */
	@Override
	public abstract void process(ParameterQuery query);

	/**
	 * 
	 * @throws ConsistencyCheckException
	 */
	public abstract void checkConsistency() 
			throws ConsistencyCheckException;

	/**
	 * 
	 * @param param
	 */
	protected abstract void doAddParameter(Parameter<?> param);
	
	/**
	 * 
	 * @param param
	 */
	protected abstract void doDeleteParameter(Parameter<?> param);
	
	/**
	 * 
	 * @param constraint
	 * @throws ParameterConstraintPropagationException
	 */
	protected abstract void doPropagateConstraint(ParameterConstraint constraint) 
			throws ParameterConstraintPropagationException;
	
	/**
	 * 
	 * @param constraint
	 */
	protected abstract void doRetractConstraint(ParameterConstraint constraint);
	
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
	private <T extends Parameter<?>> T doCreateParameter(String label, ParameterType type, ParameterDomain domain) {
		// parameter to create
		T param = null;
		try {
			// get parameter class
			Class<T> clazz = (Class<T>) Class.forName(type.getParameterClassName());
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
