package it.uniroma3.epsl2.framework.time.tn.stnu.ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class PseudoControllabilityCheckException extends ConsistencyCheckException {
	private static final long serialVersionUID = 1L;

	private Map<DomainComponent, List<Decision>> issues;
	
	/**
	 * 
	 * @param msg
	 */
	public PseudoControllabilityCheckException(String msg) {
		super(msg);
		this.issues = new HashMap<>();
	}
	
	/**
	 * 
	 * @param value
	 */
	public void addIssue(Decision dec) {
		if (!issues.containsKey(dec.getComponent())) {
			this.issues.put(dec.getComponent(), new ArrayList<>());
		}
		// add issue
		this.issues.get(dec.getComponent()).add(dec);
	}
	
	/**
	 * 
	 * @param issues
	 */
	public void setPseudoControllabilityIssues(Map<DomainComponent, List<Decision>> issues) {
		this.issues = issues;
	}

	/**
	 * 
	 * @return
	 */
	public Map<DomainComponent, List<Decision>> getPseudoControllabilityIssues() {
		return new HashMap<>(this.issues);
	}
}
