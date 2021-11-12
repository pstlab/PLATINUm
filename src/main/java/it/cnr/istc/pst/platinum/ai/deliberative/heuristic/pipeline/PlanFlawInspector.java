package it.cnr.istc.pst.platinum.ai.deliberative.heuristic.pipeline;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class PlanFlawInspector extends FlawInspector 
{
	private FlawType[] preferences;

	/**
	 * 
	 */
	protected PlanFlawInspector() {
		super("PlanFlawInspector");
	}
	
	/**
	 * 
	 */
	@PostConstruct 
	protected void init() {
		
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(
				FRAMEWORK_HOME + FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		// get preference property
		String[] prefs = properties.getProperty("preferences").trim().split(",");
		
		// set preferences
		this.preferences = new FlawType[prefs.length];
		for (int i = 0; i < prefs.length; i++) {
			// set preference
			this.preferences[i] = FlawType.getFlawTypeFromLabel(prefs[i]);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> detectFlaws() 
			throws UnsolvableFlawException {
		
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// look for flaws of a given type
		for (int index = 0; index < this.preferences.length && set.isEmpty(); index++) {
			
			// get type of flaw
			FlawType type = this.preferences[index];
			// detect flaws
			set = new HashSet<>(this.pdb.detectFlaws(type));
		}
		
		// get flaws
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> check() {
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// look for flaws of a given type
		for (int index = 0; index < this.preferences.length && set.isEmpty(); index++) {
			// get type of flaw
			FlawType type = this.preferences[index];
			// detect flaws
			set = new HashSet<>(this.pdb.checkFlaws(new FlawType[] {
					type
					}));
		}
		
		// get flaws
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) 
	{
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// look for flaw of a given type
		for (int index = 0; index < this.preferences.length && set.isEmpty(); index++) 
		{
			// get current type
			FlawType type = this.preferences[index];
			// check flaws
			Iterator<Flaw> it = flaws.iterator();
			while(it.hasNext()) 
			{
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
