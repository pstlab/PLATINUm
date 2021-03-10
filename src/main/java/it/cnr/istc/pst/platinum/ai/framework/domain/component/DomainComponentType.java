package it.cnr.istc.pst.platinum.ai.framework.domain.component;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.PlanDataBaseComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.discrete.DiscreteResource;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.ExternalStateVariable;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.FunctionalStateVariable;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.PrimitiveStateVariable;

/**
 * 
 * @author anacleto
 *
 */
public enum DomainComponentType 
{
	/**
	 * Special purpose domain component
	 */
	PLAN_DATABASE(PlanDataBaseComponent.class.getName(), "Plan Database component"),
	
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
	 * Discrete Resource type
	 */
	RESOURCE_DISCRETE(DiscreteResource.class.getName(), "Discrete Resource component");
	
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
	public String getClassClassName() {
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
