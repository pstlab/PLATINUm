package it.istc.pst.platinum.executive.dc.tga.objects;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import it.istc.pst.platinum.executive.dc.DCResult;
import it.istc.pst.platinum.executive.dc.DCResultType;
import it.istc.pst.platinum.executive.dc.DispatchDCResult;
import it.istc.pst.platinum.executive.dc.PlanExecutionStatus;
import it.istc.pst.platinum.executive.dc.WaitDCResult;
import it.istc.pst.platinum.executive.dc.tga.clocks.TGAClockControl;
import it.istc.pst.platinum.executive.dc.tga.conditions.TGAConditions;

public class TGAPlanStatus {
	private PlanExecutionStatus status;
	private TGATransition nextStatus; 
	private TGAClockControl clocks; 
	private DCResult message; 

	public TGAPlanStatus(TGAClockControl clocks, PlanExecutionStatus status) throws FileNotFoundException{
		this.clocks = clocks; 
		this.status = status;
		this.message = null; 
		nextStatus = null;

	}

	public void increaseClocks(){
		this.clocks.increaseClocks();
	}

	/*This method does the transition, given a condition and an action.*/
	private void transition(TGAConditions condition, TGATransition action) throws Exception{
		if (condition.isVerified(this.clocks)){ 
			this.clocks.setNewValues(action.getNewValues());
		}
	}


	public void evaluateCondition(TGAConditions condition) throws Exception{
		if (this.message == null || this.message.getType()!=DCResultType.FAILURE){
			if (condition.getCondition().contains("When") && condition.isVerified(this.clocks)){ 
					transition(condition, this.nextStatus);

					Map<String, String> tokensToDispatch = new HashMap<String,String>(); 
					tokensToDispatch = this.nextStatus.getInitialStates();
					for (String a : tokensToDispatch.keySet()){
						if (tokensToDispatch.get(a).equals(""))
							tokensToDispatch.remove(a);
					}
						this.message = new DispatchDCResult(tokensToDispatch);
				} else {
					if (condition.getCondition().contains("While") && condition.isVerified(this.clocks)){ 
						this.message = new WaitDCResult(condition.getPlanClock());

				}
			}
		}
	}

	public PlanExecutionStatus getPlanExecutionStatus(){
		return this.status; 
	}

	public TGATransition getAction(){
		return this.nextStatus;
	}

	public void setAction(TGATransition action){
		this.nextStatus = action; 
	}

	public DCResult getMessage(){
		return this.message; 
	}

	public TGAClockControl getClocks(){
		return this.clocks; 
	}

	public void uncontrollable(Map<String, String> status2) {
		for (String s : this.status.getStatus().keySet()){
			this.status.getStatus().put(s, status2.get(s));
		}
		this.nextStatus = null;

	}

	public boolean isFinalStatus() {
		boolean finish = true;
		for (String s: this.status.getStatus().keySet()){
			finish = finish && this.status.getStatus().get(s).equals("finish");
		}
		return finish; 
	}


	public void setPlanExecutionStatus(PlanExecutionStatus status2) {
		this.status = status2; 
	}

	public void setMessage(DCResult message){
		this.message = message;
	}

	public boolean isVerified(TGAConditions condition) {
		return condition.isVerified(this.clocks);
	}		
}
