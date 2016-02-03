/*****************************************************************************************
 * Source File: NumericalValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue;

import net.ruready.common.pointer.PubliclyCloneable;
import net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue;

/**
 * A publicly cloneable comparable object representing a numerical value in some field of
 * values (e.g. complex, real). clone() returns a deep copy of this object. Objects that
 * appear in parser targets must implement this interface. This represents a numerical
 * value that an expression evaluates to. Unifies rational, real, etc. types that are
 * publicly cloneable. Serves as a backbone to grid/mesh values in the arithmetic
 * sub-parser.
 * <p>
 * Value <i>should be immutable</i>.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E University
 *         of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07
 *         Continuing Education , University of Utah . All copyrights reserved. U.S.
 *         Patent Pending DOCKET NO. 00846 25702.PROV
 * @version 26/02/2006
 * @is.Immutable
 */
public interface NumericalValue extends Comparable<NumericalValue>, PubliclyCloneable,
		ArithmeticValue
{
	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Corresponding arithmetic mode to this type of numerical value (fields of values
	 * that this object belongs to).
	 * 
	 * @return corresponding arithmetic mode
	 */
	ArithmeticMode getArithmeticMode();

	/**
	 * Convert a double to a <code>Value</code> of corresponding type to this arithmetic
	 * mode (an enumerated factory method).
	 * 
	 * @param d
	 *            double value
	 * @return <code>Value</code> object
	 */
	NumericalValue createValue(double d);

	/**
	 * Return false if one number is infinite and the other is not, or one number is Nan
	 * and the other one is not. Otherwise return true.
	 * 
	 * @param other
	 *            long value to compare with.
	 * @return the result of "equality"
	 */
	boolean equalsWhenNan(NumericalValue other);

	/**
	 * Returns <tt>true</tt> if either the real or imaginary component of this
	 * <tt>Complex</tt> is an infinite value.
	 * <p>
	 * 
	 * @return <tt>true</tt> if either component of the <tt>Complex</tt> object is
	 *         infinite; <tt>false</tt>, otherwise.
	 *         <p>
	 */
	boolean isInfinite();

	/**
	 * Returns <tt>true</tt> if either the real or imaginary component of this
	 * <tt>Complex</tt> is a Not-a-Number (<tt>NaN</tt>) value.
	 * <p>
	 * 
	 * @return <tt>true</tt> if either component of the <tt>Complex</tt> object is
	 *         <tt>NaN</tt>; <tt>false</tt>, otherwise.
	 *         <p>
	 */
	boolean isNaN();

	/**
	 * Updates this <code>Value</code> according to the specified <code>String</code>.
	 * 
	 * @param s
	 *            the string to be parsed.
	 * @exception NumberFormatException
	 *                if the string does not contain a parsable <code>double</code>.
	 */
	void parseValue(String ss) throws NumberFormatException;

	/**
	 * ==================================================================================<br>
	 * UNARY ARITHMETIC OPERATIONS (op(this))
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.<br>
	 * ===================================================================================
	 */
	NumericalValue EXP();

	NumericalValue SQRT();

	NumericalValue CBRT();

	NumericalValue LN();

	NumericalValue LOG10();

	NumericalValue SIN();

	NumericalValue COS();

	NumericalValue TAN();

	NumericalValue ASIN();

	NumericalValue ACOS();

	NumericalValue ATAN();

	NumericalValue CSC();

	NumericalValue SEC();

	NumericalValue COT();

	NumericalValue SINH();

	NumericalValue COSH();

	NumericalValue TANH();

	NumericalValue ASINH();

	NumericalValue ACOSH();

	NumericalValue ATANH();

	NumericalValue ABS();

	NumericalValue FACT();

	NumericalValue NEGATE();

	NumericalValue RECIP();

	NumericalValue SGN();

	NumericalValue FLOOR();

	NumericalValue CEIL();

	/**
	 * ===============================================================================<br>
	 * BINARY ARITHMETIC OPERATIONS (this op y)
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.<br>
	 * ===============================================================================
	 */
	NumericalValue PLUS(NumericalValue y);

	NumericalValue MINUS(NumericalValue y);

	NumericalValue TIMES(NumericalValue y);

	NumericalValue OVER(NumericalValue y);

	NumericalValue POWER(NumericalValue y);

	NumericalValue MOD(NumericalValue y);

	NumericalValue LOG(NumericalValue y);

	NumericalValue ROOT(NumericalValue y);

	void PLUS_EQUAL(NumericalValue y);

	void MINUS_EQUAL(NumericalValue y);

	void TIMES_EQUAL(NumericalValue y);

	void OVER_EQUAL(NumericalValue y);

	/**
	 * ===============================================================================<br>
	 * DOUBLE-VALUED UNARY OPERATIONS.
	 * <p>
	 * Names are upper-case to resolve possible name conflicts with inherited methods with
	 * a different return type.<br>
	 * ===============================================================================
	 */
	double d_ABS();
}
