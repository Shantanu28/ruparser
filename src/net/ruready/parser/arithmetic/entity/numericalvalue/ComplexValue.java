/*****************************************************************************************
 * Source File: ComplexValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.math.complex.Complex;
import net.ruready.common.math.real.RealConstants;
import net.ruready.common.math.real.RealUtil;
import net.ruready.common.visitor.Visitable;
import net.ruready.parser.math.entity.MathValueID;
import net.ruready.parser.math.entity.value.MathValue;
import net.ruready.parser.math.entity.visitor.AbstractMathValueVisitor;
import net.ruready.parser.math.entity.visitor.MathValueVisitorUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A wrapper around Complex that implements the {@link MathValue} interface standard of
 * arithmetic operations.
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
 * @version Jul 18, 2007
 */
public class ComplexValue extends Complex implements NumericalValue
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ComplexValue.class);

	/**
	 * Complex constants - value version
	 */
	public static final ComplexValue ZERO_VALUE = new ComplexValue(Complex.ZERO);

	public static final ComplexValue ONE_VALUE = new ComplexValue(Complex.ONE);

	public static final ComplexValue MINUS_ONE_VALUE = new ComplexValue(Complex.MINUS_ONE);

	public static final ComplexValue UNIT_IM_VALUE = new ComplexValue(Complex.UNIT_IM);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Default constructor - real and imaginary pats are both initialized to zero.
	 */
	public ComplexValue()
	{
		super();
	}

	/**
	 * constructor - initialises both the real and imaginary parts.
	 * 
	 * @param real
	 *            real part
	 * @param imag
	 *            imaginary part
	 */
	public ComplexValue(double real, double imag)
	{
		super(real, imag);
	}

	/**
	 * Constructor - initialises real, imag = 0.0.
	 * 
	 * @param real
	 *            real part
	 */
	public ComplexValue(double real)
	{
		super(real);
	}

	/**
	 * Constructor - initialises both real and imag to the values of an existing Complex
	 * 
	 * @param c
	 */
	public ComplexValue(Complex c)
	{
		super(c);
	}

	/**
	 * Constructor - initialises real part; imag = 0.0.
	 * 
	 * @param c
	 */
	public ComplexValue(RealValue c)
	{
		super(c.getValue());
	}

	/**
	 * Convert a string into a complex value.
	 * 
	 * @param ss
	 *            string to parse
	 * @return complex value
	 * @throws NumberFormatException
	 *             if parsing fails
	 */
	public static ComplexValue parseComplex(String ss) throws NumberFormatException
	{
		Complex temp = Complex.parseComplex(ss);
		return new ComplexValue(temp);
	}

	// ========================= IMPLEMENTATION: PubliclyCloneable =========

	/**
	 * Return a shallow copy of this object. Must be implemented for this object to serve
	 * as a target of an assembly.
	 * 
	 * @return a shallow copy of this object.
	 */
	@Override
	public ComplexValue clone()
	{
		ComplexValue copy = (ComplexValue) super.clone();
		return copy;
	}

	// ========================= IMPLEMENTATION: Comparable ===============

	/**
	 * Result of comparing two complex numbers (lexicographic ordering by real part, then
	 * by imaginary part).
	 * 
	 * @param other
	 *            the other <code>Complex</code>
	 * @return the result of comparison
	 */
	public int compareTo(NumericalValue obj)
	{
		// TODO: replace with Double comparison
		ComplexValue other = (ComplexValue) obj;
		int compareRe = Double.compare(this.re(), other.re());
		int compareIm = Double.compare(this.im(), other.im());

		// If real parts are not tied, return result of real part comparison
		if (compareRe != 0)
		{
			return compareRe;
		}

		// otherwise, return result of imaginary part comparison
		return compareIm;
	}

	/**
	 * Result of equality of two complex values. They are equal if and only if their
	 * <code>re,im</code> fields are equal up according to the {@link Double} class
	 * equals() implementation.
	 * 
	 * @param obj
	 *            The other <code>Complex</code> object.
	 * @return boolean The result of equality.
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
		ComplexValue other = (ComplexValue) obj;

		return (this.compareTo(other) == 0);
	}

	// ========================= IMPLEMENTATION: TolerantlyComparable ========

	/**
	 * Result of equality of two complex numbers up to a finite tolerance. They are equal
	 * if and only if their <code>re, im</code> parts are both equal up to a relative
	 * tolerance of tol. This is an implementation of the
	 * <code>TolerantlyComparable</code> interface.
	 * 
	 * @param obj
	 *            The other <code>ComplexValue</code> object.
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
		// logger.trace("ComplexValue tolerantlyEquals(): this.class " +
		// getClass() + " obj.class " + obj.getClass());
		if (this == obj)
		{
			return TolerantlyComparable.EQUAL;
		}
		// The following clause is usually to be avoided, but doesn't violate
		// the symmetry principle because this method is declared final. On
		// the other hand, we do need sub-classes to be compared with
		// ComplexValue.
		if ((obj == null) || (!(obj instanceof ComplexValue)))
		{
			return TolerantlyComparable.NOT_EQUAL;
		}
		// Cast to friendlier version
		ComplexValue other = (ComplexValue) obj;

		// Check for infinities, NaNs
		boolean equalsWhenNanResult = equalsWhenNan(other);
		if (!equalsWhenNanResult)
		{
			// If it's infinity vs. -infinity, infinity vs. nan
			// or nan vs. -infinity, they're different (compare
			// re part vs. re part, im part vs. im part).
			return TolerantlyComparable.NOT_EQUAL_WHEN_NAN;
		}

		// logger.debug("equalsWhenNan() " + equalsWhenNan(other));
		// logger.debug("Infinite/NaN? "
		// + (isInfinite() || isNaN() || other.isInfinite() || other.isNaN()));

		// Can't decide on infinity vs. infinity, -infinity vs.
		// -infinity, nan vs. nan ==> bad sample, skip.
		if (isInfinite() || isNaN() || other.isInfinite() || other.isNaN())
		{
			return TolerantlyComparable.INDETERMINATE;
		}

		// Just like RealUtil.doubleEquals()
		double distance = this.minus(other).abs();
		double minAbs = Math.min(abs(), other.abs());

		double relativeError = (minAbs <= 10 * RealUtil.MACHINE_DOUBLE_ERROR) ? distance
				: (distance / minAbs);
		// logger.debug("ComplexValue tolerantlyEquals(): this " + this + "
		// other " + other
		// + " relativeError " + relativeError + " isZero "
		// + (relativeError <= RealConstants.SQRT2 * tol));

		// Allow slightly more slack than reals. Example: a complex -> complex
		// operation whose exact result is (x,y) but results in (x+u,y+u) where
		// u is machine round-off. According to this function, the complex error
		// is sqrt(2)*u. It is true that for (x,y) vs. (x+u,y) this is totally
		// out of wack, but I'm leaving it at that for now.
		return (relativeError <= RealConstants.SQRT2 * tol) ? TolerantlyComparable.EQUAL
				: TolerantlyComparable.NOT_EQUAL;
	}

	// ========================= IMPLEMENTATION: Visitable<Visitor<MathValue>>

	/**
	 * Let a visitor process this item. Part of the TreeVisitor pattern. This calls back
	 * the visitor's <code>visit()</code> method with this item type. Must be overridden
	 * by every item sub-class.
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

	/**
	 * Returns the HTML representation of this object.
	 * 
	 * @return the HTML representation of this object.
	 */
	@Deprecated
	public String toHTML()
	{
		return toString();
	}

	// ========================= IMPLEMENTATION: Value ====================

	/**
	 * @see net.ruready.parser.arithmetic.entity.mathvalue.ArithmeticValue#simpleName()
	 */
	public String simpleName()
	{
		return "Complex";
	}

	/**
	 * Corresponding arithmetic mode to this type of numerical value.
	 * 
	 * @return corresponding arithmetic mode
	 */
	public ArithmeticMode getArithmeticMode()
	{
		return ArithmeticMode.COMPLEX;
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
		return new ComplexValue(d);
	}

	/**
	 * Return false if one number is infinite and the other is not, or one number is NaNF
	 * and the other one is not. Otherwise return true.
	 * 
	 * @param obj
	 *            long value to compare with.
	 * @return the result of "equality"
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
		if ((obj == null) || (!(obj instanceof ComplexValue)))
		{
			return false;
		}
		// Cast to friendlier version
		ComplexValue other = (ComplexValue) obj;

		// logger.debug("equalsWhenNan(): this " + this + " other " + other + " re: "
		// + new RealValue(re()).equalsWhenNan(new RealValue(other.re())) + " im: "
		// + new RealValue(im()).equalsWhenNan(new RealValue(other.im())));

		if (!new RealValue(re()).equalsWhenNan(new RealValue(other.re())))
		{
			return false;
		}
		if (!new RealValue(im()).equalsWhenNan(new RealValue(other.im())))
		{
			return false;
		}
		return true;
	}

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
		Complex temp = (Complex.parseComplex(ss));
		this.setRe(temp.re());
		this.setIm(temp.im());
	}

	/**
	 * ======================================================== UNARY ARITHMETIC
	 * OPERATIONS (op(this)) Names are upper-case to resolve possible name conflicts with
	 * inherited methods with a different return type.
	 * ========================================================
	 */
	public NumericalValue EXP()
	{
		return new ComplexValue(exp());
	}

	public NumericalValue SQRT()
	{
		return new ComplexValue(sqrt());
	}

	public NumericalValue CBRT()
	{
		return new ComplexValue(pow(new ComplexValue(RealConstants.THIRD)));
	}

	public NumericalValue LN()
	{
		return new ComplexValue(log());
	}

	public NumericalValue LOG10()
	{
		return new ComplexValue(log10());
	}

	public NumericalValue SIN()
	{
		return new ComplexValue(sin());
	}

	public NumericalValue COS()
	{
		return new ComplexValue(cos());
	}

	public NumericalValue TAN()
	{
		return new ComplexValue(tan());
	}

	public NumericalValue ASIN()
	{
		return new ComplexValue(asin());
	}

	public NumericalValue ACOS()
	{
		return new ComplexValue(acos());
	}

	public NumericalValue ATAN()
	{
		return new ComplexValue(atan());
	}

	public NumericalValue CSC()
	{
		return new ComplexValue(cosec());
	}

	public NumericalValue SEC()
	{
		return new ComplexValue(sec());
	}

	public NumericalValue COT()
	{
		return new ComplexValue(cot());
	}

	public NumericalValue SINH()
	{
		return new ComplexValue(sinh());
	}

	public NumericalValue COSH()
	{
		return new ComplexValue(cosh());
	}

	public NumericalValue TANH()
	{
		return new ComplexValue(tanh());
	}

	public NumericalValue ASINH()
	{
		return new ComplexValue(asinh());
	}

	public NumericalValue ACOSH()
	{
		return new ComplexValue(acosh());
	}

	public NumericalValue ATANH()
	{
		return new ComplexValue(atanh());
	}

	public NumericalValue ABS()
	{
		return new ComplexValue(abs());
	}

	public NumericalValue FACT()
	{
		return new ComplexValue(fact());
	}

	public NumericalValue NEGATE()
	{
		return new ComplexValue(negate());
	}

	public NumericalValue RECIP()
	{
		return new ComplexValue(inverse());
	}

	public NumericalValue SGN()
	{
		return new ComplexValue(Math.signum(re()));
	}

	public NumericalValue FLOOR()
	{
		return new ComplexValue(Math.floor(re()), Math.floor(im()));
	}

	public NumericalValue CEIL()
	{
		return new ComplexValue(Math.ceil(re()), Math.ceil(im()));
	}

	/**
	 * ======================================================== BINARY ARITHMETIC
	 * OPERATIONS (this op y) Names are upper-case to resolve possible name conflicts with
	 * inherited methods with a different return type. These functions assume that y is an
	 * instance of Real. ========================================================
	 */
	public NumericalValue PLUS(NumericalValue y)
	{
		return new ComplexValue(plus((Complex) y));
	}

	public NumericalValue MINUS(NumericalValue y)
	{
		return new ComplexValue(minus((Complex) y));
	}

	public NumericalValue TIMES(NumericalValue y)
	{
		return new ComplexValue(times((Complex) y));
	}

	public NumericalValue OVER(NumericalValue y)
	{
		return new ComplexValue(over((Complex) y));
	}

	public NumericalValue POWER(NumericalValue y)
	{
		return new ComplexValue(pow((Complex) y));
	}

	public NumericalValue MOD(NumericalValue y)
	{
		return new ComplexValue(mod((Complex) y));
	}

	public NumericalValue LOG(NumericalValue y)
	{
		return new ComplexValue(log(this, (Complex) y));
	}

	public NumericalValue ROOT(NumericalValue y)
	{
		return new ComplexValue(root((Complex) y));
	}

	public void PLUS_EQUAL(NumericalValue y)
	{
		Complex result = this.plus((ComplexValue) y);
		this.setReIm(result.getRe(), result.getIm());
	}

	public void MINUS_EQUAL(NumericalValue y)
	{
		Complex result = this.minus((ComplexValue) y);
		this.setReIm(result.getRe(), result.getIm());
	}

	public void TIMES_EQUAL(NumericalValue y)
	{
		Complex result = this.times((ComplexValue) y);
		this.setReIm(result.getRe(), result.getIm());
	}

	public void OVER_EQUAL(NumericalValue y)
	{
		Complex result = this.over((ComplexValue) y);
		this.setReIm(result.getRe(), result.getIm());
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
		return abs();
	}

	// ========================= METHODS ===================================

	/**
	 * LOG OF GAMMA FUNCTION Based on power series approximation. WARNING: not clear
	 * whether it works for non-real numbers. Given here for completeness only.
	 */
	public static Complex logGamma(Complex c)
	{
		int j;
		Complex x, y, tmp;
		double[] cof =
		{
				76.18009172947146, -86.50532032941677, 24.01409824083091,
				-1.231739572450155, 0.1208650973866179e-2, -0.5395239384953e-5
		};
		y = x = c;
		Complex z = x.plus(5.5);
		tmp = z.minus((x.plus(0.5)).times(z.log()));
		Complex ser = new Complex(1.000000000190015);
		for (j = 0; j <= 5; j++)
		{
			y = y.plus(ONE);
			ser = ser.plus(new Complex(cof[j]).over(y));
		}
		return ser.times(new Complex(2.5066282746310005)).over(x).log().minus(tmp);
	}

	/**
	 * GAMMA FUNCTION Based on power series approximation. WARNING: not clear whether it
	 * works for non-real numbers. Given here for completeness only.
	 */
	public static Complex gamma(Complex c)
	{
		return logGamma(c).exp();
	}

	/**
	 * A factorial function. Its computation based on power series approximation. WARNING:
	 * not clear whether it works for non-real numbers. Provided here for completeness
	 * only.
	 */
	public Complex fact()
	{
		return gamma(plus(ONE));
	}
}
