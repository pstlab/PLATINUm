package it.istc.pst.platinum.framework.microkernel.lang.plan.timeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.Token;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public class Timeline implements Comparator<Token> 
{
	private StateVariable sv;
	
	/**
	 * 
	 * @param sv
	 */
	public Timeline(StateVariable sv) {
		this.sv = sv;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Token> getTokens() {
		List<Token> tokens = new ArrayList<>();
		for (Decision dec : this.sv.getActiveDecisions()) {
			tokens.add(dec.getToken());
		}
		// sort tokens
		Collections.sort(tokens, this);
		// get sorted tokens
		return tokens;
	}

	/**
	 * 
	 * @return
	 */
	public StateVariable getComponent() {
		return this.sv;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.sv.getName();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isObservation() {
		return this.sv.isExternal();
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(Token o1, Token o2) {
		// get intervals
		TemporalInterval i1 = o1.getInterval();
		TemporalInterval i2 = o2.getInterval();
		
		// compare intervals
		return i1.getStartTime().getLowerBound() < i2.getStartTime().getLowerBound() ? -1 : 
			i1.getStartTime().getLowerBound() == i2.getStartTime().getLowerBound() && 
			i1.getDurationLowerBound() <= i2.getDurationLowerBound() ? -1 : 1;
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sv == null) ? 0 : sv.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timeline other = (Timeline) obj;
		if (sv == null) {
			if (other.sv != null)
				return false;
		} else if (!sv.equals(other.sv))
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Timeline component= " + this.sv + " tokens= " + this.getTokens() + "]";
	}
}
