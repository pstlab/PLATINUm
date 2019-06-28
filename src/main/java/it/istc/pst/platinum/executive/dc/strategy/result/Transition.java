package it.istc.pst.platinum.executive.dc.strategy.result;

import it.istc.pst.platinum.executive.dc.strategy.State;

public class Transition implements Action {
	private State transitionFrom;
	private State transitionTo;
	
	private Transition() {};
	public Transition (String transition) {
		this();
		this.transitionFrom = new State(transition.substring(0, transition.indexOf('-')).trim());
		this.transitionTo = new State(transition.substring(transition.indexOf('>')+1, transition.length()).trim());
	}
	public State getTransitionFrom() {
		return transitionFrom;
	}
	public State getTransitionTo() {
		return transitionTo;
	}
	@Override
	public String toString() {
		return this.transitionFrom + "->" + this.transitionTo;
	}
	@Override
	public String getAction() {
		return "Transition";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transitionFrom == null) ? 0 : transitionFrom.hashCode());
		result = prime * result + ((transitionTo == null) ? 0 : transitionTo.hashCode());
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
		Transition other = (Transition) obj;
		if (transitionFrom == null) {
			if (other.transitionFrom != null)
				return false;
		} else if (!transitionFrom.equals(other.transitionFrom))
			return false;
		if (transitionTo == null) {
			if (other.transitionTo != null)
				return false;
		} else if (!transitionTo.equals(other.transitionTo))
			return false;
		return true;
	}
	
}
