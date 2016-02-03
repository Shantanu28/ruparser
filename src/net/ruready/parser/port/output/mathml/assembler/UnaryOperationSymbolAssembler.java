/*****************************************************************************************
 * Source File: UnaryOpSymbolAssembler.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.assembler;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.util.EnumUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.UnaryOperation;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.tokenizer.entity.MathAssembly;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pop a token from the stack and push a unary operation symbol token.
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
class UnaryOperationSymbolAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger =
			LogFactory.getLog(UnaryOperationSymbolAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Push operation info (especially: index in assembly) onto the target's operations
	 * stack
	 * 
	 * @param Assembly
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);
		Token t = (Token) a.pop();
		if (t.isWord())
		{
			t = new Token(new String(t.sval()).toLowerCase());
		}
		UnaryOperation unaryOp =
				EnumUtil.createFromString(UnaryOperation.class, t.sval());
		// logger.debug("Popped t " + t + " isWord? " + t.isWord() + " unaryOp "
		// + unaryOp);
		a.push(new MathToken(((MathAssembly) a).elementsConsumedAll() - 1, unaryOp));
		// logger.debug("end, a=" + a);
	}

}
