package it.uniroma3.epsl2.framework.microkernel.lang.plan;

import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.parameter.BindParameterRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.parameter.EqualParameterRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.parameter.NotEqualParameterRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.AfterRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.BeforeRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.ContainsRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.DuringRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.EndsDuringRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.EqualsRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.MeetsRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.MetByRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.StartsDuringRelation;

/**
 * 
 * @author anacleto
 *
 */
public enum RelationType {

	/**
	 * Allen's meets temporal relation
	 */
	MEETS(MeetsRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Allen's met-by temporal relation
	 */
	MET_BY(MetByRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Allen's before temporal relation
	 */
	BEFORE(BeforeRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Allen's after temporal relation
	 */
	AFTER(AfterRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Allen's equals temporal relation
	 */
	EQUALS(EqualsRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Allen's contains temporal relation
	 */
	CONTAINS(ContainsRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Starts-during temporal relation
	 */
	STARTS_DURING(StartsDuringRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Ends-during temporal relation
	 */
	ENDS_DURING(EndsDuringRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Allen's during temporal relation
	 */
	DURING(DuringRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * Equal parameter relation
	 */
	EQUAL_PARAMETER(EqualParameterRelation.class.getName(),
			ConstraintCategory.PARAMETER_CONSTRAINT),
	
	/**
	 * Not equal parameter relation
	 */
	NOT_EQUAL_PARAMETER(NotEqualParameterRelation.class.getName(), 
			ConstraintCategory.PARAMETER_CONSTRAINT),
	
	/**
	 * Bind a parameter to a specify value
	 */
	BIND_PARAMETER(BindParameterRelation.class.getName(),
			ConstraintCategory.PARAMETER_CONSTRAINT);

	private String cname;
	private ConstraintCategory category;
	
	/**
	 * 
	 * @param cname
	 * @param category
	 */
	private RelationType(String cname, ConstraintCategory category) {
		this.cname = cname;
		this.category = category;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRelationClassName() {
		return this.cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public ConstraintCategory getCategory() {
		return category;
	}
}
