package it.uniroma3.epsl2.framework.parameter.lang.constraints;

/**
 * 
 * @author anacleto
 *
 */
public class EqualParameterConstraint extends BinaryParameterConstraint 
{
	/**
	 * 
	 */
	protected EqualParameterConstraint() {
		super(ParameterConstraintType.EQUAL);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[EqualParameterConstraint symbol=" + this.type.getSymbol() + "\n- reference= " + this.reference + "\n- target= " + this.target +"\n]";
	}
}
