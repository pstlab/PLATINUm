package it.istc.pst.platinum.executive.dc.tga.clocks;

import java.util.HashSet;
import java.util.Set;

public class TGAClockControl {

	private Set<TGAClock> clocks; 
	private long horizon;
	private Set<String> newValues; 


	public TGAClockControl(Set<String> clocks, long horizon){
		this.clocks = new HashSet<TGAClock>();
		
		for (String s : clocks){
			this.clocks.add(new TGAClock(s));
		}
		for (TGAClock c: this.clocks){
			if (!c.getClockName().contains("clock")){
				c.setClockName(c.getClockName().concat("_clock"));
			}
		}
	
		this.horizon = horizon;
	}


	/*This method increases the values of all the clocks.*/
	public void increaseClocks(){
		for (TGAClock c : this.clocks)
			c.setTime(c.getTime()+1);
	}


	/*The method, having new values that the clock must assume, change the current values of the clock,
	 * reinitializing them if it is necessary.*/
	public void setNewValues(String values){
		this.newValues = new HashSet<String>(); 
		this.createSet(values);

		if (values != null){
			for (TGAClock c: this.clocks){
				for (String v : this.newValues)
					if (v.contains(c.getClockName())){
						if (v.contains("H")){
							setTimeRelatedToHorizon(c, v);
						}
						else 
							c.setTime(0);
					}
			}
		}
	}


	private void setTimeRelatedToHorizon(TGAClock c, String newValue){
		if (newValue.contains(c.getClockName())){
			String a = newValue.substring(newValue.indexOf("=")+1).trim();
			if (a.equals("H")) c.setTime(this.horizon);
			else {
				c.setTime(this.horizon + Integer.parseInt(numbers(a, a.indexOf("H"))));
			}
		}
	}



	private String numbers(String c, int i){
		String a = "";
		while (i<c.length() && isNumber(c.charAt(i))){
			a = a + c.charAt(i); 
			i++; 
		}
		return a; 
	}



	private boolean isNumber(char charAt) {
		return charAt=='0'||charAt=='1'||charAt=='2'||charAt=='3'||charAt=='4'||charAt=='5'
				|| charAt=='6'||charAt=='7'||charAt=='8'||charAt=='9';
	}




	public Set<TGAClock> getClocks(){
		return this.clocks; 
	}


	public TGAClock getClockWithName(String name){
		TGAClock a = null; 
		for (TGAClock c : this.clocks){
			if (c.getClockName().equals(name)) a = c;
		}
		return a; 
	}

	private void createSet(String newVal){
		String a = newVal.substring(newVal.indexOf("tau"+4)).trim(); 
		if (a.equals("1")){
			this.newValues = null;
		} else {
			a = a.substring(0, a.indexOf("}"));
			this.newValues = new HashSet<String>(); 
			this.newValues.add(a.substring(0, a.indexOf(",")-1).trim()); 
			a = a.substring(a.indexOf(",")+1);
		}
	}

	public Set<String> getNewValues(){
		return this.newValues;
	}
	
	public long getHorizon(){
		return this.horizon; 
	}
}

