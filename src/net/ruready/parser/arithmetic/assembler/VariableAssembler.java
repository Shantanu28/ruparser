/*****************************************************************************************
 * Source File: VariableAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.arithmetic.entity.mathvalue.Variable;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.math.entity.SyntaxTreeNode;
import net.ruready.parser.tokenizer.entity.MathAssembly;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This assembler processes a symbolic variable like "x" (the variable might be
 * numerically-assigned, but this is irrelevant here).
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
class VariableAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(VariableAssembler.class);

	// The implicit multiplication operation
	public static final BinaryOperation OP = BinaryOperation.TIMES;

	// ========================= FIELDS ====================================

	// String corresponding to this variable
	// private String variable;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Create a variable assembler.
	 */
	public VariableAssembler()
	{

	}

	/**
	 * Create a variable assembler.
	 * 
	 * @param variable
	 *            the string corresponding to this variable
	 */
	/*
	 * public VariableAssembler(String variable) { this.variable = variable; }
	 */

	// ========================= METHODS ===================================
	/**
	 * Replace the top token in the stack with the token's variable string. Add the token
	 * to the target's lists of expression parts.
	 * 
	 * @param Assembly
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);
		// ParametricEvaluationTarget target =
		// (ParametricEvaluationTarget)((MathTarget)
		// a.getTarget()).getFactory(ParserType.ARITHMETIC);

		// Pop the variable from assembly
		Token t = (Token) a.pop();
		String varName = t.sval();

		// Convert to lower-case
		if (t.isWord())
		{
			t = new Token(new String(varName).toLowerCase());
		}
		// Create a math token for this variable
		MathToken ta =
				new MathToken(((MathAssembly) a).elementsConsumedAll() - 1, new Variable(
						varName));

		// Push a variable's expression on the stack
		a.push(new SyntaxTreeNode(ta));

		// logger.debug("end, a=" + a);
	}
}
