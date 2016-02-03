/*****************************************************************************************
 * Source File: PrecisionAssembler.java
 ****************************************************************************************/
package net.ruready.parser.port.output.mathml.assembler;

import net.ruready.common.parser.core.assembler.Assembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.parser.core.tokens.Token;
import net.ruready.parser.arithmetic.entity.numericalvalue.format.NumericalFormatFactory;
import net.ruready.parser.port.output.mathml.entity.XmlStringTarget;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Set the non-sticky flag of # digits for this arithmetic parser's evaluation (implies a
 * precision tolerance 10^{-#digits}).
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
class PrecisionAssembler extends Assembler
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ModeAssembler.class);

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	// ========================= METHODS ===================================

	/**
	 * Pop a number (top token in the stack) and set the target's precision tolerance
	 * accordingly.
	 * 
	 * @param a
	 *            the assembly whose stack to use
	 */
	@Override
	public void workOn(Assembly a)
	{
		// logger.debug("start, a=" + a);
		XmlStringTarget target = (XmlStringTarget) a.getTarget();

		// Pop a token from the assembly, get a number.
		// Round it if it is not integer.
		Token t = (Token) a.pop();
		int digits = (int) Math.round(t.nval());

		// Input validation
		if (digits <= 0)
		{
			logger.warn("Control sequence #digits " + digits + " overrided to be "
					+ ParserNames.OPTIONS.MAX_DIGITS);
			digits = ParserNames.OPTIONS.MAX_DIGITS;
		}
		else if (digits > ParserNames.OPTIONS.MAX_DIGITS)
		{
			logger.warn("Control sequence #digits " + digits + " overrided to be "
					+ ParserNames.OPTIONS.MAX_DIGITS);
			digits = ParserNames.OPTIONS.MAX_DIGITS;
		}

		// Update target precision control for the target syntax tree
		target.setPrecisionTol(NumericalFormatFactory.digitsToPrecisionTol(digits));
		// logger.debug("Target precision set to "+target.getPrecisionTol());

		// logger.debug("end, a=" + a);
	}
}
