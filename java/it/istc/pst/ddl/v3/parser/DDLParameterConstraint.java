package it.istc.pst.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public abstract class DDLParameterConstraint extends CommonTree {

	private DDLParameterConstraintType constraintType;
	
	/**
	 * 
	 * @param payload
	 * @param type
	 */
    public DDLParameterConstraint(Token payload, DDLParameterConstraintType type) {
        super(payload);
        this.constraintType = type;
    }
    
    /**
     * 
     * @return
     */
    public DDLParameterConstraintType getConstraintType() {
		return constraintType;
	}

    abstract void parse();

    abstract boolean isBinary();
}
