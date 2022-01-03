package it.cnr.istc.pst.platinum.ai.deliberative.heuristic.pipeline;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.knowledge.DomainKnowledge;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author alessandro
 *
 */
public class HierarchicalPlanFlawInspector extends FlawInspector {
	
	private FlawType[] preferences;

	/**
	 * 
	 */
	protected HierarchicalPlanFlawInspector() {
		super("HierarchicalPlanFlawInspector");
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
		// get domain knowledge
		DomainKnowledge knowledge = this.pdb.getDomainKnowledge();
		// get the hierarchy
		List<DomainComponent>[] hierarchy = knowledge.getDomainHierarchy();
		// check domain hierarchy first
		for (int index = 0; index < hierarchy.length && set.isEmpty(); index++) {
			// check specified flaw preferences
			for (int pndex = 0; pndex < this.preferences.length && set.isEmpty(); pndex++) {
				// get current flaw type 
				FlawType type = this.preferences[pndex];
				
				// get components at the current level of the hierarchy
				for (DomainComponent component : hierarchy[index]) {
					// detect flaws on the current component
					set.addAll(component.detectFlaws(type));
				}
			}
		}
		
		
		// get detected flaws
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> check() {
		
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// get domain knowledge
		DomainKnowledge knowledge = this.pdb.getDomainKnowledge();
		// get the hierarchy
		List<DomainComponent>[] hierarchy = knowledge.getDomainHierarchy();
		// check domain hierarchy first
		for (int index = 0; index < hierarchy.length && set.isEmpty(); index++) {
			// check specified flaw preferences
			for (int pndex = 0; pndex < this.preferences.length && set.isEmpty(); pndex++) {
				// get current flaw type 
				FlawType type = this.preferences[pndex];
				
				// get components at the current level of the hierarchy
				for (DomainComponent component : hierarchy[index]) {
					// check flaws on the current component
					set.addAll(component.checkFlaws(new FlawType[] {
							type
					}));
				}
			}
		}
		
		// get flaws
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) {
		
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// get domain knowledge
		DomainKnowledge knowledge = this.pdb.getDomainKnowledge();
		// get the hierarchy
		List<DomainComponent>[] hierarchy = knowledge.getDomainHierarchy();
		// get component flaws first
		Set<Flaw> componentFlaws = new HashSet<>();
		// check domain hierarchy first
		for (int index = 0; index < hierarchy.length && componentFlaws.isEmpty(); index++) {
			
			// extract flaws of equivalent components
			for (DomainComponent component : hierarchy[index]) {
				// check flaws
				for (Flaw flaw : flaws ) {
					if (flaw.getComponent().equals(component)) {
						// add flaw
						componentFlaws.add(flaw);
					}
				}
				
			}
		}
		
		
		// look for flaw of a given type
		for (int index = 0; index < this.preferences.length && set.isEmpty(); index++) {
			
			// get current type
			FlawType type = this.preferences[index];
			// check component flaws
			for (Flaw flaw : componentFlaws) {
				// check type
				if (flaw.getType().equals(type)) {
					// add flaw
					set.add(flaw);
				}
			}
		}
		
		// get filtered set
		return set;
	}
}
