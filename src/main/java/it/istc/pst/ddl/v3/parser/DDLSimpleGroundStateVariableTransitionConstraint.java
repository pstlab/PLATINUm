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
public class DDLSimpleGroundStateVariableTransitionConstraint extends CommonTree {

    private DDLSimpleGroundStateVariableComponentDecision from;
    private DDLRange range;
    private final List<DDLSimpleGroundStateVariableComponentDecision> to = new ArrayList<DDLSimpleGroundStateVariableComponentDecision>();

    public DDLSimpleGroundStateVariableTransitionConstraint(Token payload) {
        super(payload);
    }

    void parse() {
        from = (DDLSimpleGroundStateVariableComponentDecision) getChild(0);
        from.parse();
        range = (DDLRange) getChild(1);
        range.parse();
        for (int i = 2; i < getChildCount(); i++) {
            DDLSimpleGroundStateVariableComponentDecision target = (DDLSimpleGroundStateVariableComponentDecision) getChild(i);
            target.parse();
            to.add(target);
        }
    }

    /**
     * @return the from
     */
    public DDLSimpleGroundStateVariableComponentDecision getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public List<DDLSimpleGroundStateVariableComponentDecision> getTo() {
        return Collections.unmodifiableList(to);
    }

    /**
     * @return the range
     */
    public DDLRange getRange() {
        return range;
    }
}
