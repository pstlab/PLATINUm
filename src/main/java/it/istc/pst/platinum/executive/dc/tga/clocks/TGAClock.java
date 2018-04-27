package it.istc.pst.platinum.executive.dc.tga.clocks;

public class TGAClock {

	private String clockName; 
	private long tick;  

	public TGAClock(String clockName){
		this.clockName = clockName; 
		this.tick = 0;
	}

	public TGAClock (String clockName, int tick){
		this.clockName = clockName; 
		this.tick = tick; 
	}

	public String getClockName(){
		return this.clockName; 
	}

	public void setClockName(String newName){
		this.clockName = newName; 
	}

	public long getTime(){
		return this.tick; 
	}

	public void setTime(long tick){
		this.tick = tick; 
	}
}

