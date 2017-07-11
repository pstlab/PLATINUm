package it.istc.pst.platinum.framework.domain.component;

import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBaseComponent;
import it.istc.pst.platinum.framework.domain.component.resource.DiscreteResource;
import it.istc.pst.platinum.framework.domain.component.resource.ReservoirResource;
import it.istc.pst.platinum.framework.domain.component.resource.UnaryResource;
import it.istc.pst.platinum.framework.domain.component.sv.ExternalStateVariable;
import it.istc.pst.platinum.framework.domain.component.sv.FunctionalStateVariable;
import it.istc.pst.platinum.framework.domain.component.sv.PrimitiveStateVariable;

/**
 * 
 * @author anacleto
 *
 */
public enum DomainComponentType {
	
	/**
	 * Special purpose domain component
	 */
	PDB(PlanDataBaseComponent.class.getName(), "Plan Database component"),
	
	/**
	 * Functional State Variable type
	 */
	SV_FUNCTIONAL(FunctionalStateVariable.class.getName(), "Functional State Variable component"),
	
	/**
	 * Primitive State Variable type
	 */
	SV_PRIMITIVE(PrimitiveStateVariable.class.getName(), "Primitive State Variable component"), 
	
	/**
	 * External State Variable type
	 */
	SV_EXTERNAL(ExternalStateVariable.class.getName(), "External State Variable component"),
	
	/**
	 * Reservoir Resource type
	 */
	RESOURCE_RESERVOIR(ReservoirResource.class.getName(), "Reservoir Resource component"),
	
	/**
	 * Discrete Resource type
	 */
	RESOURCE_DISCRETE(DiscreteResource.class.getName(), "Discrete Resource component"),
	
	/**
	 * Binary Resource type
	 */
	RESOURCE_UNARY(UnaryResource.class.getName(), "Unary Resource component");
	
	private String cname;
	private String label;

	/**
	 * 
	 * @param cname
	 * @param label
	 */
	private DomainComponentType(String cname, String label) {
		this.cname = cname;
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComponentClassName() {
		return cname;
	}

	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
}
