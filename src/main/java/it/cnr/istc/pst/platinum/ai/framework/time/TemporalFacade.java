package it.cnr.istc.pst.platinum.ai.framework.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.TemporalNetworkPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.TemporalSolverPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.QueryManager;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQuery;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryFactory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.InconsistentIntervaEndTimeException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.InconsistentIntervalDurationException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.InconsistentIntervalStartTimeException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.PseudoControllabilityException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConsistencyException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConstraintPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalIntervalCreationException;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TimePointCreationException;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.FixIntervalDurationConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.FixTimePointConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintFactory;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.AfterIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.BeforeIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.ContainsIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.DuringIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.EndsDuringIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.EqualsIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.MeetsIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.MetByIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.StartsDuringIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalOverlapQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalPseudoControllabilityQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalScheduleQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolver;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolverType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetwork;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetworkType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePointDistanceConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.InconsistentDistanceConstraintException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.ex.InconsistentTpValueException;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointDistanceQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointScheduleQuery;

/**
 * 
 * @author alessandro
 *
 */
@TemporalFacadeConfiguration(
		// default network type 
		network = TemporalNetworkType.STNU,
		// default temporal reasoner
		solver = TemporalSolverType.APSP
)
public class TemporalFacade extends FrameworkObject implements QueryManager<TemporalQuery>
{
	@TemporalNetworkPlaceholder
	protected TemporalNetwork tn;										// temporal network

	@TemporalSolverPlaceholder
	protected TemporalSolver<TimePointQuery> solver;					// time point reasoner
	
	protected Set<TemporalInterval> intervals;							// set of created temporal intervals
	protected TemporalQueryFactory qf;									// temporal query factory
	protected TemporalConstraintFactory cf;	 							// temporal constraint factory
	
	// static information
	
	private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);
	
	/**
	 * 
	 */
	protected TemporalFacade() {
		super();
		
		// get query factory instance
		this.qf = new TemporalQueryFactory();
		this.cf = new TemporalConstraintFactory();
		this.intervals = new HashSet<>();
		
		// reset atomic id counter if needed
		ID_COUNTER.set(0);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getOrigin() {
		return this.tn.getOrigin();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon() {
		return this.tn.getHorizon();
	}
	
	/**
	 * Create a flexible time point
	 * 
	 * @return
	 * @throws TimePointCreationException
	 */
	public synchronized TimePoint createTimePoint() 
			throws TimePointCreationException 
	{
		// time point to create
		TimePoint point = null;
		try {
			// create a time point
			point = this.tn.addTimePoint();
		} catch (InconsistentDistanceConstraintException ex) {
			throw new TimePointCreationException(ex.getMessage());
		}
		// get time point
		return point;
	}
	
	/**
	 * Create a scheduled time point
	 * 
	 * @param at
	 * @return
	 * @throws TimePointCreationException
	 */
	public synchronized TimePoint createTimePoint(long at) 
			throws TimePointCreationException {
		// time point to create
		TimePoint point = null;
		try {
			// create a fixed time point
			point = this.tn.addTimePoint(at);
		} catch (InconsistentTpValueException | InconsistentDistanceConstraintException  ex) {
			throw new TimePointCreationException(ex.getMessage());
		}
		// get time point
		return point;
	}
	
	/**
	 * Create a flexible time point within the specified bound
	 * 
	 * @param bounds
	 * @return
	 * @throws TimePointCreationException
	 */
	public synchronized TimePoint createTimePoint(long[] bounds) 
			throws TimePointCreationException {
		// time point to create
		TimePoint point = null;
		try {
			// create a time point within bound
			point = this.tn.addTimePoint(bounds[0], bounds[1]);
		} catch (InconsistentDistanceConstraintException | InconsistentTpValueException ex) {
			throw new TimePointCreationException(ex.getMessage());
		}
		// get created time point
		return point;
	}

	/**
	 * Create a controllable temporal interval
	 * 
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	public synchronized TemporalInterval createTemporalInterval(boolean controllable) 
			throws TemporalIntervalCreationException {
		// create temporal interval
		return this.createTemporalInterval(new long[] {this.getOrigin(), this.getHorizon()}, 
				new long[] {this.getOrigin(), this.getHorizon()}, 
				new long[] {1, this.getHorizon()}, 
				controllable);		
	}
	
	/**
	 * Create a flexible interval with duration within the specified bound
	 * 
	 * @param duration
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	public synchronized TemporalInterval createTemporalInterval(long[] duration, boolean controllable) 
			throws TemporalIntervalCreationException 
	{
		// create temporal interval
		return this.createTemporalInterval(new long[] {this.getOrigin(), this.getHorizon()}, 
				new long[] {this.getOrigin(), this.getHorizon()}, 
				duration, 
				controllable);
	}
	
	/**
	 * 
	 * @param end
	 * @param duration
	 * @param controllable
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	public synchronized TemporalInterval createTemporalInterval(long[] end, long[] duration, boolean controllable) 
			throws TemporalIntervalCreationException 
	{
		// create temporal interval
		return this.createTemporalInterval(new long[] {this.getOrigin(), this.getHorizon()}, 
				end, 
				duration, 
				controllable);
	}
	
	/**
	 * 
	 * @param end
	 * @param duration
	 * @param controllable
	 * @return
	 * @throws TemporalIntervalCreationException
	 */
	public synchronized TemporalInterval createTemporalInterval(long[] start, long[] end, long[] duration, boolean controllable) 
			throws TemporalIntervalCreationException {
		
		// interval's start time
		TimePoint s = null;
		try {
			
			// create flexible start time
			s = this.tn.addTimePoint(start[0], start[1]);
			
		} catch (InconsistentDistanceConstraintException | InconsistentTpValueException ex) {
			// throw exception
			throw new InconsistentIntervalStartTimeException(ex.getMessage());
		}
		
		// interval's end time
		TimePoint e = null;
		try {
			
			// create flexible end time
			e = this.tn.addTimePoint(end[0], end[1]);
			
		} catch (InconsistentDistanceConstraintException | InconsistentTpValueException ex) {
			// delete start time
			this.tn.removeTimePoint(s);
			throw new InconsistentIntervaEndTimeException(ex.getMessage());
		}
		
		// interval's duration
		TimePointDistanceConstraint d = null;
		try {
			
			// create distance constraint
			d = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
			d.setReference(s);
			d.setTarget(e);
			d.setDistanceLowerBound(Math.max(1, duration[0]));
			d.setDistanceUpperBound(Math.min(duration[1], this.getHorizon()));
			d.setControllable(controllable);
			
			// propagate distance constraint
			this.tn.addDistanceConstraint(d);
			
		} catch (InconsistentDistanceConstraintException ex) {
			
			// remove start and end time points
			this.tn.removeTimePoint(s);
			this.tn.removeTimePoint(e);
			// throw exception
			throw new InconsistentIntervalDurationException(ex.getMessage());
		}
		
		// create temporal interval 
		TemporalInterval interval = new TemporalInterval(ID_COUNTER.getAndIncrement(), d);
		// record interval
		this.intervals.add(interval);
		// get created interval
		return interval;
	}
	
	/**
	 * 
	 * Remove a temporal interval from the temporal data-based. 
	 * 
	 * The method removes the time points of the interval and all related time point distance constraints from the underlying temporal network
	 * 
	 * @param i
	 */
	public synchronized void deleteTemporalInterval(TemporalInterval i) {
		// list of time points to remove
		List<TimePoint> list = new ArrayList<>();
		// get start time
		list.add(i.getStartTime());
		// get end time
		list.add(i.getEndTime());
		// the network will automatically remove all constraints concerning the two time points
		this.tn.removeTimePoints(list);
		// remove interval 
		this.intervals.remove(i);
	}
	
	/**
	 * Check the temporal consistency of the temporal information as well as the 
	 * pseudo-controllability property of the STNU.
	 * 
	 * 
	 */
	public synchronized void verify() 
			throws ConsistencyCheckException {
		
		// check temporal network consistency
		if (!this.solver.isValid()) {
			throw new TemporalConsistencyException("The STNU is not valid!\nCheck propagated temporal constraints...\n");
		}
		
		// check also if the network is valid
		if (!this.isPseudoControllable()) {
			throw new PseudoControllabilityException("The STNU is not pseudo-controllable!\nCheck constraints on uncontrollable intervals...\n");
		}
	}
	
	/**
	 * 
	 * @throws TemporalConsistencyException
	 */
	public synchronized void verifyTemporalConsistency() 
			throws TemporalConsistencyException {
		
		// check temporal network consistency
		if (!this.solver.isValid()) {
			throw new TemporalConsistencyException("The STNU is not valid!\nCheck propagated temporal constraints...\n");
		}
	}
	
	/**
	 * 
	 * @throws PseudoControllabilityException
	 */
	public synchronized void verifyPseudocontrollability() 
			throws PseudoControllabilityException {
		
		// check also if the network is valid
		if (!this.isPseudoControllable()) {
			throw new PseudoControllabilityException("The STNU is not pseudo-controllable!\nCheck constraints on uncontrollable intervals...\n");
		}
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public synchronized <T extends TemporalQuery> T createTemporalQuery(TemporalQueryType type) {
		// query instance
		T query = this.qf.create(type);
		// get created instance
		return query;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public synchronized <T extends TemporalConstraint> T createTemporalConstraint(TemporalConstraintType type) {
		// create constraint
		T cons = this.cf.create(type);
		// get created constraint
		return cons;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void process(TemporalQuery query) 
	{
		// check query type
		switch (query.getType()) 
		{
			// check distance between intervals
			case INTERVAL_DISTANCE : 
			{
				// get query
				IntervalDistanceQuery dQuery = (IntervalDistanceQuery) query;
				// get intervals
				TemporalInterval a = dQuery.getSource();
				TemporalInterval b = dQuery.getTarget();
				// create time point query
				TimePointDistanceQuery tpQuery = this.qf.create(TemporalQueryType.TP_DISTANCE);
				// set source and target
				tpQuery.setSource(a.getEndTime());
				tpQuery.setTarget(b.getStartTime());
				// process query
				this.solver.process(tpQuery);
				// set bounds
				dQuery.setDistanceLowerBound(tpQuery.getDistanceLowerBound());
				dQuery.setDistanceUpperBOund(tpQuery.getDistanceUpperBound());
			}
			break;
			
			// check overlapping intervals
			case INTERVAL_OVERLAP :
			{
				// get query
				IntervalOverlapQuery overlap = (IntervalOverlapQuery) query;
				// get intervals
				TemporalInterval a = overlap.getReference();
				TemporalInterval b = overlap.getTarget();
				
				// check the distance between the time point of A and the start time point of B
				TimePointDistanceQuery eAsB = this.qf.create(TemporalQueryType.TP_DISTANCE);
				eAsB.setSource(a.getEndTime());
				eAsB.setTarget(b.getStartTime());
				// process query
				this.process(eAsB);
				// check computed bounds
				long dmin = eAsB.getDistanceLowerBound();
				long dmax = eAsB.getDistanceUpperBound();
				// check if can overlap
				boolean o1 = !((dmin <= 0 && dmax <= 0) || (dmin >= 0 && dmax >= 0));
				
				
				// get intervals
				a = overlap.getTarget();
				b = overlap.getReference();
				
				// check the distance between the time point of A and the start time point of B
				TimePointDistanceQuery eBsA = this.qf.create(TemporalQueryType.TP_DISTANCE);
				eBsA.setSource(a.getEndTime());
				eBsA.setTarget(b.getStartTime());
				// process query
				this.process(eBsA);
				// check computed bounds
				dmin = eBsA.getDistanceLowerBound();
				dmax = eBsA.getDistanceUpperBound();
				// check if can overlap
				boolean o2 = !((dmin <= 0 && dmax <= 0) || (dmin >= 0 && dmax >= 0));
				
		
				// set result
				overlap.setCanOverlap(o1 || o2);
				// print logging message
				debug("[" + this.getClass().getName() + "] Processing query INTERVAL_OVERLAP:\n"
						+ "- Temporal Interval (A): " + a + "\n"
						+ "- Temporal Interval (B): " + b + "\n"
						+ "- Computed (flexible) distance: [dmin= " + dmin + ", dmax= " + dmax +"]\n"
						+ "- Answer to query: " + overlap.canOverlap());
			}
			break;
			
			// check interval schedule 
			case INTERVAL_SCHEDULE : 
			{
				// get query
				IntervalScheduleQuery scheduleQuery = (IntervalScheduleQuery) query;
				// get interval
				TemporalInterval i = scheduleQuery.getInterval();
				
				// create time point bound query
				TimePointScheduleQuery sQuery = this.qf.create(TemporalQueryType.TP_SCHEDULE);
				// set point 
				sQuery.setTimePoint(i.getStartTime());
				// check start schedule
				this.solver.process(sQuery);
				
				// process end time
				TimePointScheduleQuery eQuery = this.qf.create(TemporalQueryType.TP_SCHEDULE);
				// set point
				eQuery.setTimePoint(i.getEndTime());
				// check end schedule
				this.solver.process(eQuery);
				
				// check time point distance
				TimePointDistanceQuery dQuery= this.qf.create(TemporalQueryType.TP_DISTANCE);
				// set points
				dQuery.setSource(i.getStartTime());
				dQuery.setTarget(i.getEndTime());
				// process query
				this.solver.process(dQuery);
				
				// set interval duration
				i.setDurationLowerBound(dQuery.getDistanceLowerBound());
				i.setDurationUpperBound(dQuery.getDistanceUpperBound());
			}
			break;
			
			// check if squeezed interval
			case INTERVAL_PSEUDO_CONTROLLABILITY : 
			{
				// get query
				IntervalPseudoControllabilityQuery pseudoQuery = (IntervalPseudoControllabilityQuery) query;
				// get temporal interval
				TemporalInterval i = pseudoQuery.getInterval();
				// check the schedule
				IntervalScheduleQuery squery = this.qf.create(TemporalQueryType.INTERVAL_SCHEDULE);
				squery.setInterval(i);
				this.process(squery);
				// check if pseudo-controllability condition
				pseudoQuery.setPseudoControllable(i.getDurationLowerBound() == i.getNominalDurationLowerBound() && 
						i.getDurationUpperBound() == i.getNominalDurationUpperBound());
			}
			break;
			
			// time point queries
			case TP_SCHEDULE :
			case TP_DISTANCE : 
			case TP_DISTANCE_TO_HORIZON : {
				
				// propagate time point query to the reasoner
				this.solver.process((TimePointQuery) query);
			}
			break;
		}
	}
	
	/**
	 * This method propagates an interval constraint to the underlying temporal network.
	 * 
	 * When propagating constraints no contingent link can be affected or overwritten. Thus,
	 * the method throws an exception if a contingent link is affected by the propagated 
	 * constraint.
	 * 
	 * @param constraint
	 * @throws Exception
	 */
	public synchronized void propagate(TemporalConstraint constraint) 
			throws TemporalConstraintPropagationException 
	{
		try 
		{
			// check temporal constraint type
			switch (constraint.getType()) 
			{
				// create BEFORE constraint into the temporal network
				case BEFORE : 
				{
					// get constraint
					BeforeIntervalConstraint before = (BeforeIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = new TimePointDistanceConstraint[] { 
						this.doPropagateBeforeConstraint(before)
					};
					
					// set propagated constraint
					before.setPropagatedConstraints(c);
				}
				break;
				
				// create AFTER constraint into the temporal network
				case AFTER : 
				{
					AfterIntervalConstraint after = (AfterIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = new TimePointDistanceConstraint[] {
						this.doPropagateAfterConstraint(after)
					};
					// set propagated constraint
					after.setPropagatedConstraints(c);
				}
				break;
				
				// create MEETS constraint into the temporal network
				case MEETS : 
				{
					// get constraint
					MeetsIntervalConstraint meets = (MeetsIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = new TimePointDistanceConstraint[] { 
							this.doPropagateMeetsConstraint(meets)	
					};
					// set propagated constraints
					meets.setPropagatedConstraints(c);
				}
				break;
				
				// create MET-BY constraint into the temporal network
				case MET_BY : 
				{
					// get constraint
					MetByIntervalConstraint metby = (MetByIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = new TimePointDistanceConstraint[] {
							this.doPropagateMetByConstraint(metby)
					};
					// set propagated constraints
					metby.setPropagatedConstraints(c);
				}
				break;
				
				// create CONTAINS constraint into the temporal network
				case CONTAINS : 
				{
					// get constraint
					ContainsIntervalConstraint contains = (ContainsIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = this.doPropagateContainsConstraint(contains);
					// set propagated constraints
					contains.setPropagatedConstraints(c);
				}
				break;
				
				// create DURING constraint into the temporal network
				case DURING : 
				{
					// get constraint
					DuringIntervalConstraint during = (DuringIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = this.doPropagateDuringConstraint(during);
					// set propagated constraints
					during.setPropagatedConstraints(c);
				}
				break;
				
				// create STARTS-DURING constraint into the network
				case STARTS_DURING : 
				{
					// get constraint
					StartsDuringIntervalConstraint sdc = (StartsDuringIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = this.doPropagateStartsDuringConstraint(sdc);
					// set propagated constraints
					sdc.setPropagatedConstraints(c);
				}
				break;
				
				// create ENDS-DURING constraint into the network
				case ENDS_DURING : 
				{
					// get constraint
					EndsDuringIntervalConstraint edc = (EndsDuringIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = this.doPropagateEndsDuringConstraint(edc);
					// set propagated constraints
					edc.setPropagatedConstraints(c);
				}
				break;
				
				// create EQUALS constraint into the temporal network
				case EQUALS : 
				{
					// get constraint
					EqualsIntervalConstraint equals = (EqualsIntervalConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint[] c = this.doPropagateEqualsConstraint(equals);
					// set propagated constraint
					equals.setPropagatedConstraints(c);
				}
				break;
				
				// set the duration of a temporal interval
				case FIX_INTERVAL_DURATION : 
				{
					// get constraint
					FixIntervalDurationConstraint fix = (FixIntervalDurationConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint c = this.doPropagateFixIntervalDurationConstraint(fix);
					// set propagate constraints
					fix.setPropagatedConstraints(new TimePointDistanceConstraint[] {c});
				}
				break;
				
				// schedule a time point at a given time
				case FIX_TIME_POINT :
				{
					// get constraint
					FixTimePointConstraint fix = (FixTimePointConstraint) constraint;
					// propagate constraint
					TimePointDistanceConstraint c = this.doPropagateFixTimePointConstraint(fix);
					// set propagated constraint
					fix.setPropagatedConstraints(new TimePointDistanceConstraint[] {c});
				}
				break;
				
				// set a distance constraint between two time points
				case TIME_POINT_DISTANCE :
				{
					// get constraint
					TimePointDistanceConstraint cons = (TimePointDistanceConstraint) constraint;
					// directly propagate distance constraint to the temporal network
					this.tn.addDistanceConstraint(cons);
				}
				break;
				
				default : { 
					throw new RuntimeException("Unknown temporal constraint!");
				}
			}
		}
		catch (InconsistentDistanceConstraintException ex) {
			throw new TemporalConstraintPropagationException(ex.getMessage());
		}
	}

	/**
	 * 
	 * @param constraint
	 * @throws Exception
	 */
	public synchronized void retract(TemporalConstraint constraint) {
		// retract propagated constraints
		TimePointDistanceConstraint[] toRetract = constraint.getPropagatedConstraints();
		// verify whether some constraints have been propagated
		if (toRetract != null) {
			// remove propagated distance constraints
			this.tn.removeDistanceConstraint(Arrays.asList(toRetract));
			// clear data structure
			constraint.clear();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "\nTemporal Network:\n"
				+ "" + this.tn.toString() + "\n"
				+ "--------------\n"
				+ "Solver:\n"
				+ "" + this.solver.toString() + "\n\n";
	}
	
	/**
	 * 
	 * @param equals
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint[] doPropagateEqualsConstraint(EqualsIntervalConstraint equals) 
			throws InconsistentDistanceConstraintException {
		
		// create distance constraint 
		TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		// set constraint data
		c1.setReference(equals.getReference().getStartTime());
		c1.setTarget(equals.getTarget().getStartTime());
		c1.setDistanceLowerBound(0);
		c1.setDistanceUpperBound(0);
		c1.setControllable(true);
		
		// create distance constraint
		TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		// set constraint data
		c2.setReference(equals.getReference().getEndTime());
		c2.setTarget(equals.getTarget().getEndTime());
		c2.setDistanceLowerBound(0);
		c2.setDistanceUpperBound(0);
		c2.setControllable(true);
		
		// add constraints
		this.tn.addDistanceConstraint(new TimePointDistanceConstraint[] {c1, c2});
		// get added distance constraints
		return new TimePointDistanceConstraint[] {c1, c2};
	}

	/**
	 * 
	 * @param contains
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint[] doPropagateContainsConstraint(ContainsIntervalConstraint contains) 
			throws InconsistentDistanceConstraintException {
		
		// create distance constraint
		TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		// set data
		c1.setReference(contains.getReference().getStartTime());
		c1.setTarget(contains.getTarget().getStartTime());
		c1.setDistanceLowerBound(contains.getFirstBound()[0]);
		c1.setDistanceUpperBound(contains.getFirstBound()[1]);
		c1.setControllable(true);
		
		// create distance constraint
		TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		// set data
		c2.setReference(contains.getTarget().getEndTime());
		c2.setTarget(contains.getReference().getEndTime());
		c2.setDistanceLowerBound(contains.getSecondBound()[0]);
		c2.setDistanceUpperBound(contains.getSecondBound()[1]);
		c2.setControllable(true);
		
		// add constraints
		this.tn.addDistanceConstraint(new TimePointDistanceConstraint[] {c1, c2});
		// get added constraints
		return new TimePointDistanceConstraint[] {c1, c2};
	}

	/**
	 * 
	 * @param during
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint[] doPropagateDuringConstraint(DuringIntervalConstraint during) 
			throws InconsistentDistanceConstraintException {
		
		// create constraint
		TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c1.setReference(during.getTarget().getStartTime());
		c1.setTarget(during.getReference().getStartTime());
		c1.setDistanceLowerBound(during.getFirstBound()[0]);
		c1.setDistanceUpperBound(during.getFirstBound()[1]);
		c1.setControllable(true);
		
		// create constraint
		TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c2.setReference(during.getReference().getEndTime());
		c2.setTarget(during.getTarget().getEndTime());
		c2.setDistanceLowerBound(during.getSecondBound()[0]);
		c2.setDistanceUpperBound(during.getSecondBound()[1]);
		c2.setControllable(true);
				
		// add constraint
		this.tn.addDistanceConstraint(new TimePointDistanceConstraint[] {c1, c2});
		// get added constraints
		return new TimePointDistanceConstraint[] {c1, c2};
	}
	
	/**
	 * 
	 * @param edc
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint[] doPropagateEndsDuringConstraint(EndsDuringIntervalConstraint edc) 
			throws InconsistentDistanceConstraintException {
		
		// create constraint
		TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c1.setReference(edc.getTarget().getStartTime());
		c1.setTarget(edc.getReference().getEndTime());
		c1.setDistanceLowerBound(edc.getFirstTimeBound()[0]);
		c1.setDistanceUpperBound(edc.getFirstTimeBound()[1]);
		c1.setControllable(true);
		
		// create constraint
		TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c2.setReference(edc.getReference().getEndTime());
		c2.setTarget(edc.getTarget().getEndTime());
		c2.setDistanceLowerBound(edc.getSecondTimeBound()[0]);
		c2.setDistanceUpperBound(edc.getSecondTimeBound()[1]);
		c2.setControllable(true);
		
		// add constraint 
		this.tn.addDistanceConstraint(new TimePointDistanceConstraint[] {c1, c2});
		// propagate constraints
		return new TimePointDistanceConstraint[] {c1, c2};
	}

	/**
	 * 
	 * @param sdc
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint[] doPropagateStartsDuringConstraint(StartsDuringIntervalConstraint sdc) 
			throws InconsistentDistanceConstraintException 
	{
		// create constraint
		TimePointDistanceConstraint c1 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c1.setReference(sdc.getTarget().getStartTime());
		c1.setTarget(sdc.getReference().getStartTime());
		c1.setDistanceLowerBound(sdc.getFirstTimeBound()[0]);
		c1.setDistanceUpperBound(sdc.getFirstTimeBound()[1]);
		c1.setControllable(true);
		
		// create constraint
		TimePointDistanceConstraint c2 = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE); 
		c2.setReference(sdc.getReference().getStartTime());
		c2.setTarget(sdc.getTarget().getEndTime());
		c2.setDistanceLowerBound(sdc.getSecondTimeBound()[0]);
		c2.setDistanceUpperBound(sdc.getSecondTimeBound()[1]);
		c2.setControllable(true);
		
		// add constraint
		this.tn.addDistanceConstraint(new TimePointDistanceConstraint[] {c1, c2});
		// propagate constraints
		return new TimePointDistanceConstraint[] {c1, c2};
	}

	/**
	 * 
	 * @param before
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint doPropagateBeforeConstraint(BeforeIntervalConstraint before)
			throws InconsistentDistanceConstraintException {
		
		// create constraint
		TimePointDistanceConstraint c = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c.setReference(before.getReference().getEndTime());
		c.setTarget(before.getTarget().getStartTime());
		c.setDistanceLowerBound(before.getLowerBound());
		c.setDistanceUpperBound(before.getUpperBound());
		c.setControllable(true);
		
		// add constraint
		this.tn.addDistanceConstraint(c);
		// get propagated constraint
		return c;
	}
	
	/**
	 * 
	 * @param meets
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint doPropagateMeetsConstraint(MeetsIntervalConstraint meets) 
			throws InconsistentDistanceConstraintException {
		
		// create constraint
		TimePointDistanceConstraint c = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c.setReference(meets.getReference().getEndTime());
		c.setTarget(meets.getTarget().getStartTime());
		c.setDistanceLowerBound(0);
		c.setDistanceUpperBound(0);
		c.setControllable(true);
		
		// add constraint
		this.tn.addDistanceConstraint(c);
		// get propagated constraint
		return c;
	}
	
	/**
	 * 
	 * @param after
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint doPropagateAfterConstraint(AfterIntervalConstraint after) 
			throws InconsistentDistanceConstraintException {
		
		// create constraint
		TimePointDistanceConstraint c = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c.setReference(after.getTarget().getEndTime());
		c.setTarget(after.getReference().getStartTime());
		c.setDistanceLowerBound(after.getLowerBound());
		c.setDistanceUpperBound(after.getUpperBound());
		c.setControllable(true);
		
		// add constraint
		this.tn.addDistanceConstraint(c);
		// get propagated constraint
		return c;
	}
	
	/**
	 * 
	 * @param metby
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint doPropagateMetByConstraint(MetByIntervalConstraint metby) 
			throws InconsistentDistanceConstraintException {
		
		// create constraint
		TimePointDistanceConstraint c = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c.setReference(metby.getTarget().getEndTime());
		c.setTarget(metby.getReference().getStartTime());
		c.setDistanceLowerBound(0);
		c.setDistanceUpperBound(0);
		c.setControllable(true);
		
		// add constraint
		this.tn.addDistanceConstraint(c);
		// get propagated constraint
		return c;
	}
	
	/**
	 * 
	 * @param fix
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint doPropagateFixTimePointConstraint(FixTimePointConstraint fix) 
			throws InconsistentDistanceConstraintException {
		
		// create constraint
		TimePointDistanceConstraint c = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c.setReference(this.tn.getOriginTimePoint());
		c.setTarget(fix.getReference());
		c.setDistanceLowerBound(fix.getTime());
		c.setDistanceUpperBound(fix.getTime());
		c.setControllable(true);
		
		// add constraint
		this.tn.addDistanceConstraint(c);
		// get propagated constraint
		return c;
	}

	/**
	 * 
	 * @param reference
	 * @param duration
	 * @return
	 * @throws InconsistentDistanceConstraintException
	 */
	protected TimePointDistanceConstraint doPropagateFixIntervalDurationConstraint(FixIntervalDurationConstraint fix) 
			throws InconsistentDistanceConstraintException {
		
		// create constraint
		TimePointDistanceConstraint c = this.cf.create(TemporalConstraintType.TIME_POINT_DISTANCE);
		c.setReference(fix.getReference().getStartTime());
		c.setTarget(fix.getReference().getEndTime());
		c.setDistanceLowerBound(fix.getDuration());
		c.setDistanceUpperBound(fix.getDuration());
		c.setControllable(true);
		
		// add constraint
		this.tn.addDistanceConstraint(c);
		// get propagated constraint
		return c;
	}
	
	/**
	 * This method checks if the STNU is pseudo-controllable. 
	 * 
	 *	This procedure checks if contingent links have been "squeezed"
	 *	so, the STNU is pseudo-controllable if no assumption has been 
	 *	made on the duration of uncontrollable constraints (i.e. contingent
	 *	links).
	 *
	 * @return
	 */
	private boolean isPseudoControllable() {
		
		// hypothesis
		boolean pseudoControllable = true;
		
		// get constraints
		for (TimePointDistanceConstraint constraint : this.tn.getContingentConstraints()) {
			
			// check contingent constraints
			if (!constraint.isControllable()) {
				
				// create query
				TimePointDistanceQuery query = this.qf.create(TemporalQueryType.TP_DISTANCE);
				query.setSource(constraint.getReference());
				query.setTarget(constraint.getTarget());
				// process query
				this.solver.process(query);
				
				// get actual bounds
				long dmin = query.getDistanceLowerBound();
				long dmax = query.getDistanceUpperBound();
				
				// check duration
				pseudoControllable = !(dmin > constraint.getDistanceLowerBound() || dmax < constraint.getDistanceUpperBound());
			}
			
			// check if pseudo-controllable
			if (!pseudoControllable) {
				break;
			}
		}
				
		// get result
		return pseudoControllable;
	}

	/**
	 * 
	 * For debugging only
	 * 
	 */
	public void printDiagnosticData() {
		this.tn.printDiagnosticData();
		this.solver.printDiagnosticData();
	}
}
