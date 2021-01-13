package it.cnr.istc.pst.platinum.ai.deliberative.heuristic.pipeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;

/**
 * 
 * @author anacleto
 *
 */
public class DegreeFlawInspector extends FlawInspector implements Comparator<Flaw>
{
	/**
	 * 
	 */
	protected DegreeFlawInspector() {
		super("DegreeFlawInspector");
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> detectFlaws() 
			throws UnsolvableFlawException 
	{
		// set of filtered list
		Set<Flaw> set = new HashSet<>();
		// filtered set
		List<Flaw> list = new ArrayList<>(this.pdb.detectFlaws());
		// check list 
		if (!list.isEmpty()) 
		{
			// sort flaws according to their degree (i.e. number of available solutions
			Collections.sort(list, this);
			
			// get "equivalent" flaws
			List<Flaw> eq = new ArrayList<>();
			// get the first element of the list
			Flaw flaw = list.remove(0);
			// add flaw to equivalent
			eq.add(flaw);
			
			// check other flaws with the same number of solutions
			for (Flaw other : list) {
				// check size
				if (other.getSolutions().size() == flaw.getSolutions().size()) {
					// add to equivalent list
					eq.add(other);
				}
			}
			
			// randomly select a flaw from the equivalent list
			Random rand = new Random(System.currentTimeMillis());
			int index = rand.nextInt(eq.size());
			// add flaw to the result set
			set.add(eq.get(index));
		}
				
		// get flaws
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> check() {
		// set of filtered list
		return new HashSet<>(this.pdb.checkFlaws());
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(Flaw o1, Flaw o2) {
		// compare the number of available solutions
		return o1.getSolutions().size() < o2.getSolutions().size() ? -1 : 
			o1.getSolutions().size() > o2.getSolutions().size() ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) 
	{
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// list of flaws
		List<Flaw> list = new ArrayList<>(flaws);
		// check flaws
		if (!list.isEmpty()) 
		{
			// sort flaws according to their degree (i.e. number of available solutions
			Collections.sort(list, this);
			
			// get "equivalent" flaws
			List<Flaw> eq = new ArrayList<>();
			// get the first element of the list
			Flaw flaw = list.remove(0);
			// add flaw to equivalent
			eq.add(flaw);
			
			// check other flaws with the same number of solutions
			for (Flaw other : list) {
				// check size
				if (other.getSolutions().size() == flaw.getSolutions().size()) {
					// add to equivalent list
					eq.add(other);
				}
			}
			
			// randomly select a flaw from the equivalent list
			Random rand = new Random(System.currentTimeMillis());
			int index = rand.nextInt(eq.size());
			// add flaw to the result set
			set.add(eq.get(index));
		}
		
		// get filtered set
		return set;
	}
}
