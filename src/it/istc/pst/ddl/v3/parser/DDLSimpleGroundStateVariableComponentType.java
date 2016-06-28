package it.istc.pst.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLSimpleGroundStateVariableComponentType extends DDLComponentType {

    private final Map<String, DDLSimpleGroundStateVariableComponentDecisionType> allowedValues = new HashMap<String, DDLSimpleGroundStateVariableComponentDecisionType>();
    private final List<DDLSimpleGroundStateVariableTransitionConstraint> transitionConstraints = new ArrayList<DDLSimpleGroundStateVariableTransitionConstraint>();

    public DDLSimpleGroundStateVariableComponentType(Token payload) {
        super(payload);
    }

    @Override
    void parse() {
        name = getText();
        for (int i = 0; i < getChildCount(); i++) {
            Tree child = getChild(i);
            if (child instanceof DDLSimpleGroundStateVariableComponentDecisionType) {
                DDLSimpleGroundStateVariableComponentDecisionType allowed_value = ((DDLSimpleGroundStateVariableComponentDecisionType) child);
                allowed_value.parse();
                allowedValues.put(allowed_value.getName(), allowed_value);
                continue;
            }
            if (child instanceof DDLSimpleGroundStateVariableTransitionConstraint) {
                DDLSimpleGroundStateVariableTransitionConstraint constraint = ((DDLSimpleGroundStateVariableTransitionConstraint) child);
                constraint.parse();
                transitionConstraints.add(constraint);
                continue;
            }
        }
    }

    /**
     * @return the allowedValues
     */
    public Map<String, DDLSimpleGroundStateVariableComponentDecisionType> getAllowedValues() {
        return Collections.unmodifiableMap(allowedValues);
    }

    /**
     * @return the transitionConstraints
     */
    public List<DDLSimpleGroundStateVariableTransitionConstraint> getTransitionConstraints() {
        return Collections.unmodifiableList(transitionConstraints);
    }
}
