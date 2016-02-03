/*******************************************************************************
 * Source File: RationalFormat.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.format;

import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.RationalValue;

/**
 * @author Nava L. Livne <I>&lt;nlivne@aoce.utah.edu&gt;</I> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <I>&lt;olivne@aoce.utah.edu&gt;</I> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112 Protected by U.S.
 *         Provisional Patent U-4003, February 2006
 * @version Jul 3, 2006 Allows manipulating formatting of rational number
 *          printouts.
 */
class RationalFormat implements NumericalFormat<RationalValue>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: NumericalFormat ===========

	/**
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.format.NumericalFormat#format(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public String format(final RationalValue value)
	{
		return value.toString();
	}

	// ========================= IMPLEMENTATION: Identifiable ==============

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public ArithmeticMode getIdentifier()
	{
		return ArithmeticMode.RATIONAL;
	}

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return getIdentifier().getType();
	}
}
