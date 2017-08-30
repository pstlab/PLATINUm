package it.istc.pst.platinum.deliberative.heuristic.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;

/**
 * 
 * @author anacleto
 *
 */
public class DegreeFlawFilter extends FlawFilter implements Comparator<Flaw>
{
	/**
	 * 
	 */
	protected DegreeFlawFilter() {
		super(FlawFilterType.DFF.getLabel());
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter() 
			throws UnsolvableFlawException 
	{
		// set of filtered list
		Set<Flaw> flaws = new HashSet<>();
		// filtered set
		List<Flaw> list = new ArrayList<>(this.pdb.detectFlaws());
		// check list 
		if (!list.isEmpty()) 
		{
			// sort flaws according to their degree (i.e. number of available solutions
			Collections.sort(list, this);
			// get the first element of the list
			Flaw flaw = list.remove(0);
			flaws.add(flaw);
			// check remaining flaws
			boolean stop = false;
			for (int index = 0; index < list.size() && !stop; index++) 
			{
				// get flaw
				Flaw current = list.get(index);
				// compare available solutions
				if (flaw.getSolutions().size() == current.getSolutions().size()) {
					// add flaw to the set
					flaws.add(current);
				}
				else {
					// stop, the rest of flaws have an higher number of solutions available
					stop = true;
				}
			}
		}
				
		// get flaws
		return flaws;
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
			// get the first element of the list
			Flaw flaw = list.remove(0);
			set.add(flaw);
			// check remaining flaws
			boolean stop = false;
			for (int index = 0; index < list.size() && !stop; index++) 
			{
				// get flaw
				Flaw current = list.get(index);
				// compare available solutions
				if (flaw.getSolutions().size() == current.getSolutions().size()) {
					// add flaw to the set
					set.add(current);
				}
				else {
					// stop, the rest of flaws have an higher number of solutions available
					stop = true;
				}
			}
		}
		
		// get filtered set
		return set;
	}
}
