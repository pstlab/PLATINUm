package it.istc.pst.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.runtime.Token;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class DDLSingletonStateVariableComponentDecision extends DDLComponentDecision {

    private final List<String> parNames = new ArrayList<String>();
    private final List<Object> values = new ArrayList<Object>();
    private final List<String> parameters = new ArrayList<String>();

    /**
     * 
     * @param payload
     */
    public DDLSingletonStateVariableComponentDecision(Token payload) {
    	super(payload, DDLComponentDecisionType.SINGLETON);
    }

    @Override
    void parse() {
	name = getText();
	boolean pars = false;
	for (int i = 0; i < getChildCount(); i++) {
	    if (getChild(i).getText().equals("(")) {
		pars = true;
		continue;
	    }
	    if (!pars) {
		parNames.add(getChild(i).getText());
		if (getChild(i).getChildCount() > 0) {
		    if (getChild(i).getChild(0).getType() == ddl3Lexer.INT) {
			if (getChild(i).getChild(0).getChildCount() > 0 && getChild(i).getChild(0).getChild(0).getText().equals("-")) {
			    values.add(-Long.parseLong(getChild(i).getChild(0).getText()));
			} else {
			    values.add(Long.parseLong(getChild(i).getChild(0).getText()));
			}
		    } else {
			values.add(getChild(i).getChild(0).getText());
		    }
		} else {
		    values.add(null);
		}
	    } else {
		parameters.add(getChild(i).getText());
	    }
	}
    }

    /**
     * @return the parNames
     */
    public List<String> getParameterNames() {
	return Collections.unmodifiableList(parNames);
    }

    /**
     * @return the values
     */
    public List<Object> getValues() {
	return Collections.unmodifiableList(values);
    }

    /**
     * @return the parameters
     */
    public List<String> getParameters() {
	return Collections.unmodifiableList(parameters);
    }

    /**
     * 
     * @param referenceParName
     * @return
     */
	public int getParameterIndex(String parName) {
		int index = 0;
		boolean found = false;
		while (index < this.parNames.size() && !found) {
			if (this.parNames.get(index).equals(parName)) {
				found = true;
			}
			else {
				index++;
			}
		}
		
		// check if found
		if (!found) {
			throw new RuntimeException("Parameter with label " + parName + " not found");
		}
		
		// get index
		return index;
	}
}
