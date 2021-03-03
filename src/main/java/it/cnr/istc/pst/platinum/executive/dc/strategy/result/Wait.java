package it.cnr.istc.pst.platinum.executive.dc.strategy.result;

public class Wait implements Action{
	
	public Wait(){}

	@Override
	public String getAction() {
		return "Wait";	
	}

	@Override
	public String toString() {
		return "Wait";
	}
}
