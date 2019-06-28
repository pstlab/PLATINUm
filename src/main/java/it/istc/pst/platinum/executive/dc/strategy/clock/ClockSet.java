package it.istc.pst.platinum.executive.dc.strategy.clock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClockSet {

	List<ClockRelation> clockSet; //map reordered?

//------------------- CONSTRUCTORS ----------------------
	public ClockSet () {
		this.clockSet = new  ArrayList<>();
	}

	public ClockSet (String[] clockList) {
		this();
		for(String clock:clockList) {
			if(clock.length()>2) { 
				ClockRelation cr = new ClockRelation(clock.trim());
				this.clockSet.add(cr);
			}
		}
	}

//----------------- METHODS ------------------
	
	//evaluates if the conditions of the actual status respect the request for the strategy
	public boolean evaluate(Map <String,Long> status) throws Exception{
		Boolean result = true;
		for(ClockRelation cr : clockSet) {
			result = result && cr.verify(status);
		}
		return result;	
	}
	
//----------------- GETTERS/SETTERS --------------
	
	public List<ClockRelation> getClockSet() {
		return clockSet;
	}
	
	public void addClockRelation(ClockRelation clock) {
		this.clockSet.add(clock);
	}

	@Override
	public String toString() {
		return "ClockSet [" + clockSet + "]";
	}
}
