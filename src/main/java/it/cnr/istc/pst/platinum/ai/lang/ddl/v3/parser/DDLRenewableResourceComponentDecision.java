package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.Token;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class DDLRenewableResourceComponentDecision extends DDLComponentDecision {

    private String requirementName;
    private Object value;
    private final List<String> parameters = new ArrayList<String>();

    /**
     * 
     * @param payload
     */
    public DDLRenewableResourceComponentDecision(Token payload) {
    	super(payload, DDLComponentDecisionType.RENEWABLE_RESOURCE_REQUIREMENT);
    }

    @Override
    void parse() {
	requirementName = getChild(0).getText();
	if (getChild(0).getChildCount() > 0) {
	    value = Long.parseLong(getChild(0).getChild(0).getText());
	}
	for (int i = 1; i < getChildCount(); i++) {
	    parameters.add(getChild(i).getText());
	}
    }

    public String getRequirementName() {
	return requirementName;
    }

    public Object getValue() {
	return value;
    }

    public List<String> getParameters() {
	return Collections.unmodifiableList(parameters);
    }
}
