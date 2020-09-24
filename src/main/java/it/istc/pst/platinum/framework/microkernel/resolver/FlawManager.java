package it.istc.pst.platinum.framework.microkernel.resolver;

import java.util.List;

import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;

/**
 * 
 * @author anacleto
 *
 */
public interface FlawManager 
{
	/**
	 * 
	 * @return
	 */
	public List<Flaw> checkFlaws();
	
	/**
	 * 
	 * @return
	 * @throws UnsolvableFlawException
	 */
	public List<Flaw> findFlaws() 
			throws UnsolvableFlawException;
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	public void apply(FlawSolution solution) 
			throws FlawSolutionApplicationException;
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	public void restore(FlawSolution solution) 
			throws Exception;
	
	/**
	 * 
	 * @param solution
	 */
	public void retract(FlawSolution solution);
}
