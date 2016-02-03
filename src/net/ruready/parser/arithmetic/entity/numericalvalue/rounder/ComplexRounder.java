/*******************************************************************************
 * Source File: ComplexFormat.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.rounder;

import net.ruready.common.math.real.RealUtil;
import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue;
import net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstantValue;
import net.ruready.parser.math.entity.visitor.DefaultMathValueVisitor;

/**
 * Rounds complex-valued numbers to a certain relative tolerance.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112-9359<br>
 * U.S.A.<br>
 * Day Phone: 1-801-587-5835, Fax: 1-801-585-5414<br>
 * <br>
 * Please contact these numbers immediately if you receive this file without permission
 * from the authors. Thank you.<br>
 *         <br>(c) 2006-07 Continuing Education , University of Utah . All
 *         copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 27, 2007
 */
class ComplexRounder extends DefaultMathValueVisitor implements
		NumericalRounder<ComplexValue>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * Tolerance of rounding.
	 */
	private double tolerance;

	/**
	 * The rounded value.
	 */
	private ComplexValue result;

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: NumericalFormat ===========

	/**
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.format.NumericalFormat#format(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue,
	 *      int)
	 */
	public ComplexValue round(final ComplexValue value, final double tol)
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
		return ArithmeticMode.COMPLEX;
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
	 * @see net.ruready.parser.math.entity.visitor.DefaultMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.constant.ComplexConstant)
	 */
	@Override
	public void visit(ComplexConstantValue value)
	{
		visit((ComplexValue) value);
		result = new ComplexConstantValue(value.getConstant(), result);
	}

	/**
	 * @see net.ruready.parser.math.entity.visitor.DefaultMathValueVisitor#visit(net.ruready.parser.arithmetic.entity.numericalvalue.ComplexValue)
	 */
	@Override
	public void visit(ComplexValue value)
	{
		double roundedRe = RealUtil.round(value.re(), tolerance);
		double roundedIm = RealUtil.round(value.im(), tolerance);
		result = new ComplexValue(roundedRe, roundedIm);
	}

	// ========================= PRIVATE METHODS ===========================
}
