package it.uniroma3.epsl2.framework.domain.component;

import it.uniroma3.epsl2.framework.domain.component.pdb.PlanDataBaseComponent;
import it.uniroma3.epsl2.framework.domain.component.resource.costant.DiscreteResource;
import it.uniroma3.epsl2.framework.domain.component.resource.costant.ReservoirResource;
import it.uniroma3.epsl2.framework.domain.component.resource.costant.UnaryResource;
import it.uniroma3.epsl2.framework.domain.component.sv.ExternalStateVariable;
import it.uniroma3.epsl2.framework.domain.component.sv.FunctionalStateVariable;
import it.uniroma3.epsl2.framework.domain.component.sv.PrimitiveStateVariable;

/**
 * 
 * @author anacleto
 *
 */
public enum DomainComponentType {
	
	/**
	 * Special purpose domain component
	 */
	PDB(PlanDataBaseComponent.class.getName()),
	
	/**
	 * Functional State Variable type
	 */
	SV_FUNCTIONAL(FunctionalStateVariable.class.getName()),
	
	/**
	 * Primitive State Variable type
	 */
	SV_PRIMITIVE(PrimitiveStateVariable.class.getName()), 
	
	/**
	 * External State Variable type
	 */
	SV_EXTERNAL(ExternalStateVariable.class.getName()),
	
	/**
	 * Reservoir Resource type
	 */
	RESOURCE_RESERVOIR(ReservoirResource.class.getName()),
	
	/**
	 * Discrete Resource type
	 */
	RESOURCE_DISCRETE(DiscreteResource.class.getName()),
	
	/**
	 * Binary Resource type
	 */
	RESOURCE_UNARY(UnaryResource.class.getName());
	
	private String cname;

	private DomainComponentType(String cname) {
		this.cname = cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComponentClassName() {
		return cname;
	}
}
