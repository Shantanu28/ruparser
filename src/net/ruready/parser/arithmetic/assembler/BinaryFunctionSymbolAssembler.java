/*****************************************************************************************
 * Source File: BinaryFuncSymbolAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.util.EnumUtil;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryFunction;
import net.ruready.parser.math.entity.MathToken;
import net.ruready.parser.tokenizer.entity.MathAssembly;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An assembler for the symbol of an binary arithmetic function (e.g. "root").
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
class BinaryFunctionSymbolAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger =
			LogFactory.getLog(BinaryFunctionSymbolAssembler.class);

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
		Token t = (Token) a.pop();
		if (t.isWord())
		{
			t = new Token(new String(t.sval()).toLowerCase());
		}
		BinaryFunction binaryFunc =
				EnumUtil.createFromString(BinaryFunction.class, t.sval());
		a.push(new MathToken(((MathAssembly) a).elementsConsumedAll() - 1, binaryFunc));
		// logger.debug("end, a="+a);
	}

}
