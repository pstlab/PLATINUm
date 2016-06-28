package it.uniroma3.epsl2.framework.domain.component.sv;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;

/**
 * 
 * @author anacleto
 *
 */
@DomainComponentConfiguration(
		
		// set resolvers to manage primitive variables
		resolvers = {
		
				ResolverType.SV_SCHEDULING_RESOLVER,
				
				ResolverType.SV_GAP_RESOLVER,
				
				ResolverType.BEHAVIOR_CHECKING_RESOLVER
		}
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
