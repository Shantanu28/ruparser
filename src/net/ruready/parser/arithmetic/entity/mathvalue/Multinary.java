/*******************************************************
 * Source File: Multinary.java
 *******************************************************/
package net.ruready.parser.arithmetic.entity.mathvalue;

import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

/**
 * A multi-nary operation (operation with an arbitary number of operands). There
 * must be at least two operands in the normal case.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Apr 20, 2007
 */
public interface Multinary extends ArithmeticValue
{
	// ========================= CONSTANTS =================================

	// Minimum number of operands of a multi-nary operation
	public static final int MIN_NUM_OPERANDS = 2;

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Perform an arithmetic operation on numbers (x1 op x2 op ... op xn).
	 * 
	 * @param x
	 *            operand array. Must be of length <code>2</code> or more.
	 * @return result of multi-nary operation.
	 */
	NumericalValue eval(NumericalValue[] x);
}
