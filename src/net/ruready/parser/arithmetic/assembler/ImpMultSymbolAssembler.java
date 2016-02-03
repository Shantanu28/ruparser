/*****************************************************************************************
 * Source File: ImpMultSymbolAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.rl.ParserNames;
import net.ruready.parser.tokenizer.entity.MathAssembly;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An assembler that processes an the implicit multiplication "*" symbol.
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
class ImpMultSymbolAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ImpMultSymbolAssembler.class);

	// The implicit multiplication operation
	public static final BinaryOperation OP = BinaryOperation.TIMES;

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Pop and operation token from the assembly and push a corresponding
	 * <code>MathToken</code> on [the target's operations] stack.
	 * 
	 * @param a
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);

		MathTarget target = (MathTarget) a.getTarget();
		if (!target.isImplicitMultiplication())
		{
			// Implicit multiplication was turned off, so this expression is
			// illegal.
			target.addSyntaxError(new InternationalizableErrorMessage(
					"Implicit multiplication is turned off",
					ParserNames.KEY.MATH_EXCEPTION.IMPLICIT_MULTIPLICATION));
		}

		BinaryOperation binaryOp = OP;
		// binaryOp.setImplicitMultiplication(target.isImplicitMultiplication());

		// Add the implicit multiplication token to the list of tokens as if it
		// were
		// typed. This seems like the easiest way to uniformly handle strings
		// with and without
		// implicit multiplication signs.
		((MathAssembly) a).incrementImplicitTokens();
		int index = ((MathAssembly) a).elementsConsumedAll() - 1;
		target.addImplicitToken(index, new Token(OP.toString()));

		a.push(new MathToken(index, binaryOp));
		// logger.debug("end, a=" + a);
	}
}
