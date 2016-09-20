package it.uniroma3.epsl2.deliberative.heuristic.filter.fff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;

/**
 * 
 * @author anacleto
 *
 */
public class FailFirstFlawFilter extends FlawFilter 
{
	/**
	 * 
	 */
	protected FailFirstFlawFilter() {
		super(FlawFilterType.FFF);
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) 
	{
		// index flaws by number of available solutions
		Map<Integer, List<Flaw>> index = new HashMap<>();
		// subset of flaws
		Set<Flaw> selected;
		// check flaw size
		if (!flaws.isEmpty()) 
		{
			// minimum number of solution of a flaw
			int min = Integer.MAX_VALUE - 1;
			// check number of solution of each flaw
			for (Flaw flaw : flaws) 
			{
				// get "degree"
				int size = flaw.getSolutions().size();
				// update minimum
				min = Math.min(min, size);
				if (!index.containsKey(size)) {
					index.put(size, new ArrayList<Flaw>());
				}
				// add flaw
				index.get(size).add(flaw);
			}
			
			// get selected subset
			selected = new HashSet<>(index.get(min));
		}
		else {
			selected = new HashSet<>(flaws);		
		}
		
		
		// get sub-set of flaw with the lowest number of solutions available
		return selected;
	}
}
