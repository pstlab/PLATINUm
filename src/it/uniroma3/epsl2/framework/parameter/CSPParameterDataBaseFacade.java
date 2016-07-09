package it.uniroma3.epsl2.framework.parameter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.ParameterDataBaseFacadeConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ParameterReasonerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifcycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQuery;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterConsistencyException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterCreationException;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationConstantParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericConstantParameter;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.BindParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.query.CheckValuesParameterQuery;
import it.uniroma3.epsl2.framework.parameter.solver.ParameterSolver;
import it.uniroma3.epsl2.framework.parameter.solver.ParameterSolverType;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.AddConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.AddParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.DelConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.DelParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.ParameterNotification;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.ParameterNotificationFactory;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.ParameterNotificationObserver;
import it.uniroma3.epsl2.framework.parameter.solver.lang.event.ParameterNotificationType;

/**
 * 
 * @author anacleto
 *
 */
@ParameterDataBaseFacadeConfiguration(

	solver = ParameterSolverType.CHOCHO_SOLVER
)
public final class CSPParameterDataBaseFacade extends ParameterDataBaseFacade 
{
	@ParameterReasonerReference
	private ParameterSolver reasoner;
	
	private List<ParameterNotificationObserver> observers;
	
	private ParameterNotificationFactory factory;

	/**
	 * 
	 */
	protected CSPParameterDataBaseFacade() {
		super();
		// initialize observers
		this.observers = new LinkedList<>();
		// get factory
		this.factory = ParameterNotificationFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// subscribe reasoner to updates
		this.subscribe(this.reasoner);
	}
	
	/**
	 * 
	 * @throws ConsistencyCheckException
	 */
	@Override
	public void checkConsistency() 
			throws ConsistencyCheckException {
		// check temporal network consistency
		if (!this.reasoner.isConsistent()) {
			throw new ParameterConsistencyException("Inconsistent parameter constraints found");
		}
	}

	/**
	 * 
	 */
	@Override
	public void process(ParameterQuery query) {
		// check query type
		switch (query.getType()) {
			// check parameter values
			case CHECK_PARAMETER_VALUES : {
				// get query
				CheckValuesParameterQuery pQuery = (CheckValuesParameterQuery) query;
				// get parameter
				Parameter param = pQuery.getParameter();
				// get values
				this.reasoner.computeValues(param);
			}
			break;
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doAddParameter(Parameter param)  {
		// add parameter
		this.parameters.add(param);
		this.out.put(param, new ArrayList<ParameterConstraint>());
		this.in.put(param, new ArrayList<ParameterConstraint>());
		
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
	@Override
	protected void doDeleteParameter(Parameter param) {
		// delete parameter
		this.parameters.remove(param);
		// remove related constraints
		this.out.remove(param);
		this.in.remove(param);
		
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
	@Override
	protected void doPropagateConstraint(ParameterConstraint constraint) {
		// check constraint type
		switch (constraint.getType()) 
		{
			// propagate (not) equal constraints
			case EQUAL : 
			case NOT_EQUAL : {
				// add constraint to reference parameter
				this.out.get(constraint.getReference()).add(constraint);
				this.in.get(constraint.getTarget()).add(constraint);
				
				// create notification
				AddConstraintParameterNotification notif = this.factory.create(ParameterNotificationType.ADD_CONSTRAINT);
				notif.setConstraint(constraint);
				// publish notification
				this.publish(notif);
			}
			break;
			
			// propagate bind constraint
			case BIND : {
				try 
				{
					// get constraint
					BindParameterConstraint bind = (BindParameterConstraint) constraint;
					// get parameter
					Parameter param = bind.getReference();
					ParameterDomain dom = param.getDomain();
					// create constant anonymous variable to bound the parameter with					
					switch (dom.getType()) 
					{
						// constant enumeration parameter
						case ENUMERATION_DOMAIN_PARAMETER_TYPE : {
							// create parameter
							EnumerationConstantParameter anonym = this.createAnonymousParameter(ParameterType.CONSTANT_ENUMERATION_PARAMETER_TYPE, dom);
							// set value
							anonym.setValue(bind.getValue());
							// add parameter
							this.addParameter(anonym);
							// set constraint target
							bind.setTarget(anonym);
						}
						break;
						
						// constant numeric parameter
						case NUMERIC_DOMAIN_PARAMETER_TYPE : {
							// create parameter
							NumericConstantParameter anonym = this.createAnonymousParameter(ParameterType.CONSTANT_NUMERIC_PARAMETER_TYPE, dom);
							// parse value
							Integer value = new Integer(bind.getValue());
							// set value
							anonym.setValue(value);
							// add parameter
							this.addParameter(anonym);
							// set target
							bind.setTarget(anonym);
						}
						break;
					}
					
					// add constraint to parameter
					this.out.get(constraint.getReference()).add(constraint);
					this.in.get(constraint.getTarget()).add(constraint);
					
					// create notification
					AddConstraintParameterNotification notif = this.factory.create(ParameterNotificationType.ADD_CONSTRAINT);
					notif.setConstraint(constraint);
					// publish notification
					this.publish(notif);
				}
				catch (ParameterCreationException ex) {
					this.logger.error(ex.getMessage());
				}
				
			}
			break;
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetractConstraint(ParameterConstraint constraint) {
		// remove constraints
		this.out.get(constraint.getReference()).remove(constraint);
		this.in.get(constraint.getTarget()).remove(constraint);
		
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
		for (ParameterNotificationObserver observer : this.observers) {
			observer.notify(notif);
		}
	}
	
	
	/**
	 * 
	 * @param observer
	 */
	private void subscribe(ParameterNotificationObserver observer) {
		// subscribe observer
		this.observers.add(observer);
	}
}
