/*******************************************************************************
 * Source File: IntegerFormat.java
 ******************************************************************************/
package net.ruready.parser.arithmetic.entity.numericalvalue.format;

import net.ruready.parser.arithmetic.entity.numericalvalue.ArithmeticMode;
import net.ruready.parser.arithmetic.entity.numericalvalue.IntegerValue;

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
class IntegerFormat implements NumericalFormat<IntegerValue>
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= IMPLEMENTATION: NumericalFormat ===========

	/**
	 * @see net.ruready.parser.arithmetic.entity.numericalvalue.format.NumericalFormat#format(net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue)
	 */
	public String format(final IntegerValue value)
	{
		return value.toString();
	}

	// ========================= IMPLEMENTATION: Identifiable ==============

	/**
	 * @see net.ruready.common.discrete.Identifiable#getIdentifier()
	 */
	public ArithmeticMode getIdentifier()
	{
		return ArithmeticMode.INTEGER;
	}

	/**
	 * @see net.ruready.common.discrete.Identifier#getType()
	 */
	public String getType()
	{
		return getIdentifier().getType();
	}
}
