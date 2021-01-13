package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import org.antlr.runtime.Token;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLEnumerationParameterConstraint extends DDLParameterConstraint {

    private DDLEnumerationParameterConstraintType constraintType;
    private String leftTerm;
    private String variable;
    private String value;

    /**
     * 
     * @param payload
     */
    public DDLEnumerationParameterConstraint(Token payload) {
        super(payload, DDLParameterConstraintType.ENUMERATION);
    }

    @Override
    void parse() {
        String constraint_type = getText();
        if (constraint_type.equals("=")) {
            constraintType = DDLEnumerationParameterConstraintType.EQ;
        }
        if (constraint_type.equals("!=")) {
            constraintType = DDLEnumerationParameterConstraintType.NEQ;
        }
        leftTerm = getChild(0).getText();
        if (getChild(1).getType() == ddl3Lexer.VarID) {
            variable = getChild(1).getText();
        } else {
            value = getChild(1).getText();
        }
    }

    /**
     * @return the constraintType
     */
    public DDLEnumerationParameterConstraintType getEnumerationConstraintType() {
        return constraintType;
    }

    /**
     * @return the leftTerm
     */
    public String getLeftTerm() {
        return leftTerm;
    }

    /**
     * @return the variable
     */
    public String getVariable() {
        return variable;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    boolean isBinary() {
        return true;
    }

    public enum DDLEnumerationParameterConstraintType {

        EQ, NEQ;
    }
}
