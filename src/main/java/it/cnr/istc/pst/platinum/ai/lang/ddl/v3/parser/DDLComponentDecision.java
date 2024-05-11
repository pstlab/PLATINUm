package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public abstract class DDLComponentDecision extends CommonTree {

	protected DDLComponentDecisionType decisionType;
    protected String name;

    /**
     * 
     * @param payload
     * @param type
     */
    public DDLComponentDecision(Token payload, DDLComponentDecisionType type) {
        super(payload);
        this.decisionType = type;
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
     * @return
     */
    public DDLComponentDecisionType getDecisionType() {
    	return this.decisionType;
    }
}
