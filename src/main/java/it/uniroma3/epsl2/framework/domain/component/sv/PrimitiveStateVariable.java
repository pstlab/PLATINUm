package it.uniroma3.epsl2.framework.domain.component.sv;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.utils.view.component.ComponentViewType;

/**
 * 
 * @author anacleto
 *
 */
@DomainComponentConfiguration(

		resolvers = {
		
				// scheduling resolver
				ResolverType.SV_SCHEDULING_RESOLVER,
				
				// time-line gap resolver
				ResolverType.SV_GAP_RESOLVER,
				
				// behavior checking resolver
				ResolverType.BEHAVIOR_CHECKING_RESOLVER
		},
		
		view = ComponentViewType.GANTT
)
public final class PrimitiveStateVariable extends StateVariable {
	
	/**
	 * 
	 * @param name
	 */
	protected PrimitiveStateVariable(String name) {
		super(name, DomainComponentType.SV_PRIMTIVE);
	}
}
