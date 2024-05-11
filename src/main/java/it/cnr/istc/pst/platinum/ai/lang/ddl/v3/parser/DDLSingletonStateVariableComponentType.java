package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

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
public class DDLSingletonStateVariableComponentType extends DDLComponentType {

    private final Map<String, DDLSingletonStateVariableComponentDecisionType> allowedValues = new HashMap<String, DDLSingletonStateVariableComponentDecisionType>();
    private final List<DDLSingletonStateVariableTransitionConstraint> transitionConstraints = new ArrayList<DDLSingletonStateVariableTransitionConstraint>();

    public DDLSingletonStateVariableComponentType(Token payload) {
        super(payload);
    }

    @Override
    void parse() {
        name = getText();
        for (int i = 0; i < getChildCount(); i++) {
            Tree child = getChild(i);
            if (child instanceof DDLSingletonStateVariableComponentDecisionType) {
                DDLSingletonStateVariableComponentDecisionType allowed_value = ((DDLSingletonStateVariableComponentDecisionType) child);
                allowed_value.parse();
                allowedValues.put(allowed_value.getName(), allowed_value);
                continue;
            }
            if (child instanceof DDLSingletonStateVariableTransitionConstraint) {
                DDLSingletonStateVariableTransitionConstraint constraint = ((DDLSingletonStateVariableTransitionConstraint) child);
                constraint.parse();
                transitionConstraints.add(constraint);
                continue;
            }
        }
    }

    /**
     * @return the allowedValues
     */
    public Map<String, DDLSingletonStateVariableComponentDecisionType> getAllowedValues() {
        return Collections.unmodifiableMap(allowedValues);
    }

    /**
     * @return the transitionConstraints
     */
    public List<DDLSingletonStateVariableTransitionConstraint> getTransitionConstraints() {
        return Collections.unmodifiableList(transitionConstraints);
    }
}
