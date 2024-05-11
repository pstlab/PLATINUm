package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.Token;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class DDLSimpleGroundStateVariableComponentDecision extends DDLComponentDecision {

    private final List<String> parameters = new ArrayList<String>();

    /**
     * 
     * @param payload
     */
    public DDLSimpleGroundStateVariableComponentDecision(Token payload) {
    	super(payload, DDLComponentDecisionType.GROUND);
    }

    void parse() {
	name = getText();
	for (int i = 0; i < getChildCount(); i++) {
	    parameters.add(getChild(i).getText());
	}
    }

    /**
     * @return the parameters
     */
    public List<String> getParameters() {
	return Collections.unmodifiableList(parameters);
    }
}
