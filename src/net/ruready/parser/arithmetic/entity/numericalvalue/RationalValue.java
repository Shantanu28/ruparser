/*****************************************************************************************
 * Source File: RationalValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue;

import java.io.Serializable;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.math.rational.Rational;
import net.ruready.common.math.real.RealUtil;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.arithmetic.entity.numericalvalue.util.NumericalValueUtil;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

/**
 * Our own rational-valued number. Immutable. Comparison between two rationals is defined
 * by equality up to a floating point tolerance. This is an adapter between the
 * third-party implementation of Rational and the NumericalValue interface.
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
public class RationalValue extends Rational implements NumericalValue, Serializable
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1;

	// Real constants
	public static final RationalValue ZERO = new RationalValue(0.0D);

	public static final RationalValue ONE = new RationalValue(1.0D);

	public static final RationalValue MINUS_ONE = new RationalValue(-1.0D);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Convert a double into an rational.
	 * 
	 * @param value
	 *            original real value
	 * @throws NumberFormatException
	 *             if the double is not an integer value
	 */
	public RationalValue(double value)
	{
		super(value);
	}

	/**
	 * Convert a string into a rational.
	 * 
	 * @param s
	 *            string value
	 * @throws NumberFormatException
	 *             if the string is not an integer value
	 */
	public RationalValue(String s) throws NumberFormatException
	{
		super(s);
	}

	/**
	 * Wrap a <code>Rational</code>.
	 * 
	 * @param r
	 *            rational object
	 */
	public RationalValue(Rational r)
	{
		super(r.getNumerator(), r.getDenominator());
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a deep copy of this object. Must be implemented for this object to serve as
	 * a target of an assembly.
	 * 
	 * @return a deep copy of this object.
	 */
	@Override
	public RationalValue clone()
	{
		RationalValue copy = (RationalValue) super.clone();
		return copy;
	}

	// ========================= IMPLEMENTATION: Comparable ===============

	/**
	 * Result of comparing two rationals. This is only needed because Rational extends
	 * Comparable&lt;Rational&gt; and we must implement Comparable&lt;NumericalValue&gt;.
	 * Calls the Rational compareTo() method.
	 * 
	 * @param other
	 *            the other <code>RationalValue</code>
	 * @return the result of comparison
	 */
	public int compareTo(NumericalValue obj)
	{
		return super.compareTo((RationalValue) obj);
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
		if ((obj == null) || (!(obj instanceof RationalValue)))
		{
			return TolerantlyComparable.NOT_EQUAL;
		}
		// Cast to friendlier version
		RationalValue other = (RationalValue) obj;

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
		return RealUtil.doubleEquals(super.doubleValue(), other.doubleValue()) ? TolerantlyComparable.EQUAL
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
		return "Rational";
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
		return ArithmeticMode.RATIONAL;
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
		return new RationalValue(d);
	}

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
		if ((obj == null) || (!(obj instanceof RationalValue)))
		{
			return false;
		}
		// Cast to friendlier version
		RationalValue other = (RationalValue) obj;

		double value = this.doubleValue();
		double otherValue = other.doubleValue();
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
	 * <tt>Complex</tt> is an infinite value.Always returns false.
	 * <p>
	 * 
	 * @return Always returns false.
	 */
	public boolean isInfinite()
	{
		return super.isInfinity();
	} // end isInfinite()

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
		new RationalValue(ss).clone();
	}

	/**
	 * ===== UNARY ARITHMETIC OPERATIONS (op(this)) =====
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.
	 */

	public NumericalValue EXP()
	{
		return new RationalValue(Math.exp(super.doubleValue()));
	}

	public NumericalValue SQRT()
	{
		return new RationalValue(Math.sqrt(super.doubleValue()));
	}

	public NumericalValue CBRT()
	{
		return new RationalValue(Math.cbrt(super.doubleValue()));
	}

	public NumericalValue LN()
	{
		return new RationalValue(Math.log(super.doubleValue()));
	}

	public NumericalValue LOG10()
	{
		return new RationalValue(Math.log10(super.doubleValue()));
	}

	public NumericalValue SIN()
	{
		return new RationalValue(Math.sin(super.doubleValue()));
	}

	public NumericalValue COS()
	{
		return new RationalValue(Math.cos(super.doubleValue()));
	}

	public NumericalValue TAN()
	{
		return new RationalValue(Math.tan(super.doubleValue()));
	}

	public NumericalValue ASIN()
	{
		return new RationalValue(Math.asin(super.doubleValue()));
	}

	public NumericalValue ACOS()
	{
		return new RationalValue(Math.acos(super.doubleValue()));
	}

	public NumericalValue ATAN()
	{
		return new RationalValue(Math.atan(super.doubleValue()));
	}

	public NumericalValue CSC()
	{
		return new RationalValue(1.0 / Math.sin(super.doubleValue()));
	}

	public NumericalValue SEC()
	{
		return new RationalValue(1.0 / Math.cos(super.doubleValue()));
	}

	public NumericalValue COT()
	{
		return new RationalValue(1.0 / Math.tan(super.doubleValue()));
	}

	public NumericalValue SINH()
	{
		return new RationalValue(Math.sinh(super.doubleValue()));
	}

	public NumericalValue COSH()
	{
		return new RationalValue(Math.cosh(super.doubleValue()));
	}

	public NumericalValue TANH()
	{
		return new RationalValue(Math.tanh(super.doubleValue()));
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
		RationalValue result = this.clone();
		result.abs();
		return result;
	}

	public NumericalValue FACT()
	{
		throw NumericalValueUtil.unsupportedOperationException("FACT");
	}

	public NumericalValue NEGATE()
	{
		RationalValue result = this.clone();
		result.negate();
		return result;
	}

	public NumericalValue RECIP()
	{
		return new RationalValue(recip());
	}

	public NumericalValue SGN()
	{
		return new RationalValue(Math.signum(super.doubleValue()));
	}

	public NumericalValue FLOOR()
	{
		return new RationalValue(Math.floor(super.doubleValue()));
	}

	public NumericalValue CEIL()
	{
		return new RationalValue(Math.ceil(super.doubleValue()));
	}

	/**
	 * ===== BINARY ARITHMETIC OPERATIONS (this op y) ====
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.
	 */

	public NumericalValue PLUS(NumericalValue y)
	{
		return new RationalValue(this.add((RationalValue) y));
	}

	public NumericalValue MINUS(NumericalValue y)
	{
		return new RationalValue(this.subtract((RationalValue) y));
	}

	public NumericalValue TIMES(NumericalValue y)
	{
		return new RationalValue(this.multiply((RationalValue) y));
	}

	public NumericalValue OVER(NumericalValue y)
	{
		return new RationalValue(this.divide((RationalValue) y));
	}

	public NumericalValue POWER(NumericalValue y)
	{
		return new RationalValue(Math.pow(doubleValue(), ((RationalValue) y)
				.doubleValue()));
	}

	/**
	 * @param y
	 * @return
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue#MOD(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public NumericalValue MOD(NumericalValue y)
	{
		throw NumericalValueUtil.unsupportedOperationException("MOD");
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
		this.doRawAdd((RationalValue) y);
	}

	public void MINUS_EQUAL(NumericalValue y)
	{
		this.doRawSubtract((RationalValue) y);
	}

	public void TIMES_EQUAL(NumericalValue y)
	{
		this.doRawMultiply((RationalValue) y);
	}

	public void OVER_EQUAL(NumericalValue y)
	{
		this.doRawDivide((RationalValue) y);
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
		return d_abs();
	}

	// ========================= METHODS ===================================

	// ========================= GETTERS & SETTERS =========================
}
