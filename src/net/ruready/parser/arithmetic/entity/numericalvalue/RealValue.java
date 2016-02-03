/*****************************************************************************************
 * Source File: RealValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue;

import java.io.Serializable;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.math.real.RealUtil;
import net.ruready.common.math.real.SpecialFunctions;
import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.common.rl.CommonNames;
import net.ruready.common.util.HashCodeUtil;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

/**
 * Our own real-valued number. Immutable. Comparison between two reals is defined by
 * equality up to floating point precision.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version 05/12/2005
 */
public class RealValue implements NumericalValue, PubliclyCloneable, Serializable,
		ArithmeticValue
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1;

	// Real constants
	public static final RealValue ZERO = new RealValue(0.0D);

	public static final RealValue ONE = new RealValue(1.0D);

	public static final RealValue MINUS_ONE = new RealValue(-1.0D);

	// ========================= FIELDS ====================================

	private double value;

	// ========================= CONSTRUCTORS ==============================

	public RealValue(double value)
	{
		this.value = value;
	}

	/**
	 * Round a double.
	 * 
	 * @param value
	 *            original real value
	 * @param tol
	 *            round-off tolerance. For <code>n</code> significant digits, use
	 *            <code>tol=10^{-n}</code>.
	 */
	public RealValue(double value, double tol)
	{
		this.value = RealUtil.round(value, tol);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 *            object to copy from
	 */
	public RealValue(RealValue other)
	{
		this.value = other.getValue();
	}

	/**
	 * @param s
	 * @throws NumberFormatException
	 */
	public RealValue(String s) throws NumberFormatException
	{
		this.value = Double.parseDouble(s);
	}

	// ========================= IMPLEMENTATION: Object ====================

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return CommonNames.MISC.EMPTY_STRING + nicePrint(value);
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
	public RealValue clone()
	{
		try
		{
			RealValue copy = (RealValue) super.clone();
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
	 * Result of comparing two reals based on their double values (using the
	 * {@link Double} <code>compare()</code> function.
	 * 
	 * @param other
	 *            the other <code>NumericalValue</code>
	 * @return the result of comparison
	 */
	public int compareTo(NumericalValue obj)
	{
		RealValue other = (RealValue) obj;
		return Double.compare(value, other.getValue());
	}

	/**
	 * Result of equality of two reals based on their double values (using the
	 * {@link Double} <code>compare()</code> function.
	 * 
	 * @param obj
	 *            The other <code>RealValue</code> object.
	 * @return the result of equality
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
		RealValue other = (RealValue) obj;

		return (this.compareTo(other) == 0);
	}

	// ========================= IMPLEMENTATION: TolerantlyComparable ========

	/**
	 * Result of equality of two reals up to a finite tolerance. They are equal if and
	 * only if their <code>value</code> fields are equal up to a relative tolerance of
	 * tol. This is an implementation of the <code>TolerantlyComparable</code>
	 * interface.
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
		if ((obj == null) || (!(obj instanceof RealValue)))
		{
			return TolerantlyComparable.NOT_EQUAL;
		}
		// Cast to friendlier version
		RealValue other = (RealValue) obj;

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

		return RealUtil.doubleEquals(value, other.getValue(), tol) ? TolerantlyComparable.EQUAL
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
		return "Real";
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

	// ========================= IMPLEMENTATION: Value ====================

	/**
	 * Corresponding arithmetic mode to this type of numerical value.
	 * 
	 * @return corresponding arithmetic mode
	 */
	public ArithmeticMode getArithmeticMode()
	{
		return ArithmeticMode.REAL;
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
		return new RealValue(d);
	}

	/**
	 * Return false if one number is infinite and the other is not, or one number is Nan
	 * and the other one is not. Otherwise return true.
	 * 
	 * @param obj
	 *            long value to compare with.
	 * @return the result of "equality"
	 */
	/**
	 * Return false if one number is infinite and the other is not, or one number is Nan
	 * and the other one is not. Always returns false.
	 * 
	 * @param other
	 *            long value to compare with.
	 * @return the result of "equality". Always returns false.
	 */
	final public boolean equalsWhenNan(NumericalValue obj)
	{
		// Standard checks to ensure the adherence to the general contract of
		// the equals() method
		// logger.trace("ComplexValue tolerantlyEquals(): this.class " +
		// getClass() + " obj.class " + obj.getClass());
		if (this == obj)
		{
			return true;
		}
		// The following clause is usually to be avoided, but doesn't violate
		// the symmetry principle because this method is declared final. On
		// the other hand, we do need sub-classes to be compared with
		// ComplexValue.
		if ((obj == null) || (!(obj instanceof RealValue)))
		{
			return false;
		}
		// Cast to friendlier version
		RealValue other = (RealValue) obj;

		double otherValue = other.getValue();
		if (((value == Double.POSITIVE_INFINITY) && (otherValue != Double.POSITIVE_INFINITY))
				|| ((value == Double.NEGATIVE_INFINITY) && (otherValue != Double.NEGATIVE_INFINITY))
				|| (Double.isNaN(value) && !Double.isNaN(otherValue)))
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns <tt>true</tt> if either the real or imaginary component of this
	 * <tt>Complex</tt> is an infinite value.
	 * <p>
	 * 
	 * @return <tt>true</tt> if either component of the <tt>Complex</tt> object is
	 *         infinite; <tt>false</tt>, otherwise.
	 *         <p>
	 */
	public boolean isInfinite()
	{
		return Double.isInfinite(value);
	} // end isInfinite()

	/**
	 * Returns <tt>true</tt> if either the real or imaginary component of this
	 * <tt>Complex</tt> is a Not-a-Number (<tt>NaN</tt>) value.
	 * <p>
	 * 
	 * @return <tt>true</tt> if either component of the <tt>Complex</tt> object is
	 *         <tt>NaN</tt>; <tt>false</tt>, otherwise.
	 *         <p>
	 */
	public boolean isNaN()
	{
		return Double.isNaN(value);
	} // end isNaN()

	/**
	 * Updates this <code>Value</code> according to the specified <code>String</code>.
	 * 
	 * @param s
	 *            the string to be parsed.
	 * @exception NumberFormatException
	 *                if the string does not contain a parsable <code>double</code>.
	 */
	public void parseValue(String ss) throws NumberFormatException
	{
		value = Double.parseDouble(ss);
	}

	/**
	 * ===== UNARY ARITHMETIC OPERATIONS (op(this)) =====
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.
	 */

	public NumericalValue EXP()
	{
		return new RealValue(Math.exp(value));
	}

	public NumericalValue SQRT()
	{
		return new RealValue(Math.sqrt(value));
	}

	public NumericalValue CBRT()
	{
		return new RealValue(Math.cbrt(value));
	}

	public NumericalValue LN()
	{
		return new RealValue(Math.log(value));
	}

	public NumericalValue LOG10()
	{
		return new RealValue(Math.log10(value));
	}

	public NumericalValue SIN()
	{
		return new RealValue(Math.sin(value));
	}

	public NumericalValue COS()
	{
		return new RealValue(Math.cos(value));
	}

	public NumericalValue TAN()
	{
		return new RealValue(Math.tan(value));
	}

	public NumericalValue ASIN()
	{
		return new RealValue(Math.asin(value));
	}

	public NumericalValue ACOS()
	{
		return new RealValue(Math.acos(value));
	}

	public NumericalValue ATAN()
	{
		return new RealValue(Math.atan(value));
	}

	public NumericalValue CSC()
	{
		return new RealValue(SpecialFunctions.csc(value));
	}

	public NumericalValue SEC()
	{
		return new RealValue(SpecialFunctions.sec(value));
	}

	public NumericalValue COT()
	{
		return new RealValue(SpecialFunctions.cot(value));
	}

	public NumericalValue SINH()
	{
		return new RealValue(Math.sinh(value));
	}

	public NumericalValue COSH()
	{
		return new RealValue(Math.cosh(value));
	}

	public NumericalValue TANH()
	{
		return new RealValue(Math.tanh(value));
	}

	public NumericalValue ASINH()
	{
		return new RealValue(RealUtil.asinh(value));
	}

	public NumericalValue ACOSH()
	{
		return new RealValue(RealUtil.acosh(value));
	}

	public NumericalValue ATANH()
	{
		return new RealValue(RealUtil.atanh(value));
	}

	public NumericalValue ABS()
	{
		return new RealValue(Math.abs(value));
	}

	public NumericalValue FACT()
	{
		return new RealValue(SpecialFunctions.fact(value));
	}

	public NumericalValue NEGATE()
	{
		return new RealValue(-value);
	}

	public NumericalValue RECIP()
	{
		return new RealValue(RealValue.ONE.getValue() / value);
	}

	public NumericalValue SGN()
	{
		return new RealValue(Math.signum(value));
	}

	public NumericalValue FLOOR()
	{
		return new RealValue(Math.floor(value));
	}

	public NumericalValue CEIL()
	{
		return new RealValue(Math.ceil(value));
	}

	/**
	 * ===== BINARY ARITHMETIC OPERATIONS (this op y) ====
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.
	 */

	public NumericalValue PLUS(NumericalValue y)
	{
		return new RealValue(value + ((RealValue) y).getValue());
	}

	public NumericalValue MINUS(NumericalValue y)
	{
		return new RealValue(value - ((RealValue) y).getValue());
	}

	public NumericalValue TIMES(NumericalValue y)
	{
		return new RealValue(value * ((RealValue) y).getValue());
	}

	public NumericalValue OVER(NumericalValue y)
	{
		return new RealValue(value / ((RealValue) y).getValue());
	}

	public NumericalValue POWER(NumericalValue y)
	{
		return new RealValue(Math.pow(value, ((RealValue) y).getValue()));
	}

	public NumericalValue MOD(NumericalValue y)
	{
		return new RealValue(value % ((RealValue) y).getValue());
	}

	public NumericalValue LOG(NumericalValue y)
	{
		return new RealValue(SpecialFunctions.log(value, ((RealValue) y).getValue()));
	}

	public NumericalValue ROOT(NumericalValue y)
	{
		return new RealValue(SpecialFunctions.root(value, ((RealValue) y).getValue()));
	}

	public void PLUS_EQUAL(NumericalValue y)
	{
		value += ((RealValue) y).getValue();
	}

	public void MINUS_EQUAL(NumericalValue y)
	{
		value -= ((RealValue) y).getValue();
	}

	public void TIMES_EQUAL(NumericalValue y)
	{
		value *= ((RealValue) y).getValue();
	}

	public void OVER_EQUAL(NumericalValue y)
	{
		value /= ((RealValue) y).getValue();
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

	/**
	 * Nicely print a double.
	 * 
	 * @param x
	 *            a double
	 * @return a nice String representation of <code>x</code>
	 */
	private static String nicePrint(double x)
	{
		long xRounded = java.lang.Math.round(x);
		if (RealUtil.doubleEquals(1.0 * xRounded, x))
		{
			return CommonNames.MISC.EMPTY_STRING + xRounded;
		}
		return CommonNames.MISC.EMPTY_STRING + x;
	}

	// ========================= GETTERS & SETTERS =========================

	public double getValue()
	{
		return value;
	}

}
