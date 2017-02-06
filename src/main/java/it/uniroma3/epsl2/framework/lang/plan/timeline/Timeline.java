package it.uniroma3.epsl2.framework.lang.plan.timeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.Token;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public class Timeline implements Comparator<Token> 
{
	private DomainComponent component;
	
	/**
	 * 
	 * @param component
	 */
	public Timeline(DomainComponent component) {
		this.component = component;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Token> getTokens() {
		List<Token> tokens = new ArrayList<>();
		for (Decision dec : this.component.getActiveDecisions()) {
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
	public DomainComponent getComponent() {
		return this.component;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.component.getName();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isObservation() {
		return this.component.isExternal();
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
		result = prime * result + ((component == null) ? 0 : component.hashCode());
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
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Timeline component= " + this.component + " tokens= " + this.getTokens() + "]";
	}
}
