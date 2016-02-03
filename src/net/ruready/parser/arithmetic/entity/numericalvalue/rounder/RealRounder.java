/*******************************************************************************
 * Source File: RealFormat.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.rounder;

import net.ruready.common.math.real.RealUtil;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.RealValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue;
import net.ruready.parser.math.entity.visitor.DefaultMathValueVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Rounds real-valued numbers to a certain relative tolerance.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach
 *         and Continuing Education (AOCE) 1901 East South Campus Dr., Room
 *         2197-E University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112-9359<br>
 *         U.S.A.<br>
 *         Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 *         <br>
 *         Please contact these numbers immediately if you receive this file
 *         without permission from the authors. Thank you.<br>
 *         <br>
 *         (c) 2006-07 Continuing Education , University of Utah . All
 *         copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 27, 2007
 */
class RealRounder extends DefaultMathValueVisitor implements
		NumericalRounder<RealValue>
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RealRounder.class);

	// ========================= FIELDS ====================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: NumericalFormat ===========

	// ========================= FIELDS ====================================

	/**
	 * Tolerance of rounding.
	 */
	private double tolerance;

	/**
	 * The rounded value.
	 */
	private RealValue result;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: NumericalFormat ===========

	/**
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.format.NumericalFormat#format(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue,
	 *      int)
	 */
	public RealValue round(final RealValue value, final double tol)
	{
		this.tolerance = tol;
		this.visit(value);
		return result;
	}

	// ========================= IMPLEMENTATION: Identifiable ==============

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public ArithmeticMode getIdentifier()
	{
		return ArithmeticMode.REAL;
	}

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return getIdentifier().getType();
	}

	// ========================= IMPLEMENTATION: DefaultMathValueVisitor ===

	/**
	 * @see net.ruready.parser.math.entity.visitor.DefaultMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.RealConstantValue)
	 */
	@Override
	public void visit(RealConstantValue value)
	{
		visit((RealValue) value);
		result = new RealConstantValue(value.getConstant(), result);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.DefaultMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.RealValue)
	 */
	@Override
	public void visit(RealValue value)
	{
		result = new RealValue(RealUtil.round(value.getValue(), tolerance));
	}

	// ========================= PRIVATE METHODS ===========================

	/**
	 * Used to a be a relative tolerance for printouts. We truncate instead to
	 * (-log(10))-digits after the decimal dot using "printf" formatting.
	 * 
	 * @param r
	 *            real valued number
	 * @param tol
	 *            tolerance of rounding
	 * @param rationalRound
	 *            round to rational (tolerance=tol) or not.
	 * @return r printed to tol-precision
	 */
	private static String formatReal(final RealValue r, final double tol,
			final boolean rationalRound)
	{
		// Make sure that r is printed as an integer
		// if it is [close to] one.
		long rInt = java.lang.Math.round(r.getValue());
		// Seems like "u" is too strict. So have 10*u
		// as the threshold.
		if (RealUtil.doubleEquals(1.0 * rInt, r.getValue(), tol))
		{
			return CommonNames.MISC.EMPTY_STRING + rInt;
		}
		// Use ceil instead here?! Using +1 to cover the
		// required precision and a little bit more. Check
		// this again sometime.
		final int precision = (r.getValue() == 0) ? 0 : Math.max(0,
				(int) (1 - java.lang.Math.ceil(java.lang.Math.log10(tol
						* Math.abs(r.getValue())))));
		// logger.debug("precision "+precision+" r "+r+" tol "+tol);
		final String format = "%." + precision + "f";
		// This will cause rounding to the nearest rational,
		// and that's not always the desired output.
		// RealValue rounded = r;
		// if (rationalRound) {
		// rounded = new RealValue(r.getValue(), tol);
		// }

		Object[] temp = new Object[1];
		temp[0] = r.getValue();
		String raw = String.format(format, temp); // CommonNames.MISC.EMPTY_STRING+rounded;

		// Remove trailing "0"s like in r=2.1, format=%.5f
		// ==> raw = 2.10000
		int dot = raw.indexOf(".");
		int end = raw.length();
		if ((dot >= 0) && (end >= dot + 1))
		{
			while (raw.charAt(end - 1) == '0')
			{
				end--;
			}
			raw = raw.substring(0, end);
		}

		// If nothing after the decimal dot, that's an integer, get rid of the
		// dot
		dot = raw.indexOf(".");
		end = raw.length();
		if (dot == end - 1)
		{
			raw = raw.substring(0, end - 1);
		}

		/*
		 * logger.debug("r "+r); logger.debug("precision "+precision);
		 * logger.debug("format "+format); logger.debug("temp "+temp);
		 * logger.debug("raw "+raw);
		 */
		/*
		 * int dot = raw.indexOf("."); if ((dot >= 0) && (dot + precision <
		 * raw.length()-1)) { // Too many digits were printed after the decimal
		 * point, // truncate format //return String.format(format, temp);
		 * return raw.substring(0, dot + precision + 1); } else { // OK, just
		 * print raw return raw; }
		 */
		return raw;
	}

	// ################## MAIN TESTING CALL #######################
	public static void main(String[] args)
	{
		Object[] temp = new Object[1];
		temp[0] = new Double(2.8200000000000003);
		String format = "%.3f";
		logger.debug(String.format(format, temp));

		temp[0] = new Double(9.00000000000003);
		format = "%.5f";
		logger.debug(String.format(format, temp));

		logger.debug(formatReal(new RealValue(2), 1e-5, false));
		logger.debug(formatReal(new RealValue(2.000000001), 1e-5, false));

		logger.debug(formatReal(new RealValue(2.1), 1e-5, false));
		logger.debug(formatReal(new RealValue(210), 1e-5, false));

		logger.debug(formatReal(new RealValue(1.0000), 1e-3, false));
		logger.debug(formatReal(new RealValue(1.0001), 1e-3, false));
	}
}
