/*****************************************************************************************
 * Source File: ArithmeticLiteralValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.mathvalue;

import net.ruready.common.visitor.Visitable;
import net.ruready.parser.math.entity.value.LiteralValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A literal symbol/word in mathematical expressions analyzed by the parser. This is NOT a
 * variable (it does not have a value, just a name).
 * 
 * @immutable
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version Apr 24, 2007
 */
public class ArithmeticLiteralValue extends LiteralValue implements ArithmeticValue
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ArithmeticLiteralValue.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a symbolic variable with a name. The name is converted to lower case.
	 * 
	 * @param name
	 *            variable name (must follow conventions)
	 */
	public ArithmeticLiteralValue(final String name)
	{
		super(name);
	}

	// ========================= STATIC METHODS ============================

	/**
	 * Check if this is a valid variable value. Must be a non-reserved word.
	 * 
	 * @param var
	 *            variable's name
	 * @return is it a valid name
	 */
	public static boolean isValidVariableName(String var)
	{
		return Variable.isValidVariableName(var);
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= IMPLEMENTATION: Visitable<Visitor<MathValue>>

	/**
	 * Let a visitor process this value type. Part of the TreeVisitor pattern. This calls
	 * back the visitor's <code>visit()</code> method with this item type. Must be
	 * overridden by every item sub-class.
	 * 
	 * @param <B>
	 *            specific visitor type; this method may be implemented for multiple
	 *            classes of {@link Visitable}.
	 * @param visitor
	 *            item visitor whose <code>visit()</code> method is invoked
	 * @see net.ruready.common.visitor.Visitable#accept(net.ruready.common.visitor.Visitor)
	 */
	@Override
	public <B extends AbstractMathValueVisitor> void accept(B visitor)
	{
		MathValueVisitorUtil.accept(this, visitor);
	}

	// ========================= METHODS ===================================

	/**
	 * Equality between two literals based on their names. If at least one of the names is
	 * <code>null</code>, this method returns <code>false</code>. However, if both
	 * values are <code>null</code>, the method returns true if name equality holds as
	 * explained.
	 * 
	 * @param obj
	 *            another variable
	 * @return result of equality of variable names
	 */
	@Override
	public boolean equals(Object obj)
	{
		// TODO: replace with Double equality
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		if (this == obj)
		{
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass()))
		{
			return false;
		}
		// Cast to friendlier version
		ArithmeticLiteralValue other = (ArithmeticLiteralValue) obj;

		return (getName() == null) ? false : getName().equals(other.getName());
	}

	// ========================= GETTERS & SETTERS =========================

}
