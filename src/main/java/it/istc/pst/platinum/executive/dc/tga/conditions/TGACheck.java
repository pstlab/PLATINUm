package it.istc.pst.platinum.executive.dc.tga.conditions;

import java.util.HashSet;
import java.util.Set;

import it.istc.pst.platinum.executive.dc.tga.clocks.TGAClockControl;


public abstract class TGACheck {
	protected String condition; 
	protected Set<String> conditions; 
	
	public TGACheck(String condition){
		this.condition = condition; 
		this.conditions = new HashSet<String>();
	}
	
	/*This method controls if the set of conditions is verified on the plan.*/
	public abstract boolean isVerified(TGAClockControl clock);

	protected void adjust(){
		for (String c : this.conditions){
			if (startsWithANumber(c) && (c.contains("<") || c.contains("<="))){
				String a = c; 
				String symbol = symbols(c); 
				if (symbol.equals("<=")) symbol = ">=";
				
				a = a.substring((a.indexOf(symbols(a)))+symbols(a).length()).concat(symbol) + numbers(c, 0); 
			}
		}
	}
	
	protected String symbols(String c){
		if (c.contains("<")){
			if (c.contains("<="))
				return "<=";
			return "<"; 
		}
			return "=="; 

	}
	
	protected String numbers(String c, int i){
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

	protected boolean startsWithANumber(String c){
		return c.startsWith("0") || c.startsWith("1") || c.startsWith("2") || 
				c.startsWith("3") || c.startsWith("4")|| c.startsWith("5") || 
				c.startsWith("6") || c.startsWith("7") || c.startsWith("8") || 
				c.startsWith("9");
	}

}
