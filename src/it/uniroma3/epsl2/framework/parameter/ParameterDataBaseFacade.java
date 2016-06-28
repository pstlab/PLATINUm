package it.uniroma3.epsl2.framework.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQuery;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.QueryManager;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterConstraintPropagationException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterCreationException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterNotFoundException;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterFactory;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;
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
	
	protected Map<String, ParameterDomain> domains;				// available domains
	protected Set<Parameter> parameters;						// current parameters
	protected Map<Parameter, List<ParameterConstraint>> out;	// outgoing constraints
	protected Map<Parameter, List<ParameterConstraint>> in;		// incoming constraints
	
	protected ParameterFactory pFactory;						// parameter factory
	protected ParameterQueryFactory qFactory;					// query factory

	/**
	 * 
	 */
	protected ParameterDataBaseFacade() {
		// set parameters' domains
		this.domains = new HashMap<>();
		// set of parameters
		this.parameters = new HashSet<>();
		this.out = new HashMap<>();
		this.in = new HashMap<>();
		
		// get parameter factory 
		this.pFactory = ParameterFactory.getInstance();
		// get query factory
		this.qFactory = ParameterQueryFactory.getInstance();
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
	public List<Parameter> getParameters() {
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
	public <T extends ParameterDomain> T createParameterDomain(String name, ParameterDomainType type) {	
		// domain
		T domain = this.pFactory.createParameterDomain(name, type);
		// add domain 
		this.domains.put(domain.getName(), domain);
		// get created domain
		return domain;
	}
	
	/**
	 * Create an anonymous parameter as instance of the specified domain.
	 * 
	 * @param type
	 * @param domain
	 * @pram value
	 * @return
	 */
	public <T extends Parameter> T createAnonymousParameter(ParameterType type, ParameterDomain domain) 
			throws ParameterCreationException
	{
		// check domain and parameter type
		if (!type.getDomainType().equals(domain.getType())) {
			throw new ParameterCreationException("Wrong domain type selection " + domain.getType() + " for parameter of type " + type);
		}
		
		// get create parameter
		return this.pFactory.createAnonymousParameter(type, domain);
	}
	
	/**
	 * 
	 * @param label
	 * @param type
	 * @param domain
	 * @return
	 * @throws ParameterCreationException
	 */
	public <T extends Parameter> T createParameter(String label, ParameterType type, ParameterDomain domain) 
			throws ParameterCreationException
	{
		// check domain and parameter type
		if (!type.getDomainType().equals(domain.getType())) {
			throw new ParameterCreationException("Wrong domain type selection " + domain.getType() + " for parameter of type " + type);
		}
		
		// get create parameter
		return this.pFactory.createParameter(label, type, domain);
	}
	
	/**
	 * 
	 * @param param
	 */
	public void addParameter(Parameter param) {
		// add parameter
		this.doAddParameter(param);
	}
	
	/**
	 * 
	 * @param param
	 * @throws ParameterNotFoundException
	 */
	public void deleteParameter(Parameter param) 
			throws ParameterNotFoundException 
	{
		// check parameter
		if (!this.parameters.contains(param)) {
			throw new ParameterNotFoundException("Parameter [" + param.getId() + " " + param.getLabel() + "] not found");
		}
		
		// remove parameter
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
		// check if parameters exist
		if (!this.parameters.contains(constraint.getReference()) || !this.parameters.contains(constraint.getTarget())) {
			throw new ParameterConstraintPropagationException("Constraint's parameters not found reference= " + constraint.getReference() + " target= " + constraint.getTarget());
		}
		
		// check domain of involved parameters
		Parameter reference = constraint.getReference();
		Parameter target = constraint.getTarget();
		// check domains
		if (!reference.getDomain().equals(target.getDomain())) {
			throw new ParameterConstraintPropagationException("Impossible to post constraints between parameters of different types of domains");
		}
		
		// propagate constraint
		this.doPropagateConstraint(constraint);
	}
	
	/**
	 * 
	 * @param constraint
	 */
	public void retract(ParameterConstraint constraint) {
		// check if constraint exists
		if (this.out.containsKey(constraint.getReference()) && 
				this.out.get(constraint.getReference()).contains(constraint) &&
				this.in.containsKey(constraint.getTarget()) &&
				this.in.get(constraint.getTarget()).contains(constraint)) {
			
			// retract constraint
			this.doRetractConstraint(constraint);
		}
		else {
			// constraint not found
			this.logger.warning("ParameterConstraint not found " + constraint);
		}
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
	protected abstract void doAddParameter(Parameter param);
	
	/**
	 * 
	 * @param param
	 */
	protected abstract void doDeleteParameter(Parameter param);
	
	/**
	 * 
	 * @param constraint
	 */
	protected abstract void doPropagateConstraint(ParameterConstraint constraint);
	
	/**
	 * 
	 * @param constraint
	 */
	protected abstract void doRetractConstraint(ParameterConstraint constraint);
}
