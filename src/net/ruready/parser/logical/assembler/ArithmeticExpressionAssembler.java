/*****************************************************************************************
 * Source File: ExpressionEvaluationAssembler.java
 ****************************************************************************************/
package net.ruready.parser.logical.assembler;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.SyntaxTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An assembler for post-processing, at the end of an arithmetic expression matching. Adds
 * a fictitious root node to the expression's syntax tree for convenience during logical
 * parser super-processing.
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
 * @version Sep 8, 2007
 */
class ArithmeticExpressionAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(ArithmeticExpressionAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Post-processing of an arithmetic expression target object. Rounds the results,
	 * performs analysis on the tokens, etc.
	 * 
	 * @param a
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		MathTarget target = (MathTarget) a.getTarget();

		// =====================================================================
		// Final syntax tree is not on the stack; it's already in the target
		// =====================================================================
		SyntaxTreeNode arithmeticExpression = target.getSyntax();
		// SyntaxTreeNode tree = (SyntaxTreeNode) a.pop();
		// target.setSyntax(tree);
		// logger.debug("Popped tree " + tree);

		// =====================================================================
		// Add a root node to indicate that this is an arithmetic expression.
		// Merely a convenience for the logical parser.
		// 5-JUL-07: turning this off. Using a visitor pattern in logical parser
		// to determine which nodes are arithmetic expressions.
		// =====================================================================
		// ArithmeticValue rootValue = new ArithmeticLiteralValue(
		// ParserNames.RESERVED_WORDS.ARITHMETIC_EXPRESSION);
		// MathToken newThisData = new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER,
		// rootValue,
		// MathTokenStatus.FICTITIOUS_CORRECT);
		// SyntaxTreeNode rootNode = new SyntaxTreeNode(newThisData);
		//
		// SyntaxTreeUtil.replaceParentNode(target, tree, rootNode);

		// Push the expression's syntax tree back onto the stack so that the
		// logical super-parser can process it.
		a.push(arithmeticExpression);
	}

}
