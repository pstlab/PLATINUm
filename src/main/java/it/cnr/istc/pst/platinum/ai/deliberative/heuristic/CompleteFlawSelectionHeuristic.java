package it.cnr.istc.pst.platinum.ai.deliberative.heuristic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.NoFlawFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;

/**
 * 
 * @author anacleto
 *
 */
public class CompleteFlawSelectionHeuristic extends FlawSelectionHeuristic
{
	/**
	 * 
	 */
	protected CompleteFlawSelectionHeuristic() {
		super("RandomFlawSelectionHeuristic");
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> choose() 
			throws UnsolvableFlawException, NoFlawFoundException {
		
		// detect flaws
		List<Flaw> flaws = this.pdb.detectFlaws();
		// check flaws found
		if (flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		
		// consider all flaws for plan refinement
		return new HashSet<>(flaws);
	}

	/**
	 * 
	 */
	@Override
	public Set<Flaw> check() {
		// check flaws
		return new HashSet<>(this.pdb.checkFlaws());
	}
	
	
}
