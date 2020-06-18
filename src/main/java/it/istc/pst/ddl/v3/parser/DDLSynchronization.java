package it.istc.pst.ddl.v3.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLSynchronization extends CommonTree {

    private DDLComponentDecision reference;
    private final Map<String, DDLInstantiatedComponentDecision> targets = new HashMap<String, DDLInstantiatedComponentDecision>();
    private final List<DDLParameterConstraint> parameterConstraints = new LinkedList<DDLParameterConstraint>();
    private final List<DDLTemporalRelation> temporalRelations = new LinkedList<DDLTemporalRelation>();

    public DDLSynchronization(Token payload) {
        super(payload);
    }

    void parse() {
        reference = (DDLComponentDecision) getChild(0);
        reference.parse();
        for (int i = 1; i < getChildCount(); i++) {
            Tree child = getChild(i);
            if (child instanceof DDLInstantiatedComponentDecision) {
                DDLInstantiatedComponentDecision target = ((DDLInstantiatedComponentDecision) child);
                target.parse();
                targets.put(target.getId(), target);
                continue;
            }
            if (child instanceof DDLParameterConstraint) {
                DDLParameterConstraint parameter_constraint = ((DDLParameterConstraint) child);
                parameter_constraint.parse();
                parameterConstraints.add(parameter_constraint);
                continue;
            }
            if (child instanceof DDLTemporalRelation) {
                DDLTemporalRelation temporal_relation = ((DDLTemporalRelation) child);
                temporal_relation.parse();
                temporalRelations.add(temporal_relation);
                continue;
            }
        }
    }

    /**
     * @return the reference
     */
    public DDLComponentDecision getReference() {
        return reference;
    }

    /**
     * @return the targets
     */
    public Map<String, DDLInstantiatedComponentDecision> getTargets() {
        return Collections.unmodifiableMap(targets);
    }

    /**
     * @return the parameterConstraints
     */
    public List<DDLParameterConstraint> getParameterConstraints() {
        return Collections.unmodifiableList(parameterConstraints);
    }

    /**
     * @return the temporalRelations
     */
    public List<DDLTemporalRelation> getTemporalRelations() {
        return Collections.unmodifiableList(temporalRelations);
    }
}
