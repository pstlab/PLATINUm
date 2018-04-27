package it.istc.pst.platinum.executive.dc.tga;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import it.istc.pst.platinum.executive.dc.PlanExecutionStatus;
import it.istc.pst.platinum.executive.dc.tga.clocks.TGAClockControl;
import it.istc.pst.platinum.executive.dc.tga.objects.TGAPlanStatus;
import it.istc.pst.platinum.executive.dc.tga.strategy.TGAStrategy;

public class TGAManager {
	private TGAPlanStatus istant;    
	private boolean notified; 

	public TGAManager(TGAStrategy strategy, PlanExecutionStatus status) throws FileNotFoundException{
		this.istant = new TGAPlanStatus(new TGAClockControl(clocks(strategy), 
				strategy.getHorizon()), status); 
		this.notified = false; 
	}


	public Set<String> clocks(TGAStrategy strategy){
		Set<String> clocks = new HashSet<String>(); 

		for (String a : strategy.stateVariables()){
			clocks.add(a.concat(a));
		}
		clocks.addAll(strategy.getRelations().getRelations());

		return clocks; 
	}

	public void run(TGAStrategy strategy) {
		this.istant.increaseClocks();
		this.istant.setAction(null);
		this.istant = strategy.goOn(istant);
	}

	public TGAPlanStatus getIstant(){
		return this.istant; 
	}
	
	public void setInstant(TGAPlanStatus istant){
		this.istant = istant; 
	}
	
	public boolean getNotified(){
		return this.notified; 
	}
	public void setNotified(boolean notified){
		this.notified = notified;
	}
	public boolean isComplete(){
		return this.istant.isFinalStatus();
	}

}
