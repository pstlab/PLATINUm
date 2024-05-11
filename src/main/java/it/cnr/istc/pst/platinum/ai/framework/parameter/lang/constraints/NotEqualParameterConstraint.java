package it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints;

/**
 * 
 * @author anacleto
 *
 */
public class NotEqualParameterConstraint extends BinaryParameterConstraint 
{
	/**
	 * 
	 */
	protected NotEqualParameterConstraint() {
		super(ParameterConstraintType.NOT_EQUAL);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[NotEqualParameterConstraint symbol= " + this.type.getSymbol() + "\n- reference= " + this.reference + "\n- target= " + this.target + "\n]";
	}
	
}
