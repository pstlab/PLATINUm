package it.cnr.istc.pst.platinum.domain.component.sv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.cnr.istc.pst.platinum.ai.framework.domain.DomainComponentBuilder;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.ExternalStateVariable;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariableValue;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryFactory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacade;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacadeBuilder;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolverType;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacadeBuilder;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.FixIntervalDurationConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintFactory;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalScheduleQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolverType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetworkType;

/**
 * 
 * @author anacleto
 *
 */
@TemporalFacadeConfiguration(
		network = TemporalNetworkType.STNU,
		solver = TemporalSolverType.APSP
)
@ParameterFacadeConfiguration(
		solver = ParameterSolverType.CHOCHO_SOLVER
)
public class ExternalStateVariableComponentTestCase {
	
	private static final int ORIGIN = 0;
	private static final int HORIZON = 100;
	private TemporalFacade tf;
	private ParameterFacade pf;
	private ExternalStateVariable psv;

	/**
	 * 
	 */
	@BeforeEach
	public void init() {
		System.out.println("**********************************************************************************");
		System.out.println("************************* State Variable Component Test Case ***********************");
		System.out.println("**********************************************************************************");
		
		// create temporal facade
		this.tf = TemporalFacadeBuilder.createAndSet(this, ORIGIN, HORIZON);
		// create parameter facade
		this.pf = ParameterFacadeBuilder.createAndSet(this);
		
		// create State Variable
		this.psv = DomainComponentBuilder.createAndSet("EX_SV1", DomainComponentType.SV_EXTERNAL, this.tf, this.pf);
	}
	
	/**
	 * 
	 */
	@AfterEach
	public void clear() {
		this.psv = null;
		this.tf = null;
		this.pf = null;
		System.gc();
		System.out.println();
		System.out.println("**********************************************************************************");
		System.out.println();
	}
	
	/**
	 * 
	 */
	@Test
	public void createExternalStateVariableTest() {
		System.out.println("[Test]: createExternalStateVariableTest() --------------------");
		System.out.println();
		
		// check state variable object
		assertNotNull(this.psv);
		// create state variable description
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1");
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long [] {10, 30}, true);
		StateVariableValue v3 = this.psv.addStateVariableValue("Val-3", new long[] {11, 25}, false);
		// add transitions
		this.psv.addValueTransition(v1, v2);
		this.psv.addValueTransition(v2, v3);
		System.out.println(this.psv);
	}
	
	/**
	 * 
	 */
	@Test
	public void addDecisionsTest() {
		System.out.println("[Test]: addDecisionsTest() --------------------");
		System.out.println();
		// check state variable object
		assertNotNull(this.psv);
		// create the state variable description
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1", new long[] {5, 5}, true);
		StateVariableValue v2 = this.psv.addStateVariableValue("Val-2", new long[] {10, 30}, true);
		// add transitions
		this.psv.addValueTransition(v1, v2);
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {}, 
					new long[] {10, 10}, new long[] {15, 15}, v1.getDurationBounds());
			this.psv.activate(d1);
			Decision d2 = this.psv.create(v2, new String[] {});
			this.psv.activate(d2);
			Decision d3 = this.psv.create(v1, new String[] {});
			this.psv.activate(d3);
			
			// check consistency
			this.tf.verify();
			this.pf.verify();
			
			// check flexible schedule
			TemporalQueryFactory qFactory = new TemporalQueryFactory();
			// create flexible schedule query
			IntervalScheduleQuery query = qFactory.create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(d1.getToken().getInterval());
			// process query
			this.tf.process(query);
			
			// print decision
			System.out.println(d1);
			// check bounds
			assertTrue(d1.getStart()[0] == 10);
			assertTrue(d1.getStart()[1] == 10);
			assertTrue(d1.getDuration()[0] == 5);
			assertTrue(d1.getDuration()[1] == 5);
			
			// create query
			query = qFactory.create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(d2.getToken().getInterval());
			// process query
			this.tf.process(query);
			
			// print decision
			System.out.println(d2);
			// check bounds
			assertTrue(d2.getStart()[0] == this.tf.getOrigin());
			assertTrue(d2.getStart()[1] == (this.tf.getHorizon() - v2.getDurationBounds()[0]));
			assertTrue(d2.getDuration()[0] == v2.getDurationBounds()[0]);
			assertTrue(d2.getDuration()[1] == v2.getDurationBounds()[1]);
			
			// print state variable information
			System.out.println();
			System.out.println(this.psv);
			System.out.println(this.tf);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void addObservationTest() {
		System.out.println("[Test]: addObservationTest() --------------------");
		System.out.println();
		// check state variable object
		assertNotNull(this.psv);
		// create the state variable description
		StateVariableValue v1 = this.psv.addStateVariableValue("Val-1", new long[] {2, 10}, true);
		System.out.println(this.psv);
		System.out.println(this.tf);
		try 
		{
			// create tokens
			Decision d1 = this.psv.create(v1, new String[] {}, new long[] {15, 15}, v1.getDurationBounds());
			this.psv.activate(d1);
			// print decision
			System.out.println(d1 + " - duration bounds: [" + d1.getDuration()[0] + ", " + d1.getDuration()[1] + "]");
			
			// check consistency
			this.tf.verify();
			this.pf.verify();
			
			// check flexible schedule
			TemporalQueryFactory qFactory = new TemporalQueryFactory();
			// create flexible schedule query
			IntervalScheduleQuery query = qFactory.create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(d1.getToken().getInterval());
			// process query
			this.tf.process(query);
			// print decision
			System.out.println(d1 + " - duration bounds: [" + d1.getDuration()[0] + ", " + d1.getDuration()[1] + "]");
			
			try 
			{
				TemporalConstraintFactory iFactory = new TemporalConstraintFactory();
				FixIntervalDurationConstraint constraint = iFactory.create(TemporalConstraintType.FIX_INTERVAL_DURATION);
				constraint.setReference(d1.getToken().getInterval());
				constraint.setDuration(12);
				// propagate observation
				this.tf.propagate(constraint);
				assertTrue(false);
			}
			catch (Exception ex) {
				System.out.println("\n> " + ex.getMessage() + "\n");
			}
			
			// check consistency
			this.tf.verify();
			this.pf.verify();
			
			// check schedule
			query = qFactory.create(TemporalQueryType.INTERVAL_SCHEDULE);
			query.setInterval(d1.getToken().getInterval());
			// process query
			this.tf.process(query);
			System.out.println(d1 + " - duration bounds: [" + d1.getDuration()[0] + ", " + d1.getDuration()[1] + "]");
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			assertTrue(false);
		}
	}
	
}
