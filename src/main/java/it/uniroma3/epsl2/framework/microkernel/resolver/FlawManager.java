package it.uniroma3.epsl2.framework.microkernel.resolver;

import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

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
	 * @throws UnsolvableFlawFoundException
	 */
	public List<Flaw> findFlaws() 
			throws UnsolvableFlawFoundException;
	
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
