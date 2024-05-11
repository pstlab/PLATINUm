package it.cnr.istc.pst.platinum.testing.executive.dc.strategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.executive.dc.strategy.ListStrategy;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Transition;

public class TestListStrategy {
	
	//using simple-non-alloc
			//horizon = 100
			//plan {
			//	timelines {
			//		pm
			//		{ token 1 { earth [10,20] [10,20] }
			//		unallocated 2
			//		{  [70,80] [50,70] }
			//		token 3 { slewing [100,100] [20,30] }
			//		}
			//	}
			//	relations {
			//		#              gv 5 contains [0,infty][0,infty] pm 8
			//		pm 1 before [60,70] pm 3
			//	}
			//}
	
	public static void main(String[] args) {
		ListStrategy strategy;
		Map<String,String> initialState;
		Map<String,Long> tc;
		Map<Transition, Map<String, String>> uPostConditions;
		Set<String> uStates;
		
		//initialization
		initialState = new HashMap<>();
		initialState.put("pm","start");
		tc = new HashMap<>();
		tc.put("plan",(long) 0);
		tc.put("R1", (long) 0);
		tc.put("pm", (long) 0);
		uPostConditions = new HashMap<>(); //empty
		uStates = new HashSet<>(); //empty
		String s1 = "State: ( pm.pm3 ) ";
		List<String> ss1 = new ArrayList<>(); 
		ss1.add("While you are in	(plan_clock<100 && plan_clock-pm.pm_clock<=80 "
				+ "&& pm.pm_clock-plan_clock<=-70), wait.");
		ss1.add("When you are in (plan_clock==100 && 20<=pm.pm_clock && pm.pm_clock<=30), "
				+ "take transition pm.pm3->pm.finish { plan_clock >= 100 && pm_clock >= 20, tau, 1 }");
		String s2 = "State: ( pm.start )";
		List<String> ss2 = new ArrayList<>();
		ss2.add("When you are in (plan_clock==pm.pm_clock && pm.pm_clock==0), take transition "
				+ "pm.start->pm.pm1 { plan_clock == 0, tau, 1 }");
		String s3 = "State: ( pm.pm2 )";
		List<String> ss3 = new ArrayList<>();
		ss3.add("When you are in (70<=plan_clock && 260<=R1_clock && 50<=pm.pm_clock && plan_clock<=80 "
				+ "&& R1_clock<=270 && pm.pm_clock<=70), take transition pm.pm2->pm.pm3 { plan_clock >= "
				+ "70 && pm_clock >= 50 && R1_clock >= H + 60 && R1_clock <= H + 70, tau, pm_clock := 0 }");
		ss3.add("While you are in	(200<=R1_clock && plan_clock-R1_clock<=-180 && R1_clock<260 && "
				+ "R1_clock-plan_clock<=190 && R1_clock-pm.pm_clock==200), wait.");
		String s4 = "State: ( pm.pm1 ) ";
		List<String> ss4 = new ArrayList<>();
		ss4.add("When you are in (10<=plan_clock && 10<=pm.pm_clock && plan_clock<=20 && pm.pm_clock<=20),"
				+ " take transition pm.pm1->pm.pm2 { plan_clock >= 10 && pm_clock >= 10, tau, pm_clock := 0, "
				+ "R1_clock := H }");
		ss4.add("While you are in	(plan_clock<10 && plan_clock==pm.pm_clock), wait.");
		
		
		strategy = new ListStrategy(100);
		strategy.setInitialState(initialState);
		strategy.setuPostConditions(uPostConditions);
		strategy.setuStates(uStates);
		strategy.setTimelineClocks(tc);
		strategy.getStrategyAsStrings(s1,ss1);
		strategy.getStrategyAsStrings(s2, ss2);
		strategy.getStrategyAsStrings(s3, ss3);
		strategy.getStrategyAsStrings(s4, ss4);
		
		System.out.println(strategy + "\n");
		
		System.out.println(strategy.askAllStrategySteps(0, initialState,true)+"\n");
		initialState.replace("pm","pm1");
		System.out.println(strategy.askAllStrategySteps(1, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(2, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(3, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(4, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(5, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(6, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(7, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(8, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(9, initialState,true)+"\n");
		System.out.println(strategy.askAllStrategySteps(10, initialState,true)+"\n");
		initialState.replace("pm", "pm2");
		System.out.println(strategy.askAllStrategySteps(11, initialState,true)+"\n");		
	}

}
