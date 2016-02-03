/*****************************************************************************************
 * Source File: BinaryValue.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.entity.mathvalue;

import net.ruready.parser.arithmetic.entity.numericalvalue.NumericalValue;

/**
 * Binary operation or function. A Convenient unification of concepts for matching,
 * parsing and evaluating mathematical expressions.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing
 *         Education (AOCE) 1901 East South Campus Dr., Room 2197-E University of Utah,
 *         Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E, University of
 *         Utah University of Utah, Salt Lake City, UT 84112 (c) 2006-07 Continuing
 *         Education , University of Utah . All copyrights reserved. U.S. Patent Pending
 *         DOCKET NO. 00846 25702.PROV
 * @version 24/02/2006
 */
public interface BinaryValue extends ArithmeticValue
{
	// ========================= CONSTANTS =================================

	// Number of operands of a binary operation
	public static final int NUM_OPERANDS = 2;

	// Left/first operand child node index of a binary operation in a syntax tree
	public static final int LEFT = 0;

	// Right/second operand child node index of a binary operation in a syntax tree
	public static final int RIGHT = 1;

	// ========================= ABSTRACT METHODS ==========================

	/**
	 * Perform an arithmetic operation on two numbers (x op y or op(x,y)).
	 * 
	 * @param x
	 *            the left operand.
	 * @param y
	 *            the right operand.
	 * @return result of binary operation.
	 */
	NumericalValue eval(NumericalValue x, NumericalValue y);
}
