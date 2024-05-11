package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.ConstraintCategory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.EqualParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.NotEqualParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.AfterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.ContainsRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.DuringRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.EndEndRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.EndsDuringRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.EqualsRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.MeetsRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.MetByRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.StartStartRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.StartsDuringRelation;

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
	 * start-start temporal interval constraint
	 */
	START_START(StartStartRelation.class.getName(),
			ConstraintCategory.TEMPORAL_CONSTRAINT),
	
	/**
	 * end-end temporal interval constraint
	 */
	END_END(EndEndRelation.class.getName(), 
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
