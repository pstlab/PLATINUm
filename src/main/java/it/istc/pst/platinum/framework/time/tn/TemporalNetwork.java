package it.istc.pst.platinum.framework.time.tn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.time.tn.ex.DistanceConstraintNotFoundException;
import it.istc.pst.platinum.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.istc.pst.platinum.framework.time.tn.ex.InconsistentTpValueException;
import it.istc.pst.platinum.framework.time.tn.ex.TemporalNetworkTransactionFailureException;
import it.istc.pst.platinum.framework.time.tn.ex.TimePointNotFoundException;
import it.istc.pst.platinum.framework.time.tn.lang.event.AddRelationTemporalNetworkNotification;
import it.istc.pst.platinum.framework.time.tn.lang.event.AddTimePointTemporalNetworkNotification;
import it.istc.pst.platinum.framework.time.tn.lang.event.DelRelationTemporalNetworkNotification;
import it.istc.pst.platinum.framework.time.tn.lang.event.DelTimePointTemporalNetworkNotification;
import it.istc.pst.platinum.framework.time.tn.lang.event.TemporalNetworkNotificationFactory;
import it.istc.pst.platinum.framework.time.tn.lang.event.TemporalNetworkNotificationTypes;
import it.istc.pst.platinum.framework.time.tn.lang.event.TemporalNetworkObserver;
import it.istc.pst.platinum.framework.time.tn.lang.event.ex.NotificationPropagationFailureException;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalNetwork extends ApplicationFrameworkObject 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE_LOGGER)
	protected FrameworkLogger logger;
	
	private int counter;				// time point ID counter
	protected long origin;
	protected long horizon;
	
	// origin time point
	protected TimePoint tpOrigin;
	// horizon time point
	protected TimePoint tpHorizion;
	
	// data structures
	protected Set<TimePoint> recyclePoints;
	
	// temporal network observers
	private List<TemporalNetworkObserver> observers;
	
	/**
	 * Creates and initialize a Temporal Network instance.
	 * 
	 * The constructor takes as input a temporal origin and a 
	 * temporal horizon. These values determine the lower and the 
	 * upper bounds of time points variables respectively.
	 * 
	 * @param origin
	 * @param horizon
	 */
	protected TemporalNetwork(long origin, long horizon) {
		super();
		// setup counter
		this.counter = 0;
		// initialize data
		this.origin = origin;
		this.horizon = horizon;
		
		// create the origin time point
		this.tpOrigin = new TimePoint(this.nextTpId(), origin, origin);
		this.tpOrigin.setLowerBound(origin);
		this.tpOrigin.setUpperBound(origin);
		
		// create the horizon time point
		this.tpHorizion = new TimePoint(this.nextTpId(), horizon, horizon);
		this.tpHorizion.setLowerBound(horizon);
		this.tpHorizion.setUpperBound(horizon);
		
		// setup recycle points and observers
		this.recyclePoints = new HashSet<TimePoint>();
		this.observers = new LinkedList<TemporalNetworkObserver>();
	}

	/**
	 * Subscribes a new observer to the Temporal Network
	 * 
	 * @param obs
	 */
	public final void subscribe(TemporalNetworkObserver obs) {
		try {
			// add observer
			this.observers.add(obs);
			// send initialization notification
			obs.notify(TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.INITIALIZATION));	
		} catch (NotificationPropagationFailureException ex) {
			this.logger.error("[" + this.getClass().getName() + "]: Error at Observer Initialization\n- message= " + ex.getMessage());
		}
	}
	
	/**
	 * Returns the value of the origin
	 * 
	 * @return
	 */
	public final long getOrigin() {
		return origin;
	}
	
	/**
	 * Returns the value of the horizon
	 * 
	 * @return
	 */
	public final long getHorizon() {
		return this.horizon;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract int size();
	
	/**
	 * Returns the temporal origin reference time point
	 * 
	 * @return
	 */
	public final TimePoint getOriginTimePoint() {
		return this.tpOrigin;
	}
	
	/**
	 * 
	 * @return
	 */
	public final TimePoint getHorizionTimePoint() {
		return this.tpHorizion;
	}
	
	/**
	 * Returns the time point with selected id
	 * 
	 * @param id
	 * @return
	 * @throws TimePointNotFoundException
	 */
	public abstract TimePoint getTimePoint(int id) 
			throws TimePointNotFoundException;
	
	/**
	 * Returns all the time points of the temporal network
	 * 
	 * @return
	 */
	public abstract List<TimePoint> getTimePoints();
	
	/**
	 * Returns all distance constraints where the specified 
	 * time point is the source of the constraint
	 * 
	 * @param tp
	 * @return
	 */
	public abstract List<TimePointDistanceConstraint> getConstraints(TimePoint tp);
	
	/**
	 * Returns the list of all distance constraints of the network
	 * 
	 * @return
	 */
	public List<TimePointDistanceConstraint> getConstraints() {
		Set<TimePointDistanceConstraint> set = new HashSet<>();
		for (TimePoint tp : this.getTimePoints()) {
			// add constraints
			set.addAll(this.getConstraints(tp));
		}
		return new ArrayList<>(set);
	}
	
	/**
	 * Returns all distance constraints involving time points tp1 and tp2. The 
	 * method returns both constraints (tp1,tp2) and constraints (tp2,tp1) if
	 * they exist
	 * 
	 * @param tp1
	 * @param tp2
	 * @return
	 */
	public abstract List<TimePointDistanceConstraint> getConstraints(TimePoint tp1, TimePoint tp2);
	
	/**
	 * Returns the distance constraint between the origin of the network and the time point.
	 * 
	 * The method returns null if no constraint is found
	 * 
	 * @param point
	 * @return
	 */
	public abstract TimePointDistanceConstraint getConstraintFromOrigin(TimePoint point);
	
	/**
	 * 
	 * @param tp
	 * @return
	 */
	public abstract TimePointDistanceConstraint getConstraintToHorizon(TimePoint tp);
	
	/**
	 * Creates a new flexible TimePoint
	 * 
	 * The method can also use a previously created (and 
	 * deleted) time point rather than create a new instance
	 * 
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	public final TimePoint addTimePoint() 
			throws InconsistentDistanceConstraintException 
	{
		// create a time point or use a previous created one
		TimePoint tp = null;
		// extract the first element
		Iterator<TimePoint> it = this.recyclePoints.iterator();
		// check among recycled time points first
		if (it.hasNext()) {
			// extract next time point
			tp = it.next();
			// remove time point from the set
			it.remove();
		}
		else {
			// actually create flexible time point
			tp = new TimePoint(this.nextTpId(), this.origin, this.horizon);
		}
		
		// add time point to the network
		this.doAddTimePoint(tp);
		
		// create notification
		AddTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory.
				createNotification(TemporalNetworkNotificationTypes.ADD_TP);
		info.addTimePoint(tp);
		// notify observers
		for (TemporalNetworkObserver obs : this.observers) {
			try {
				obs.notify(info);
			} catch (NotificationPropagationFailureException ex) {
				this.logger.error(ex.getMessage());
			}
		}

		// return new time point
		return tp;
	}
	
	/**
	 * Creates N flexible time points 
	 * 
	 * The method can also use previously created (and 
	 * deleted) time points rather than create new instances
	 * 
	 * @param n
	 * @return
	 * @throws TemporalNetworkTransactionFailureException
	 * 
	 */
	public final List<TimePoint> addTimePoints(int n) 
			throws TemporalNetworkTransactionFailureException 
	{
		// roll-back flag
		boolean rollback = false;
		// create notification
		AddTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory.
				createNotification(TemporalNetworkNotificationTypes.ADD_TP);
		
		// create time points
		for (int i= 0; i < n && !rollback; i++) {
			try {
				// create flexible time point
				TimePoint tp = this.addTimePoint();	//this.doCreateFlexibleTimePoint();
				info.addTimePoint(tp);
			}
			catch (InconsistentDistanceConstraintException ex) {
				// roll-back 
				rollback = true;
				this.logger.error(ex.getMessage());
			}
		}
		
		// check if transition is successfully ended
		if (!rollback) {
			// notify observers
			for (TemporalNetworkObserver obs : this.observers) {
				try {
					obs.notify(info);
				} catch (NotificationPropagationFailureException ex) {
					this.logger.error(ex.getMessage());
				}
			}
		}
		else {
			// undo created time points without notifying observers
			for (TimePoint tp : info.getPoints()) {
				try {
					// delete 
					this.doRemoveTimePoint(tp);
				} catch (TimePointNotFoundException ex) {
					this.logger.error(ex.getMessage());
				}
			}
			// throw exception
			throw new TemporalNetworkTransactionFailureException("Error while creating time points - Transaction Rollback");
		}
		
		// get time points
		return info.getPoints();
	}
	
	/**
	 * Creates a new TimePoint and fix it into the network at
	 * the desired time (at)
	 * 
	 * The method throws an exception if the specified value (at)
	 * is out of the temporal network domain
	 * 
	 * The method can also use a previously created (and deleted) time
	 * point rather than create a new instance
	 * 
	 * @param at
	 * @return
	 * @throws InconsistentTpValueException
	 */
	public final TimePoint addTimePoint(long at) 
			throws InconsistentTpValueException, InconsistentDistanceConstraintException
	{
		// check desired value
		if (at < this.origin || at > this.horizon) {
			// inconsistent value
			throw new InconsistentTpValueException("TimePoint value (= " + at +") out of the domain [origin= " + this.origin + ", horizon= " + this.horizon);
		}
		
		// create a time point or use a previous created one
		TimePoint tp = null;
		// extract the first element
		Iterator<TimePoint> it = this.recyclePoints.iterator();
		// check recycled time points first
		if (it.hasNext()) 
		{
			// extract next time point
			tp = it.next();
			// set bounds
			tp.setDomLb(at);
			tp.setDomUb(at);
			// remove time point from the set
			it.remove();
		}
		else {
			// create time point
			tp = new TimePoint(this.nextTpId(), at, at);
		}
		
		// add time point to the network
		this.doAddTimePoint(tp);

		// create notification
		AddTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.ADD_TP);
		info.addTimePoint(tp);
		// notify observers
		for (TemporalNetworkObserver obs : this.observers) {
			try {
				obs.notify(info);
			}
			catch (NotificationPropagationFailureException ex) {
				this.logger.error(ex.getMessage());
			}
		}

		// return new time point
		return tp;
	}
	
	/**
	 * 
	 * @param lb
	 * @param ub
	 * @return
	 * @throws InconsistentTpValueException
	 * @throws InconsistentDistanceConstraintException
	 */
	public final TimePoint addTimePoint(long lb, long ub) 
			throws InconsistentTpValueException, InconsistentDistanceConstraintException
	{
		// check domain
		if (lb > ub || lb < this.origin || ub > this.horizon) {
			// inconsistent value
			throw new InconsistentTpValueException("TimePoint (lb= " + lb +", ub= " + ub + ") is inconsistent or it is out of the domain [origin= " + this.origin + ", horizon= " + this.horizon);
		}
		
		// create a time point or use a previous created one
		TimePoint tp = null;
		// extract the first element
		Iterator<TimePoint> it = this.recyclePoints.iterator();
		// check recycled time points first
		if (it.hasNext()) {
			// extract next time point
			tp = it.next();
			// set desired bounds
			tp.setDomLb(lb);
			tp.setDomUb(ub);
			// remove time point from the set
			it.remove();
		}
		else {
			// create time point
			tp = new TimePoint(this.nextTpId(), lb, ub);
		}
		
		// add time point to the network
		this.doAddTimePoint(tp);

		// create notification
		AddTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.ADD_TP);
		info.addTimePoint(tp);
		// notify observers
		for (TemporalNetworkObserver obs : this.observers) {
			try {
				obs.notify(info);
			}
			catch (NotificationPropagationFailureException ex) {
				this.logger.error(ex.getMessage());
			}
		}

		// return new time point
		return tp;
	}
	
	/**
	 * 
	 * @param tp
	 * @throws InconsistentDistanceConstraintException
	 */
	protected abstract void doAddTimePoint(TimePoint tp) 
			throws InconsistentDistanceConstraintException;

	/**
	 * Removes a Time Point from the network
	 * 
	 * @param tp
	 */
	public final void removeTimePoint(TimePoint tp) {
		try {
			// delete time point
			this.doRemoveTimePoint(tp);
			// add time point to recycle list
			this.recyclePoints.add(tp);
			
			// create notification
			DelTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory
					.createNotification(TemporalNetworkNotificationTypes.DEL_TP);
			info.addTimePoint(tp);
			// notify observers
			for (TemporalNetworkObserver obs : this.observers) {
				try {
					// try to notify
					obs.notify(info);
				}
				catch (NotificationPropagationFailureException ex) {
					this.logger.error(ex.getMessage());
				}
			}
		} 
		catch (TimePointNotFoundException ex) {
			this.logger.error(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param tps
	 */
	public final void removeTimePoints(List<TimePoint> tps) {
		// create notification
		DelTimePointTemporalNetworkNotification info = TemporalNetworkNotificationFactory
				.createNotification(TemporalNetworkNotificationTypes.DEL_TP);
		for (TimePoint tp : tps) {
			try {
				// delete time point
				this.doRemoveTimePoint(tp);
				// add time point to recycle list
				this.recyclePoints.add(tp);
				info.addTimePoint(tp);
			}
			catch (TimePointNotFoundException ex) {
				this.logger.error(ex.getMessage());
			}
		}
		
		// check information
		if (!info.isEmpty()) {
			// notify observers
			for (TemporalNetworkObserver obs : this.observers) {
				try {
					// try to notify
					obs.notify(info);
				} catch (NotificationPropagationFailureException ex) {
					this.logger.error(ex.getMessage());
				}
			}
		}
	}
	
	/**
	 * Actually delete the selected time point from the network 
	 * and all related constraints
	 * 
	 * @param tp
	 * @throws TimePointNotFoundException
	 */
	protected abstract void doRemoveTimePoint(TimePoint tp) 
			throws TimePointNotFoundException;
	
	/**
	 * Creates a distance constraint between two time points with the 
	 * specified lower and upper bounds
	 * 
	 * Throws an exception when trying to create a distance constraint with
	 * lower bound greater than upper bound (lb > ub)
	 * 
	 * @param constraint
	 * @return
	 * @throws InconsistentTpDistanceRelation
	 */
	public final void addDistanceConstraint(TimePointDistanceConstraint constraint) 
			throws InconsistentDistanceConstraintException 
	{
		// check consistency of distance bound
		if (constraint.getDistanceLowerBound() > constraint.getDistanceUpperBound())  {
			// inconsistent value
			throw new InconsistentDistanceConstraintException("DistanceConstraint "
					+ "(dmin= " + constraint.getDistanceLowerBound() +", "
					+ "dmax= " + constraint.getDistanceUpperBound() + ") "
					+ "is inconsistent or it is not compatible wih the domain [origin= " + this.origin + ", horizon= " + this.horizon + "]");
		}
		
		// add constraint to the network
		this.doAddConstraint(constraint);
		
		// create notification
		AddRelationTemporalNetworkNotification info = TemporalNetworkNotificationFactory
				.createNotification(TemporalNetworkNotificationTypes.ADD_REL);
		// set constraint
		info.addRelation(constraint);
		
		// notify observers
		for (TemporalNetworkObserver obs : this.observers) 
		{
			try {
				// try to notify
				obs.notify(info);
			}
			catch (NotificationPropagationFailureException ex) {
				this.logger.error(ex.getMessage());
			}
		}
	}

	/**
	 * Create N distance constraints all at once.
	 * 
	 * The method is atomic, i.e. if a constraint creation operation fails than all the 
	 * constraints previously created by the method are aborted. So if something goes wrong
	 * during the creation of constraints then the network is restored to the status before
	 * method invocation.   
	 * 
	 * @param constraints
	 * @return
	 * @throws TemporalNetworkTransactionFailureException
	 */
	public final void addDistanceConstraint(TimePointDistanceConstraint[] constraints) 
			throws TemporalNetworkTransactionFailureException 
	{
		// roll-back flag
		boolean rollback = false;
		// create notification
		AddRelationTemporalNetworkNotification info = TemporalNetworkNotificationFactory.
				createNotification(TemporalNetworkNotificationTypes.ADD_REL);
		
		// propagate constraints
		for (int i = 0; i < constraints.length && !rollback; i++) 
		{
			try 
			{
				// get constraint
				TimePointDistanceConstraint constraint = constraints[i];
				// add constraint to network
				this.doAddConstraint(constraint);
				// add constraint to notification
				info.addRelation(constraint);
			}
			catch (InconsistentDistanceConstraintException ex ) {
				// roll-back
				rollback = true;
				this.logger.error(ex.getMessage());
			}
		}
			
		// check if transaction has been successfully done
		if (!rollback) 
		{
			// notify observers
			for (TemporalNetworkObserver obs : this.observers) 
			{
				try 
				{
					// try to notify
					obs.notify(info);
				} catch (NotificationPropagationFailureException ex) {
					this.logger.error(ex.getMessage());
				}
			}
		}
		else 
		{
			// undo transaction
			for (TimePointDistanceConstraint rel : info.getRels()) 
			{
				try 
				{
					// undo without notifying observers
					this.doRemoveDistanceConstraint(rel);
				}
				catch (DistanceConstraintNotFoundException ex) {
					this.logger.error(ex.getMessage());
				}
			}
			
			// throw exception
			throw new TemporalNetworkTransactionFailureException("Error while creating temporal constraints - Rollback Transaction");
		}
	}
	
	/**
	 * 
	 * @param rel
	 * @throws InconsistentDistanceConstraintException
	 */
	protected abstract void doAddConstraint(TimePointDistanceConstraint rel) 
			throws InconsistentDistanceConstraintException;
	
	/**
	 * Removes the distance constraint from the network
	 * 
	 * @param rel
	 */
	public final void removeConstraint(TimePointDistanceConstraint rel) 
	{
		try 
		{
			// remove temporal relation
			this.doRemoveDistanceConstraint(rel);
			
			// create notification
			DelRelationTemporalNetworkNotification info = TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.DEL_REL);
			info.addRelation(rel);
			// notify observers
			for (TemporalNetworkObserver obs : this.observers) {
				try {
					// try to notify
					obs.notify(info);
				}
				catch (NotificationPropagationFailureException ex) {
					this.logger.error(ex.getMessage());
				}
			}
		} catch (DistanceConstraintNotFoundException ex) {
			this.logger.error(ex.getMessage());
		}
	}
	
	/**
	 * Removes N distance constraints all at once
	 *
	 * @param rel
	 */
	public final void removeDistanceConstraint(List<TimePointDistanceConstraint> rels) {
		// create notification
		DelRelationTemporalNetworkNotification info = TemporalNetworkNotificationFactory.createNotification(TemporalNetworkNotificationTypes.DEL_REL);
		for (TimePointDistanceConstraint rel : rels) {
			try {
				// remove temporal relation
				this.doRemoveDistanceConstraint(rel);
				info.addRelation(rel);
			}
			catch (DistanceConstraintNotFoundException ex) {
				this.logger.error(ex.getMessage());
			}
		}
		
		// notify observers
		for (TemporalNetworkObserver obs : this.observers) {
			try {
				// try to notify
				obs.notify(info);
			}
			catch (NotificationPropagationFailureException ex) {
				this.logger.error(ex.getMessage());
			}
		}
	}
	
	/**
	 * Actually remove constraint from network
	 * 
	 * @param rel
	 * @throws DistanceConstraintNotFoundException
	 */
	protected abstract void doRemoveDistanceConstraint(TimePointDistanceConstraint rel) 
			throws DistanceConstraintNotFoundException;
	
	/**
	 * 
	 * @param lb
	 * @param ub
	 * @return
	 */
	protected TimePoint createTimePoint(long lb, long ub) {
		return new TimePoint(this.nextTpId(), lb, ub);
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param distance
	 * @param controllable
	 * @return
	 */
	protected final TimePointDistanceConstraint createDistanceConstraint(TimePoint from, TimePoint to, long[] distance, boolean controllable) 
	{
		// create constraint
		TimePointDistanceConstraint c = new TimePointDistanceConstraint();
		c.setReference(from);
		c.setTarget(to);
		c.setDistanceLowerBound(distance[0]);
		c.setDistanceUpperBound(distance[1]);
		c.setControllable(controllable);
		// get constraint
		return c;
	}
	
	/**
	 * 
	 * @return
	 */
	private synchronized int nextTpId() {
		return counter++;
	}
}