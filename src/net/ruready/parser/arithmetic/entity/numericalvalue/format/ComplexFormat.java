/*****************************************************************************************
 * Source File: ComplexFormat.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.format;

import net.ruready.common.math.basic.TolerantlyComparable;
import net.ruready.common.math.complex.Complex;
import net.ruready.common.math.real.RealConstants;
import net.ruready.common.math.real.RealUtil;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;

/**
 * @author Nava L. Livne <I>&lt;nlivne@aoce.utah.edu&gt;</I> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <I>&lt;olivne@aoce.utah.edu&gt;</I> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112 Protected by U.S. Provisional Patent U-4003, February
 *         2006
 * @version Jul 3, 2006 Allows manipulating formatting of real number printouts.
 */
class ComplexFormat implements NumericalFormat<ComplexValue>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: NumericalFormat ===========

	/**
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.format.NumericalFormat#format(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public String format(final ComplexValue value)
	{
		// Used to point to a print function in Complex, now
		// code is here
		// return CommonNames.MISC.EMPTY_STRING+c.toString(tol);

		// Compute imaginary part sign
		char ch = (value.im() < 0.0D) ? '-' : '+';
		// See comment in formatReal()
		RealValue Re = new RealValue(value.re());
		RealValue Im = new RealValue(value.im());
		RealValue AbsIm = new RealValue(Math.abs(value.im()));
		RealFormat f = new RealFormat();
		String reStr = f.format(Re);
		String imStr = f.format(Im);
		String absImStr = f.format(AbsIm);
		final double tol = RealUtil.MACHINE_DOUBLE_ERROR;
		if ((Im.tolerantlyEquals(new RealValue(RealConstants.ONE), tol) == TolerantlyComparable.EQUAL)
				|| (Im.tolerantlyEquals(new RealValue(RealConstants.MINUS_ONE), tol) == TolerantlyComparable.EQUAL))
		{
			imStr = CommonNames.MISC.EMPTY_STRING;
		}
		if (Im.tolerantlyEquals(new RealValue(RealConstants.ZERO), tol) == TolerantlyComparable.EQUAL)
		{
			return CommonNames.MISC.EMPTY_STRING + reStr;
		}
		if (Re.tolerantlyEquals(new RealValue(RealConstants.ZERO), tol) == TolerantlyComparable.EQUAL)
		{
			return imStr + Complex.getUNIT_IM_SYMBOL();
		}
		return reStr + ch + absImStr + Complex.getUNIT_IM_SYMBOL();
	}

	// ========================= IMPLEMENTATION: Identifiable ==============

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public ArithmeticMode getIdentifier()
	{
		return ArithmeticMode.COMPLEX;
	}

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return getIdentifier().getType();
	}

	// ========================= PRIVATE METHODS ===========================

	// ========================= PRIVATE METHODS ===========================
}
