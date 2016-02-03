/*****************************************************************************************
 * Source File: ArithmeticLiteralValue.java
 ****************************************************************************************/
package net.ruready.parser.math.entity.value;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.util.HashCodeUtil;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A literal symbol/word in mathematical and logical expressions analyzed by the parser.
 * This is NOT a variable (it does not have a value, just a name).
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
public class LiteralValue implements MathValue
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(LiteralValue.class);

	// ========================= FIELDS ====================================

	// String corresponding to this variable
	private final String name;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a symbolic variable with a name. The name is converted to lower case.
	 * 
	 * @param name
	 *            variable name (must follow conventions)
	 */
	public LiteralValue(final String name)
	{
		super();
		this.name = ((name == null) ? null : name.toLowerCase());
	}

	// ========================= STATIC METHODS ============================

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * Prints the variable's name.
	 */
	@Override
	public String toString()
	{
		return name;
	}

	/**
	 * Must be overridden when <code>equals()</code> is.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int result = HashCodeUtil.SEED;

		// Mandatory fields used in equals()
		result = HashCodeUtil.hash(result, name);

		// Optional fields that are likely to be different for different
		// instances

		return result;
	}

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
	public <B extends AbstractMathValueVisitor> void accept(B visitor)
	{
		visitor.visit(this);
	}

	// ========================= IMPLEMENTATION: Identifiable =================

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public final String getType()
	{
		return getIdentifier().getType();
	}

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public MathValueID getIdentifier()
	{
		return MathValueID.LITERAL;
	}

	// ========================= IMPLEMENTATION: MathValue ====================
	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	public String simpleName()
	{
		return "Literal";
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isArithmeticOperation()
	 */
	public boolean isArithmeticOperation()
	{
		return false;
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isNumerical()
	 */
	public boolean isNumerical()
	{
		return false;
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isSignOp()
	 */
	public boolean isSignOp()
	{
		return false;
	}

	// ========================= IMPLEMENTATION: TolerantlyComparable ======

	/**
	 * Result of equality of two tokens up to a finite tolerance. Delegates to their
	 * values. If at least one of the values is <code>null</code>, they are not equal.
	 * 
	 * @param obj
	 *            The other <code>MathToken</code> object.
	 * @param tol
	 *            tolerance of equality, if we compare to a finite precision (for n digits
	 *            of accuracy, use tol = 10^{-n}).
	 * @return the result of tolerant equality of two evaluable quantities. Returns
	 *         {@link #EQUAL} if they are tolerantly equal; returns {@link #INDETERMINATE}
	 *         if tolerant equality cannot be returned; otherwise, returns a number that
	 *         is different from both constants, e.g., {@link #NON_EQUAL}.
	 */
	final public int tolerantlyEquals(MathValue obj, double tol)
	{
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		// logger.trace("MathToken tolerantlyEquals(): this.class " +
		// getClass() + " obj.class " + obj.getClass());
		if (this == obj)
		{
			return TolerantlyComparable.EQUAL;
		}
		// The following clause is usually to be avoided, but doesn't violate
		// the symmetry principle because this method is declared final. On
		// the other hand, we do need sub-classes to be compared with
		// MathToken.
		if ((obj == null) || (!(obj instanceof LiteralValue)))
		{
			return TolerantlyComparable.NOT_EQUAL;
		}
		// Cast to friendlier version
		LiteralValue other = (LiteralValue) obj;

		return other.equals(this) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
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
		LiteralValue other = (LiteralValue) obj;

		return (name == null) ? false : name.equals(other.getName());
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

}
