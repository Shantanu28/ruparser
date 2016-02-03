/*****************************************************************************************
 * Source File: MultinaryOpAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.MultinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.tokenizer.entity.MathAssembly;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop all the math tokens corresponding to a multi-nary operation from the assembly's
 * stack, and build a corresponding syntax tree.
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
class MultinaryOperationAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger =
			LogFactory.getLog(MultinaryOperationAssembler.class);

	// ========================= FIELDS ====================================

	// Fence token
	private final Token fence;

	// The multinary op
	private final MultinaryOperation multinaryOp;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a multi-nary operation assembler.
	 * 
	 * @param fence
	 *            fence token
	 * @param multinaryOp
	 *            multi-nary operation
	 */
	public MultinaryOperationAssembler(final Token fence,
			final MultinaryOperation multinaryOp)
	{
		super();
		this.fence = fence;
		this.multinaryOp = multinaryOp;
	}

	// ========================= IMPLEMENTATION: Assembler =================

	/**
	 * @see net.ruready.parser.core.parse.Assembler#workOn(net.ruready.parser.core.parse.Assembly)
	 */
	@Override
	public void workOn(Assembly a)
	{
		logger.debug("[" + fence + "] " + "start, a=" + a);

		// Pop all terms above the fence
		List<?> allTerms = Assembler.elementsAbove(a, fence);

		logger.debug("[" + fence + "] " + "allTerms " + allTerms);
		// if (!allTerms.isEmpty()) {
		// // Process only if found a multi-nary operation; for a single
		// // term, do nothing
		// // Process multi-nary operation
		if (allTerms.isEmpty())
		{
			// Process only if found a multi-nary operation; for a single
			// term, do nothing
			// Process multi-nary operation
			SyntaxTreeNode e = (SyntaxTreeNode) a.pop();
			a.push(e);
		}
		else
		{
			List<MathToken> unaryMathTokens = new ArrayList<MathToken>();
			List<SyntaxTreeNode> terms = new ArrayList<SyntaxTreeNode>();
			Iterator<?> iterator = allTerms.iterator();
			while (iterator.hasNext())
			{
				// Read terms and add them in reverse order to operand lists
				SyntaxTreeNode term = (SyntaxTreeNode) iterator.next();
				MathToken binaryOpToken = (MathToken) iterator.next();
				BinaryOperation binaryOp = (BinaryOperation) binaryOpToken.getValue();
				// logger.debug("binaryOp " + binaryOpToken.getValue());
				// Replace the binary operation placed on the assembly by
				// other assembler with the corresponding unary op token
				MathToken unaryOpToken =
						new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER, binaryOp
								.unaryOpForm());
				unaryMathTokens.add(0, unaryOpToken);
				terms.add(0, term);
			}

			// Pop the first term from stack
			SyntaxTreeNode firstTerm = (SyntaxTreeNode) a.pop();
			UnaryOperation op = multinaryOp.unaryOpForm();
			MathToken firstOpToken =
					new MathToken(CommonNames.MISC.INVALID_VALUE_INTEGER, op);
			unaryMathTokens.add(0, firstOpToken);
			terms.add(0, firstTerm);

			// Create a syntax tree for the multi-nary op
			// TODO: what should the multi-nary operation's assembly index be? Is
			// it fictitous? (probably)
			MathToken multinaryOpToken =
					new MathToken(((MathAssembly) a).elementsConsumedAll() - 1,
							multinaryOp);

			// logger.debug("firstTerm " + firstTerm);
			// logger.debug("multinaryOpToken " + multinaryOpToken);
			// logger.debug("terms " + terms);
			// logger.debug("unaryMathTokens " + unaryMathTokens);

			SyntaxTreeNode e =
					SyntaxTreeNode.op(multinaryOpToken, unaryMathTokens, terms);
			a.push(e);
		}

		// Push the resulting expression on the stack

		logger.debug("[" + fence + "] " + "end, a=" + a);
	}

	// ========================= METHODS ===================================
}
