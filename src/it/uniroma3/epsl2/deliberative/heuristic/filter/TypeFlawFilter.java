package it.uniroma3.epsl2.deliberative.heuristic.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifcycle.PostConstruct;

/**
 * 
 * @author anacleto
 *
 */
public class TypeFlawFilter extends FlawFilter 
{
	private FlawType[] preferences;

	/**
	 * 
	 */
	protected TypeFlawFilter() {
		super(FlawFilterType.TFF);
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// load preferences
		this.preferences = new FlawType[] {
				FlawType.PLAN_REFINEMENT,
				FlawType.RESOURCE_PEAK,
				FlawType.SV_SCHEDULING,
				FlawType.SV_GAP
		};
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) {
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// look for flaw of a given type
		for (int index = 0; index < this.preferences.length && set.isEmpty(); index++) {
			// get current type
			FlawType type = this.preferences[index];
			// check flaws
			Iterator<Flaw> it = flaws.iterator();
			while(it.hasNext()) {
				// get next flaw
				Flaw flaw = it.next();
				// check 
				if (flaw.getType().equals(type)) {
					set.add(flaw);
				}
			}
		}
		
		// get filtered set
		return set;
	}
}
