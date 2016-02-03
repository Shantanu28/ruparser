/*****************************************************************************************
 * Source File: ImpMultModeAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.common.rl.CommonNames;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Disable implicit multiplication in the target for this arithmetic parser's expression
 * evaluation.
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
class ImpMultModeAssembler extends Assembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ImpMultModeAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Pop a symbol (top token in the stack) and set the target's imp mult flag to false.
	 * 
	 * @param a
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// Pop a token from the assembly ("*")
		Token t = (Token) a.pop();

		MathTarget target = (MathTarget) a.getTarget();

		// Update target imp mult control
		// logger.debug("t.sval = '" + t.sval() + "'");
		if ((CommonNames.MISC.EMPTY_STRING + ParserNames.ARITHMETIC_COMPILER.MULT_NOT_REQUIRED)
				.equals(t.sval()))
		{
			// Found the negation symbol, Turn imp mult on
			target.setImplicitMultiplication(true);
		}
		else
		{
			// Found the "do" symbol, turn imp mult off
			target.setImplicitMultiplication(false);
		}

		logger.debug("Target imp mult mode set to " + target.isImplicitMultiplication());
	}

}
