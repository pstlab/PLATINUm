package it.istc.pst.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLSingletonStateVariableTransitionConstraint extends CommonTree {

    private DDLSingletonStateVariableComponentDecision from;
    private DDLRange range;
    private final List<DDLSingletonStateVariableComponentDecision> to = new ArrayList<DDLSingletonStateVariableComponentDecision>();
    private final List<DDLParameterConstraint> constraints = new ArrayList<DDLParameterConstraint>();

    public DDLSingletonStateVariableTransitionConstraint(Token payload) {
        super(payload);
    }

    void parse() {
        from = (DDLSingletonStateVariableComponentDecision) getChild(0);
        from.parse();
        range = (DDLRange) getChild(1);
        range.parse();
        for (int i = 2; i < getChildCount(); i++) {
            if (getChild(i) instanceof DDLSingletonStateVariableComponentDecision) {
                DDLSingletonStateVariableComponentDecision target = (DDLSingletonStateVariableComponentDecision) getChild(i);
                target.parse();
                to.add(target);
            }
            if (getChild(i) instanceof DDLParameterConstraint) {
                DDLParameterConstraint constraint = (DDLParameterConstraint) getChild(i);
                constraint.parse();
                constraints.add(constraint);
            }
        }
    }

    /**
     * @return the from
     */
    public DDLSingletonStateVariableComponentDecision getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public List<DDLSingletonStateVariableComponentDecision> getTo() {
        return Collections.unmodifiableList(to);
    }

    /**
     * @return the constraints
     */
    public List<DDLParameterConstraint> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }

    /**
     * @return the range
     */
    public DDLRange getRange() {
        return range;
    }
}
