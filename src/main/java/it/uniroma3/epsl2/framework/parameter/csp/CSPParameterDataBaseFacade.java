package it.uniroma3.epsl2.framework.parameter.csp;

import java.util.LinkedList;
import java.util.List;

import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.ParameterDataBaseFacadeConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQuery;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacade;
import it.uniroma3.epsl2.framework.parameter.csp.event.AddConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.AddParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.DelConstraintParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.DelParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotificationFactory;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotificationObserver;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotificationType;
import it.uniroma3.epsl2.framework.parameter.csp.solver.ParameterSolverType;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterConsistencyException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterConstraintPropagationException;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.query.CheckValuesParameterQuery;

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
		// subscribe observer
		this.observers.add(this.solver);
	}
	
	/**
	 * 
	 * @throws ConsistencyCheckException
	 */
	@Override
	public void checkConsistency() 
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
		for (ParameterNotificationObserver observer : this.observers) {
			observer.update(notif);
		}
	}
}
