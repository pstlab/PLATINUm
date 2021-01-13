package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLProblem extends CommonTree {

    private String name;
    private String domainName;
    private final Map<String, DDLInstantiatedComponentDecision> componentDecisions = new LinkedHashMap<String, DDLInstantiatedComponentDecision>();
    private final List<DDLTemporalRelation> temporalRelations = new ArrayList<DDLTemporalRelation>();
    private final List<DDLParameterConstraint> parameterConstraints = new ArrayList<DDLParameterConstraint>();

    public DDLProblem(Token payload) {
        super(payload);
    }

    public void parse() {
        name = getText();
        domainName = getChild(0).getText();
        for (int i = 1; i < getChildCount(); i++) {
            Tree child = getChild(i);
            if (child instanceof DDLInstantiatedComponentDecision) {
                DDLInstantiatedComponentDecision component_decision = ((DDLInstantiatedComponentDecision) child);
                component_decision.parse();
                if (componentDecisions.containsKey(component_decision.getId())) {
                    throw new RuntimeException("Duplicate component decision id in problem definition");
                }
                componentDecisions.put(component_decision.getId(), component_decision);
                continue;
            }
            if (child instanceof DDLTemporalRelation) {
                DDLTemporalRelation comp_type = ((DDLTemporalRelation) child);
                comp_type.parse();
                temporalRelations.add(comp_type);
                continue;
            }
            if (child instanceof DDLParameterConstraint) {
                DDLParameterConstraint comp_type = ((DDLParameterConstraint) child);
                comp_type.parse();
                parameterConstraints.add(comp_type);
                continue;
            }
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the temporalRelations
     */
    public List<DDLTemporalRelation> getTemporalRelations() {
        return Collections.unmodifiableList(temporalRelations);
    }

    /**
     * @return the parameterConstraints
     */
    public List<DDLParameterConstraint> getParameterConstraints() {
        return Collections.unmodifiableList(parameterConstraints);
    }

    /**
     * @return the componentDecisions
     */
    public Map<String, DDLInstantiatedComponentDecision> getComponentDecisions() {
        return Collections.unmodifiableMap(componentDecisions);
    }

    /**
     * @return the domain
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domainName = domain;
    }
}
