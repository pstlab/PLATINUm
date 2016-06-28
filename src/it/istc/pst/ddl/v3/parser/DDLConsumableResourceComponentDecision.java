package it.istc.pst.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.Token;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class DDLConsumableResourceComponentDecision extends DDLComponentDecision {

    private DDLConsumableResourceComponentDecisionType type;
    private String parName;
    private Object value;
    private final List<String> parameters = new ArrayList<String>();

    /**
     * 
     * @param payload
     */
    public DDLConsumableResourceComponentDecision(Token payload) {
    	super(payload, DDLComponentDecisionType.CONSUMABLE_RESOURCE_REQUIREMENT);
    }

    @Override
    void parse() {
	if (getChild(0).getText().equals("PRODUCTION")) {
	    type = DDLConsumableResourceComponentDecisionType.Production;
	}
	if (getChild(0).getText().equals("CONSUMPTION")) {
	    type = DDLConsumableResourceComponentDecisionType.Consumption;
	}
	parName = getChild(1).getText();
	if (getChild(1).getChildCount() > 0) {
	    value = Long.parseLong(getChild(1).getChild(0).getText());
	}
	for (int i = 2; i < getChildCount(); i++) {
	    parameters.add(getChild(i).getText());
	}
    }

    public DDLConsumableResourceComponentDecisionType getComponentDecisionType() {
	return type;
    }

    public String getParameterName() {
	return parName;
    }

    public Object getValue() {
	return value;
    }

    public List<String> getParameters() {
	return Collections.unmodifiableList(parameters);
    }

    public enum DDLConsumableResourceComponentDecisionType {
	Production, Consumption
    }
}
