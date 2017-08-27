package it.istc.pst.platinum.framework.time.tn.ex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class PseudoControllabilityCheckException extends ConsistencyCheckException {
	private static final long serialVersionUID = 1L;

	private Map<DomainComponent<?>, List<Decision>> issues;
	
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
			this.issues.put(dec.getComponent(), new ArrayList<Decision>());
		}
		// add issue
		this.issues.get(dec.getComponent()).add(dec);
	}
	
	/**
	 * 
	 * @param issues
	 */
	public void setPseudoControllabilityIssues(Map<DomainComponent<?>, List<Decision>> issues) {
		this.issues = issues;
	}

	/**
	 * 
	 * @return
	 */
	public Map<DomainComponent<?>, List<Decision>> getPseudoControllabilityIssues() {
		return new HashMap<>(this.issues);
	}
}
