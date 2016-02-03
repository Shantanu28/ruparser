/*****************************************************************************************
 * Source File: MathParserTrackException.java
 ****************************************************************************************/
package net.ruready.parser.service.exception;

import net.ruready.parser.rl.ParserNames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Signals that a parser cannot complete its matching of a string because it encountered
 * an unexpected assembly element. Saves error message in a field.@author Nava L. Livne
 * <i>&lt;nlivne@aoce.utah.edu&gt;</i> Academic Outreach and Continuing Education (AOCE)
 * 1901 East South Campus Dr., Room 2197-E University of Utah, Salt Lake City, UT 84112
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
public class MathParserTrackException extends MathParserException
{
	// ========================= CONSTANTS =================================

	/**
	 * @serial Serializable version identifier.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A logger that helps identify this class' printouts.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(MathParserTrackException.class);

	// ========================= FIELDS ====================================

	/**
	 * Error message part: after which element did we fail?
	 */
	private final String after;

	/**
	 * Error message parts: expect element.
	 */
	private final String expected;

	/**
	 * Error message parts: element found.
	 */
	private final String found;

	// ========================= CONSTRUCTORS ==============================

	/**
	 * Constructs a <code>MathParserTrackException</code> with the specified reasons for
	 * the exception.
	 * 
	 * @param expression
	 *            Math expression for which the error is reported
	 * @param name
	 *            name of last parsed element
	 * @param after
	 *            an indication of what text was parsed
	 * @param expected
	 *            an indication of what kind of thing was expected, such as a ')' token
	 * @param found
	 *            the text the thrower actually found
	 */
	public MathParserTrackException(String expression, String name, String after,
			String expected, String found)
	{
		super(expression, "\nAfter : " + after + "\nExpected: " + expected + "\nFound : "
				+ found, ParserNames.KEY.MATH_EXCEPTION.SEQUENCE_TRACK, after, expected,
				found);
		logger.debug(this.getMessage());
		this.after = after;
		this.found = found;
		this.expected = expected;
	}

	// ========================= GETTERS & SETTERS =========================

	/**
	 * Returns some indication of what text was interpretable.
	 * 
	 * @return some indication of what text was interpretable
	 */
	public String getAfter()
	{
		return after;
	}

	/**
	 * Returns some indication of what kind of thing was expected, such as a ')' token.
	 * 
	 * @return some indication of what kind of thing was expected, such as a ')' token
	 */
	public String getExpected()
	{
		return expected;
	}

	/**
	 * Returns the text element the thrower actually found when it expected something
	 * else.
	 * 
	 * @return the text element the thrower actually found when it expected something else
	 */
	public String getFound()
	{
		return found;
	}
}
