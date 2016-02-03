/*****************************************************************************************
 * Source File: AppendWordAssembler.java
 ****************************************************************************************/
package net.ruready.parser.param.assembler;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.param.entity.ParametricEvaluationTarget;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Append a word to the parametric string (no evaluation required).
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
class AppendWordAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(AppendWordAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Replace the top token in the stack with the token's Double value. Add the number to
	 * the target's lists of expression parts.
	 * 
	 * @param Assembly
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// Pop a token from the assembly
		Token t = (Token) a.pop();
		String str = new String(t.sval());

		// Get required info for math parsing from
		// ParamEvalParser's target
		ParametricEvaluationTarget target = (ParametricEvaluationTarget) a.getTarget();

		// Add string to target's evalString
		target.appendToEvalString(str);
	}

}
