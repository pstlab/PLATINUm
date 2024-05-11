package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import org.antlr.runtime.Token;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class DDLRenewableResourceComponentType extends DDLComponentType {

    private int capacity;

    public DDLRenewableResourceComponentType(Token payload) {
	super(payload);
    }

    @Override
    void parse() {
	name = getText();
	capacity = Integer.parseInt(getChild(0).getText());
    }

    public int getCapacity() {
	return capacity;
    }
}
