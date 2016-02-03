/*****************************************************************************************
 * Source File: IntegerValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue;

import java.io.Serializable;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.math.real.RealUtil;
import net.ruready.common.math.real.SpecialFunctions;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.util.HashCodeUtil;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.arithmetic.entity.numericalvalue.util.NumericalValueUtil;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

/**
 * Our own integer-valued number. Immutable. Comparison between two integers is defined by
 * equality up to a floating point tolerance.
 * <p>
 * -------------------------------------------------------------------------<br>
 * (c) 2006-2007 Continuing Education, University of Utah<br>
 * All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * <p>
 * This file is part of the RUReady Program software.<br>
 * Contact: Nava L. Livne <code>&lt;nlivne@aoce.utah.edu&gt;</code><br>
 * Academic Outreach and Continuing Education (AOCE)<br>
 * 1901 East South Campus Dr., Room 2197-E<br>
 * University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 * -------------------------------------------------------------------------
 * 
 * @author Oren E. Livne <code>&lt;olivne@aoce.utah.edu&gt;</code>
 * @version Aug 14, 2007
 */
public class IntegerValue implements NumericalValue, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1;

	// Real constants
	public static final IntegerValue ZERO = new IntegerValue(0);

	public static final IntegerValue ONE = new IntegerValue(1);

	public static final IntegerValue MINUS_ONE = new IntegerValue(-1);

	// ========================= FIELDS ====================================

	/**
	 * The actual value.
	 */
	private long value;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Convert a double into an integer. Round to the nearest integer.
	 * 
	 * @param value
	 *            original real value
	 * @throws NumberFormatException
	 */
	public IntegerValue(double value)
	{
		// if (!RealUtil.doubleEquals(value, Math.ceil(value))) {
		// throw new NumberFormatException("Bad double value for IntegerValue
		// constructor: "
		// }
		this.value = Integer.parseInt(CommonNames.MISC.EMPTY_STRING
				+ (int) Math.ceil(value));
	}

	/**
	 * Convert a string into an integer.
	 * 
	 * @param s
	 *            string value
	 * @throws NumberFormatException
	 *             if the string is not an integer value
	 */
	public IntegerValue(String s) throws NumberFormatException
	{
		this.value = Integer.parseInt(s);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return CommonNames.MISC.EMPTY_STRING + value;
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
		result = HashCodeUtil.hash(result, value);
		return result;
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public IntegerValue clone()
	{
		try
		{
			IntegerValue copy = (IntegerValue) super.clone();
			return copy;
		}
		catch (CloneNotSupportedException e)
		{
			// this shouldn't happen, because we are Cloneable
			throw new InternalError("clone() failed: " + e.getMessage());
		}
	}

	// ========================= IMPLEMENTATION: Comparable ===============

	/**
	 * Result of comparing two integers. They They are equal if and only if their
	 * <code>value</code> fields are equal up to a relative tolerance of 1e-16. If not,
	 * the <code>&lt;, &gt;</code> relations are identical to those of doubles.
	 * 
	 * @param other
	 *            the other <code>IntegerValue</code>
	 * @return the result of comparison
	 */
	public int compareTo(NumericalValue obj)
	{
		IntegerValue other = (IntegerValue) obj;
		return new Long(value).compareTo(other.getValue());
	}

	/**
	 * Result of equality of two integers. They are equal if and only if their
	 * <code>value</code> fields are equal.
	 * 
	 * @param obj
	 *            The other <code>IntegerValue</code> object.
	 * @return the result of equality
	 */
	@Override
	public boolean equals(Object obj)
	{
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
		IntegerValue other = (IntegerValue) obj;

		return (this.compareTo(other) == 0);
	}

	// ========================= IMPLEMENTATION: TolerantlyComparable ========

	/**
	 * Result of equality of two integers up to a finite tolerance. They are equal if and
	 * only if their <code>value</code> fields are equal up to a relative tolerance
	 * <code>tol</code>.
	 * 
	 * @param obj
	 *            The other <code>Real</code> object.
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
		if (this == obj)
		{
			return TolerantlyComparable.EQUAL;
		}
		// The following clause is usually to be avoided, but doesn't violate
		// the symmetry principle because this method is declared final. On
		// the other hand, we do need sub-classes to be compared with
		// ComplexValue.
		if ((obj == null) || (!(obj instanceof IntegerValue)))
		{
			return TolerantlyComparable.NOT_EQUAL;
		}
		// Cast to friendlier version
		IntegerValue other = (IntegerValue) obj;

		// Check for infinities, NaNs
		if (!equalsWhenNan(other))
		{
			// If it's infinity vs. -infinity, infinity vs. nan
			// or nan vs. -infinity, they're different (compare
			// re part vs. re part, im part vs. im part).
			return TolerantlyComparable.NOT_EQUAL_WHEN_NAN;
		}

		// Can't decide on infinity vs. infinity, -infinity vs.
		// -infinity, nan vs. nan ==> bad sample, skip.
		if (isInfinite() || isNaN() || other.isInfinite() || other.isNaN())
		{
			return TolerantlyComparable.INDETERMINATE;
		}

		// This a tolerance-relative evaluation. See the Double implementation.
		return RealUtil.doubleEquals(value, other.getValue()) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
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
		MathValueVisitorUtil.accept(this, visitor);
	}

	// ========================= IMPLEMENTATION: Identifiable =================

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return getIdentifier().getType();
	}

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public MathValueID getIdentifier()
	{
		return MathValueID.ARITHMETIC_NUMBER;
	}

	// ========================= IMPLEMENTATION: MathValue =================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	public String simpleName()
	{
		return "Integer";
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
		return true;
	}

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#isSignOp()
	 */
	public boolean isSignOp()
	{
		return false;
	}

	// ========================= IMPLEMENTATION: NumericalValue ===========

	/**
	 * Corresponding arithmetic mode to this type of numerical value.
	 * 
	 * @return corresponding arithmetic mode
	 */
	public ArithmeticMode getArithmeticMode()
	{
		return ArithmeticMode.INTEGER;
	}

	/**
	 * Convert a double to a <code>Value</code> of corresponding type to this arithmetic
	 * mode (an enumerated factory method).
	 * 
	 * @param d
	 *            double value
	 * @return <code>Value</code> object
	 */
	public NumericalValue createValue(double d)
	{
		return new IntegerValue(d);
	}

	/**
	 * Return false if one number is infinite and the other is not, or one number is Nan
	 * and the other one is not. An integer is always non-NaN, so this method must return
	 * <code>true</code>.
	 * 
	 * @param obj
	 *            long value to compare with.
	 * @return the result of "equality". Always returns <code>true</code>.
	 */
	public boolean equalsWhenNan(NumericalValue obj)
	{
		// An integer is always non-NaN, so this method must return true
		return true;
	}

	/**
	 * Returns <tt>true</tt> if either the real or imaginary component of this
	 * <tt>Complex</tt> is an infinite value.Always returns false.
	 * <p>
	 * 
	 * @return Always returns false.
	 */
	public boolean isInfinite()
	{
		return false;
	} // end isInfinite()

	/**
	 * Returns <tt>true</tt> if either the real or imaginary component of this
	 * <tt>Complex</tt> is a Not-a-Number (<tt>NaN</tt>) value.
	 * <p>
	 * 
	 * @return Always returns false.
	 */
	public boolean isNaN()
	{
		return false;
	} // end isNaN()

	/**
	 * Updates this <code>Value</code> according to the specified <code>String</code>.
	 * 
	 * @param ss
	 *            the string to be parsed.
	 * @exception NumberFormatException
	 *                if the string does not contain a parsable <code>double</code>.
	 */
	public void parseValue(String ss) throws NumberFormatException
	{
		value = Integer.parseInt(CommonNames.MISC.EMPTY_STRING + value);
	}

	/**
	 * ===== UNARY ARITHMETIC OPERATIONS (op(this)) =====
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.
	 */

	public NumericalValue EXP()
	{
		return new IntegerValue(Math.exp(value));
	}

	public NumericalValue SQRT()
	{
		throw NumericalValueUtil.unsupportedOperationException("SQRT");
	}

	public NumericalValue CBRT()
	{
		throw NumericalValueUtil.unsupportedOperationException("CBRT");
	}

	public NumericalValue LN()
	{
		throw NumericalValueUtil.unsupportedOperationException("LN");
	}

	public NumericalValue LOG10()
	{
		throw NumericalValueUtil.unsupportedOperationException("LOG10");
	}

	public NumericalValue SIN()
	{
		throw NumericalValueUtil.unsupportedOperationException("SIN");
	}

	public NumericalValue COS()
	{
		throw NumericalValueUtil.unsupportedOperationException("COS");
	}

	public NumericalValue TAN()
	{
		throw NumericalValueUtil.unsupportedOperationException("TAN");
	}

	public NumericalValue ASIN()
	{
		throw NumericalValueUtil.unsupportedOperationException("ASIN");
	}

	public NumericalValue ACOS()
	{
		throw NumericalValueUtil.unsupportedOperationException("ACOS");
	}

	public NumericalValue ATAN()
	{
		throw NumericalValueUtil.unsupportedOperationException("ATAN");
	}

	public NumericalValue CSC()
	{
		throw NumericalValueUtil.unsupportedOperationException("CSC");
	}

	public NumericalValue SEC()
	{
		throw NumericalValueUtil.unsupportedOperationException("SEC");
	}

	public NumericalValue COT()
	{
		throw NumericalValueUtil.unsupportedOperationException("COT");
	}

	public NumericalValue SINH()
	{
		throw NumericalValueUtil.unsupportedOperationException("SINH");
	}

	public NumericalValue COSH()
	{
		throw NumericalValueUtil.unsupportedOperationException("COSH");
	}

	public NumericalValue TANH()
	{
		throw NumericalValueUtil.unsupportedOperationException("TANH");
	}

	public NumericalValue ASINH()
	{
		throw NumericalValueUtil.unsupportedOperationException("ASINH");
	}

	public NumericalValue ACOSH()
	{
		throw NumericalValueUtil.unsupportedOperationException("ACOSH");
	}

	public NumericalValue ATANH()
	{
		throw NumericalValueUtil.unsupportedOperationException("ATANH");
	}

	public NumericalValue ABS()
	{
		return new IntegerValue((value < 0) ? (-value) : value);
	}

	public NumericalValue FACT()
	{
		return new IntegerValue(SpecialFunctions.fact(value));
	}

	public NumericalValue NEGATE()
	{
		return new IntegerValue(-value);
	}

	public NumericalValue RECIP()
	{
		throw NumericalValueUtil.unsupportedOperationException("RECIP");
	}

	public NumericalValue SGN()
	{
		return new IntegerValue(Math.signum(value));
	}

	public NumericalValue FLOOR()
	{
		return new IntegerValue(value);
	}

	public NumericalValue CEIL()
	{
		return new IntegerValue(value);
	}

	/**
	 * ===== BINARY ARITHMETIC OPERATIONS (this op y) ====
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.
	 */

	public NumericalValue PLUS(NumericalValue y)
	{
		return new IntegerValue(value + ((IntegerValue) y).getValue());
	}

	public NumericalValue MINUS(NumericalValue y)
	{
		return new IntegerValue(value - ((IntegerValue) y).getValue());
	}

	public NumericalValue TIMES(NumericalValue y)
	{
		return new IntegerValue(value * ((IntegerValue) y).getValue());
	}

	public NumericalValue OVER(NumericalValue y)
	{
		return new IntegerValue(value / ((IntegerValue) y).getValue());
	}

	public NumericalValue POWER(NumericalValue y)
	{
		return new IntegerValue(Math.pow(value, ((IntegerValue) y).getValue()));
	}

	public NumericalValue MOD(NumericalValue y)
	{
		return new IntegerValue(value % ((IntegerValue) y).getValue());
	}

	/**
	 * @param y
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue#LOG(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public NumericalValue LOG(NumericalValue y)
	{
		throw NumericalValueUtil.unsupportedOperationException("LOG");
	}

	/**
	 * @param y
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue#ROOT(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public NumericalValue ROOT(NumericalValue y)
	{
		throw NumericalValueUtil.unsupportedOperationException("ROOT");
	}

	public void PLUS_EQUAL(NumericalValue y)
	{
		value += ((IntegerValue) y).getValue();
	}

	public void MINUS_EQUAL(NumericalValue y)
	{
		value -= ((IntegerValue) y).getValue();
	}

	public void TIMES_EQUAL(NumericalValue y)
	{
		value *= ((IntegerValue) y).getValue();
	}

	public void OVER_EQUAL(NumericalValue y)
	{
		value /= ((IntegerValue) y).getValue();
	}

	/**
	 * ===============================================================================<br>
	 * DOUBLE-VALUED UNARY OPERATIONS.
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.<br>
	 * ===============================================================================
	 */
	public double d_ABS()
	{
		return Math.abs(value);
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================

	public long getValue()
	{
		return value;
	}
}
