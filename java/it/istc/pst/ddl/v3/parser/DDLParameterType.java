package it.istc.pst.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public abstract class DDLParameterType extends CommonTree {

	protected DDLParameterConstraintType type;
    protected String name;

    DDLParameterType(Token payload, DDLParameterConstraintType type) {
        super(payload);
        this.type = type;
    }

    abstract void parse();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     */
    public DDLParameterConstraintType getDomainType() {
    	return this.type;
    }
}
