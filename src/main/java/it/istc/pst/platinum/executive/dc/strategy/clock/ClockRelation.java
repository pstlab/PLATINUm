package it.istc.pst.platinum.executive.dc.strategy.clock;

import java.util.Map;

public class ClockRelation {
	private String value1;
	private String value2;
	private String relation;

	//------------------------ CONSTRUCTORS ---------------------------
	public ClockRelation(String clockRelation) { 

		if (clockRelation.contains(">=")) this.relation = ">=";			
		else if (clockRelation.contains("<=")) this.relation = "<=";			
		else if (clockRelation.contains(">")) this.relation = ">";		
		else if (clockRelation.contains("<")) this.relation = "<";
		else if (clockRelation.contains("==")) this.relation = "==";

		String[] split = clockRelation.split(relation);
		 this.value1 = split[0];
		 this.value2 = split[1];
		if(this.value1.contains("_")) this.value1 = this.value1.substring(0,this.value1.indexOf("_"));
		if(this.value1.contains(".")) this.value1 = this.value1.substring(0,this.value1.indexOf("."));
		if(this.value2.contains("_")) this.value2 = this.value2.substring(0,this.value2.indexOf("_"));
		if(this.value2.contains(".")) this.value2 = this.value2.substring(0,this.value2.indexOf("."));
	}

	//---------------------METHODS -----------------------

	//verifies if the clockrelation is acceptable in the status
	public boolean verify(Map<String, Long> status) throws Exception {

		long valueInt1 = evaluateExpression(status,this.value1);;
		long valueInt2 = evaluateExpression(status,this.value2);;

		if (this.relation == ">=") return (valueInt1>=valueInt2);
		else if (this.relation == "<=") return (valueInt1<=valueInt2);
		else if (this.relation == ">") return (valueInt1>valueInt2);
		else if (this.relation == "<") return (valueInt1<valueInt2);
		else if (this.relation == "==") return (valueInt1==valueInt2);
		else throw new Exception();
	}

	//evaluate the actual expression of the clockrelation in a numeric value
	private long evaluateExpression(Map<String, Long> status,String value) {
		long valueInt;
		try { 
			valueInt = Integer.parseInt(value); 	
		}
		catch (NumberFormatException nfe) {

			if(value.contains("-")) {
				String[] split = value.split("-");
				valueInt = status.get(split[0].trim())-status.get(split[1].trim());
			} else valueInt = status.get(value);
		}
		return valueInt;
	}

	//-------------------- GETTERS/SETTERS------------------
	public String getRelation() {
		return relation;
	}

	public String getValue1() {
		return value1;
	}

	public String getValue2() {
		return value2;
	}

	@Override
	public String toString() {
		return "ClockRelation [" + value1 + relation + value2 +"]";
	}
}

