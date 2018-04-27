package it.istc.pst.platinum.executive.dc.tga.conditions;

import java.util.HashSet;
import java.util.Set;

import it.istc.pst.platinum.executive.dc.tga.clocks.TGAClock;
import it.istc.pst.platinum.executive.dc.tga.clocks.TGAClockControl;

public class TGAConditions extends TGACheck{

	/*Condition must be verified to take the transition.*/
	public TGAConditions(String condition){
		super(condition); 
		this.conditions = new HashSet<String>();
		setOfConditions(); 
	}

	private void setOfConditions(){
		String condition1 = this.condition;

		if (condition1.startsWith("While")){
			condition1 = condition1.substring(17).trim(); 
		} else { 
			if (condition1.startsWith("When")){
				condition1 = condition1.substring(16).trim();
			}		
		}
		String[] split = condition1.split("&&");
		split[0] = split[0].replace('(', ' ');
		split[split.length-1] = split[split.length-1].replace(')',' ');
		
		for (int i = 0; i < split.length; i++){
			split[i] = split[i].trim();
			this.conditions.add(split[i]);
		}

		adjust();

	}
	
	public boolean isWhen(){
		return this.condition.startsWith("When");
	}

	public boolean isVerified(TGAClockControl clock){
		boolean verified = true; 
		String[] a; 
		for (String s: this.conditions){
			if ((a = numberOfClocks(clock, s)).length < 2)
				verified = verified && verify(s, clock);
			else verified = verified && verifyTwoClocks(clock, a[0], a[1], s);
		}

		return verified; 
	}

	private boolean verifyTwoClocks(TGAClockControl clocks, String firstClock, String secondClock, String condition){
		int i;
		boolean verified;
		if (startsWithANumber(condition)){
			if (condition.contains("<")) {
				i = Integer.parseInt(numbers(condition, 0));
				verified = (clocks.getClockWithName(firstClock).getTime() - 
						clocks.getClockWithName(secondClock).getTime() >= i); 
			} else verified = (clocks.getClockWithName(firstClock) == clocks.getClockWithName(secondClock)); 
		} else { 	
			if (condition.contains("<")) {
				i = Integer.parseInt(numbers(condition, firstClock.length()+secondClock.length()+1));
				verified = (clocks.getClockWithName(firstClock).getTime() - 
						clocks.getClockWithName(secondClock).getTime() <= i); 
			} else verified = (clocks.getClockWithName(firstClock) == clocks.getClockWithName(secondClock));}
		return verified; 
	}

	private boolean verify(String condition, TGAClockControl clock){
		boolean verified = false; 
		for(TGAClock c: clock.getClocks()){
			if (condition.contains(c.getClockName())){
				switch(symbols(condition)){
				case ">": {
					verified = c.getTime() > Integer.parseInt(numbers(condition, condition.indexOf(">")+1));} 
				break; 
				case ">=": {
					verified = verified && c.getTime() >= Integer.parseInt(numbers(condition, condition.indexOf(">=")+2));} 
				break; 
				case "==": {
					verified = verified && c.getTime() == Integer.parseInt(numbers(condition, condition.indexOf("==")+2));} 
				break; 
				case "<": {
					verified = verified && c.getTime() < Integer.parseInt(numbers(condition, condition.indexOf("<")+1));} 
				break; 
				case "<=": {
					verified = verified && c.getTime() <= Integer.parseInt(numbers(condition, condition.indexOf(">")+2));} 
				break; 
				}

			}
		}
		return verified;
	}

	public String[] numberOfClocks(TGAClockControl clocks, String condition){
		String[] two = new String[2]; 
		int i = 0; 

		for (TGAClock c: clocks.getClocks()){
			if (condition.contains(c.getClockName())) two[i]=c.getClockName(); 
		}

		return two; 
	}

	public long getPlanClock(){
		int a = 0;
		for (String i : this.conditions){
			if (i.contains("plan_clock") && oneClock(i))
				a = Integer.parseInt(numbers(i, 0));
		}
		return a; 
	}

	private boolean oneClock(String condition){
		int one = 0;
		for (int i = 0; i < condition.length()-4; i++){
			for (int j = 4; j <condition.length(); j++){
				if (condition.substring(i, j).equals("clock")) one++;
			}
		}
		return one == 1; 
	}
	
	public Set<String> getConditions(){
		return this.conditions; 
	}
	public String getCondition(){
		return this.condition; 
	}
	
	public void setCondition(String condition){
		this.condition = condition; 
		setOfConditions();
	}

}
