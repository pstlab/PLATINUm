package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.Token;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLNumericParameterConstraint extends DDLParameterConstraint {

    private DDLNumericParameterConstraintType constraintType;
    private String leftTerm;
    private long leftCoefficient;
    private int absoluteValue = 0;
    private final List<String> rightVariables = new ArrayList<>();
    private final List<Integer> rightCoefficients = new ArrayList<>();

    /**
     * 
     * @param payload
     */
    public DDLNumericParameterConstraint(Token payload) {
        super(payload, DDLParameterConstraintType.NUMERIC);
    }

    @Override
    void parse() {
        String constraint_type = getText();
        if (constraint_type.equals("=")) {
            constraintType = DDLNumericParameterConstraintType.EQ;
        }
        if (constraint_type.equals(">")) {
            constraintType = DDLNumericParameterConstraintType.GT;
        }
        if (constraint_type.equals("<")) {
            constraintType = DDLNumericParameterConstraintType.LT;
        }
        if (constraint_type.equals(">=")) {
            constraintType = DDLNumericParameterConstraintType.GE;
        }
        if (constraint_type.equals("<=")) {
            constraintType = DDLNumericParameterConstraintType.LE;
        }
        if (constraint_type.equals("!=")) {
            constraintType = DDLNumericParameterConstraintType.NEQ;
        }
        if (getChild(0).getType() == ddl3Lexer.VarID) {
            leftTerm = getChild(0).getText();
            leftCoefficient = 1l;
        } else if (getChild(0).getText().equals("*")) {
            long coefficient = getChild(0).getChild(0).getText().equals("INF") ? Long.MAX_VALUE - 1 : Long.parseLong(getChild(0).getChild(0).getText());
            if (getChild(0).getChild(0).getChildCount() > 0) {
                coefficient = getChild(0).getChild(0).getChild(0).getText().equals("-") ? -coefficient : coefficient;
            }
            leftTerm = getChild(1).getChild(1).getText();
            leftCoefficient = coefficient;
        }
        if (getChild(1).getType() == ddl3Lexer.VarID) {
            rightVariables.add(getChild(1).getText());
//            rightCoefficients.add(1l);
            rightCoefficients.add(1);
        } else if (getChild(1).getText().equals("*")) {
            int coefficient = getChild(1).getChild(0).getText().equals("INF") ? Integer.MAX_VALUE - 1 : Integer.parseInt(getChild(1).getChild(0).getText());
            if (getChild(1).getChild(0).getChildCount() > 0) {
                coefficient = getChild(1).getChild(0).getChild(0).getText().equals("-") ? -coefficient : coefficient;
            }
            rightVariables.add(getChild(1).getChild(1).getText());
            rightCoefficients.add(coefficient);
        } else {
            int coefficient = getChild(1).getText().equals("INF") ? Integer.MAX_VALUE - 1 : Integer.parseInt(getChild(1).getText());
            if (getChild(1).getChildCount() > 0) {
                coefficient = getChild(1).getChild(0).getText().equals("-") ? -coefficient : coefficient;
            }
            rightVariables.add(null);
            rightCoefficients.add(coefficient);
        }
        for (int i = 2; i < getChildCount(); i++) {
            if (getChild(i).getText().equals("+")) {
                if (getChild(i).getChild(0).getType() == ddl3Lexer.VarID) {
                    rightVariables.add(getChild(i).getChild(0).getText());
                    rightCoefficients.add(1);
                } else if (getChild(i).getChild(0).getText().equals("*")) {
                    rightVariables.add(getChild(i).getChild(0).getChild(1).getText());
                    rightCoefficients.add(Integer.parseInt(getChild(i).getChild(0).getChild(0).getText()));
                } else {
                    int coefficient = getChild(i).getChild(0).getText().equals("INF") ? Integer.MAX_VALUE - 1 : Integer.parseInt(getChild(i).getChild(0).getText());
                    if (getChild(i).getChildCount() > 0) {
                        coefficient = getChild(i).getChild(0).getText().equals("-") ? -coefficient : coefficient;
                    }
                    absoluteValue += coefficient;
                }
            } else {
                if (getChild(i).getChild(0).getType() == ddl3Lexer.VarID) {
                    rightVariables.add(getChild(i).getChild(0).getText());
                    rightCoefficients.add(-1);
                } else if (getChild(i).getChild(0).getText().equals("*")) {
                    rightVariables.add(getChild(i).getChild(0).getChild(1).getText());
                    rightCoefficients.add(-Integer.parseInt(getChild(i).getChild(0).getChild(0).getText()));
                } else {
                    long coefficient = getChild(i).getChild(0).getText().equals("INF") ? Integer.MAX_VALUE - 1 : Integer.parseInt(getChild(i).getChild(0).getText());
                    if (getChild(i).getChildCount() > 0) {
                        coefficient = getChild(i).getChild(0).getText().equals("-") ? -coefficient : coefficient;
                    }
                    absoluteValue -= coefficient;
                }
            }
        }
    }

    public boolean isBinary() {
        return rightVariables.size() == 1 && absoluteValue == 0 && rightVariables.get(0) != null;
    }

    /**
     * @return the constraintType
     */
    public DDLNumericParameterConstraintType getNumericConstraintType() {
        return constraintType;
    }

    /**
     * @return the leftTerm
     */
    public String getLeftTerm() {
        return leftTerm;
    }

    /**
     * @return the leftCoefficient
     */
    public long getLeftCoefficient() {
        return leftCoefficient;
    }

    /**
     * @return the rightVariables
     */
    public List<String> getRightVariables() {
        return Collections.unmodifiableList(rightVariables);
    }

    /**
     * @return the rightCoefficients
     */
    public List<Integer> getRightCoefficients() {
        return Collections.unmodifiableList(rightCoefficients);
    }

//    public int getAbsoluteValue() {
//        return absoluteValue;
//    }

    public enum DDLNumericParameterConstraintType {

        EQ, LT, GT, LE, GE, NEQ;
    }
}
