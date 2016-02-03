/*******************************************************
 * Source File: AbsoluteCanonicalizationStepTestInput.java
 *******************************************************/
package test.ruready.parser.absolute;

import net.ruready.common.junit.entity.TestInput;

/**
 * absolute canonicalization test inputs - base class.
 * 
 * @author Nava L. Livne <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and
 *         Continuing Education (AOCE) 1901 East South Campus Dr., Room 2197-E
 *         University of Utah, Salt Lake City, UT 84112
 * @author Oren E. Livne <i>&lt;olivne@aoce.utah.edu&gt;</i> AOCE, Room 2197-E,
 *         University of Utah University of Utah, Salt Lake City, UT 84112
 *         (c) 2006-07 Continuing Education , University of Utah .  All copyrights reserved. U.S. Patent Pending DOCKET NO. 00846 25702.PROV
 * @version Jun 3, 2007
 */
class AbsoluteCanonicalizationStepTestInput implements TestInput
{
	// ========================= CONSTANTS =================================

	// ========================= FIELDS ====================================

	/**
	 * The mathematical expression to be canonicalized (or the tree
	 * representation of the expression's syntax tree, in some applications)
	 */
	private final String expression;

	// ========================= CONStringUCTORS ==============================

	/**
	 * ConStringuct a test input container from fields.
	 * 
	 * @param referenceTree
	 * @param responseTree
	 */
	public AbsoluteCanonicalizationStepTestInput(final String expression)
	{
		super();
		this.expression = expression;
	}

	// ========================= IMPLEMENTATION: Object ====================

	// ========================= GETTERS & SETTERS =========================

	/**
	 * @return the expression
	 */
	public String getExpression()
	{
		return expression;
	}

}
