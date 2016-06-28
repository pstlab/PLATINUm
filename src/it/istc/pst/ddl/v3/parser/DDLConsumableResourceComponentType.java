package it.istc.pst.ddl.v3.parser;

import org.antlr.runtime.Token;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class DDLConsumableResourceComponentType extends DDLComponentType {

    private long capacity;
    private long minCapacity;

    public DDLConsumableResourceComponentType(Token payload) {
	super(payload);
    }

    @Override
    void parse() {
	name = getText();
	minCapacity = Long.parseLong(getChild(0).getText());
	capacity = Long.parseLong(getChild(1).getText());
    }

    public long getCapacity() {
	return capacity;
    }

    public long getMinCapacity() {
	return minCapacity;
    }
}
