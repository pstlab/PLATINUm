package it.istc.pst.platinum.executive.dc.strategy;

public class State {
	private String timeline;
	private String token;
	
	private State() {}
	public State (String state ) {
		this();
		this.timeline = state.substring(0, state.indexOf('.')).trim();
		this.token = state.substring(state.indexOf('.')+1, state.length() ).trim();
	}
	public String getTimeline() {
		return timeline;
	}
	public String getToken() {
		return token;
	}
	@Override
	public String toString() {
		return this.timeline + "." + this.token;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((timeline == null) ? 0 : timeline.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (timeline == null) {
			if (other.timeline != null)
				return false;
		} else if (!timeline.equals(other.timeline))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if(other.token.equals("?") || token.equals("?")) {
			return true;	
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
	
	
	
}
