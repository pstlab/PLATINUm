package it.istc.pst.platinum.executive.dc.tga.conditions;

import java.util.Set;

import it.istc.pst.platinum.executive.dc.tga.clocks.TGAClock;
import it.istc.pst.platinum.executive.dc.tga.clocks.TGAClockControl;

public class TGAGuards extends TGACheck {

	public TGAGuards(){
		super("");
	}

	private void putGuards(){
		String guards = this.condition;
		guards = guards.substring(0, guards.length()-1).trim();
		String[] splits = guards.split("&&");
		
		for (String split : splits){
			this.conditions.add(split);
		}

		adjust();
	}

	private boolean verify(TGAClock clock, long horizon) {
		boolean verified = false; 
		for (String c : this.conditions){
			if (c.contains(clock.getClockName())){
				if (c.contains("H")){
					verified = controlWithHorizon(clock, c, horizon); 
				}
				else {
					switch(symbols(c)){
					case ">": {
						verified = verified && clock.getTime() > Integer.parseInt(numbers(c, c.indexOf(">")+1));} 
					break; 
					case ">=": {
						verified = verified && clock.getTime() >= Integer.parseInt(numbers(c, c.indexOf(">=")+2));} 
					break; 
					case "==": {
						verified = verified && clock.getTime() == Integer.parseInt(numbers(c, c.indexOf("==")+2));} 
					break; 
					case "<": {
						verified = verified && clock.getTime() < Integer.parseInt(numbers(c, c.indexOf("<")+1));} 
					break; 
					case "<=": {
						verified = verified && clock.getTime() <= Integer.parseInt(numbers(c, c.indexOf(">")+2));} 
					break; 
					}
				}
			}
		}
		return verified; 
	}

	private boolean controlWithHorizon(TGAClock clock, String c, long horizon) {
		String a = c.substring(c.indexOf("=")+1).trim();
		boolean verified = false;
		if (a.equals("H")) {
			switch(symbols(c)){
			case ">": {
				verified = clock.getTime() > horizon;} 
			break; 
			case ">=": {
				verified = clock.getTime() >= horizon;} 
			break; 
			case "==": {
				verified = clock.getTime() == horizon;} 
			break; 
			case "<": {
				verified = clock.getTime() < horizon;} 
			break; 
			case "<=": {
				verified = clock.getTime() <= horizon ;} 
			break; 
			}
		}
		else {
			switch(symbols(c)){
			case ">=": {
				verified = clock.getTime() >= horizon + Integer.parseInt(numbers(c, c.indexOf("+")+1));} 
			break; 
			case ">": {
				verified = clock.getTime() > horizon + Integer.parseInt(numbers(c, c.indexOf("+")+1));} 
			break; 
			case "==": {
				verified = clock.getTime() == horizon + Integer.parseInt(numbers(c, c.indexOf("+")+1));} 
			break; 
			case "<=": {
				verified = clock.getTime() <= horizon + Integer.parseInt(numbers(c, c.indexOf("+")+1));} 
			break; 
			case "<": {
				verified = clock.getTime() < horizon + Integer.parseInt(numbers(c, c.indexOf("+")+1));} 
			break; 

			}
		}
		return verified; 

	}

	public boolean isVerified(TGAClockControl clock){
		boolean verified = true;
		for (TGAClock c : clock.getClocks()){
			verified = verified && verify(c, clock.getHorizon()); 
		}
		return verified; 
	}

	public String getGuard(){
		return this.condition; 
	}

	public Set<String> getGuards(){
		return this.conditions; 
	}
	
	public void setGuard(String guard){
		this.condition = guard; 
		putGuards(); 
	}

}
