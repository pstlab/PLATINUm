package it.istc.pst.ddl.v3.parser;

import org.antlr.runtime.Token;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class DDLConsumableResourceComponentType extends DDLComponentType {

    private int capacity;
    private int minCapacity;

    public DDLConsumableResourceComponentType(Token payload) {
	super(payload);
    }

    @Override
    void parse() {
    	name = getText();
    	minCapacity = Integer.parseInt(getChild(0).getText());
    	capacity = Integer.parseInt(getChild(1).getText());
    }

    public int getCapacity() {
	return capacity;
    }

    public int getMinCapacity() {
	return minCapacity;
    }
}
