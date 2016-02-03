/*****************************************************************************************
 * Source File: TrackFailAssembler.java
 ****************************************************************************************/
package net.ruready.parser.arithmetic.assembler;

import net.ruready.common.misc.Auxiliary;
import net.ruready.common.parser.core.assembler.TrackAssembler;
import net.ruready.common.parser.core.entity.Assembly;
import net.ruready.common.exception.InternationalizableErrorMessage;
import net.ruready.parser.arithmetic.entity.mathvalue.BinaryOperation;
import net.ruready.parser.math.entity.MathTarget;
import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An assembler to be invoked when a sequence track fails.
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
@Deprecated
class TrackFailAssembler extends TrackAssembler implements Auxiliary
{
	// ========================= CONSTANTS =================================

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(TrackFailAssembler.class);

	// The implicit multiplication operation
	public static final BinaryOperation OP = BinaryOperation.TIMES;

	// ========================= FIELDS ====================================

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Constructs an assembler to gracefully handle a sequence failure.
	 */
	public TrackFailAssembler()
	{

	}

	// ========================= IMPLEMENTATION: TrackAssembler ============

	/**
	 * Handles a sequence failure and gives specified reasons for the failure.
	 * 
	 * @param a
	 *            the assembly to work on
	 * @param after
	 *            an indication of what text was parsed
	 * @param expected
	 *            an indication of what kind of thing was expected, such as a ')' token
	 * @param found
	 *            the text the thrower actually found
	 */
	@Override
	protected void workOn(Assembly a, String after, String expected, String found)
	{
		// logger.debug("start, a=" + a);

		MathTarget target = (MathTarget) a.getTarget();
		String message = "Syntax Error: " + ParserNames.KEY.MATH_EXCEPTION.SEQUENCE_TRACK
				+ " after '" + after + "' expected '" + expected + "' found '" + found
				+ "'";
		target.addSyntaxError(new InternationalizableErrorMessage(message,
				ParserNames.KEY.MATH_EXCEPTION.SEQUENCE_TRACK, after, expected, found));
		logger.debug(message);

		// logger.debug("end, a=" + a);
	}
	// ========================= METHODS ===================================

}
